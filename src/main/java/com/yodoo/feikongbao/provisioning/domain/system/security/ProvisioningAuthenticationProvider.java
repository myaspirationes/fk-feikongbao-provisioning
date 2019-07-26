package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.util.MD5Util;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProvisioningAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    ProvisioningUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 用户名
        String userName = (String) authentication.getPrincipal();
        // 密码
        String password = (String) authentication.getCredentials();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new BadCredentialsException("请输入用户名或密码！");
        }
        UserDetails userInfo = userDetailsService.loadUserByUsername(userName);
        if (!userInfo.getPassword().equals(MD5Util.MD5Encode(password))) {
            throw new BadCredentialsException("密码不正确，请重新登陆！");
        }
        return new UsernamePasswordAuthenticationToken(userName, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    /**
     * 设置自身link
     *
     * @Author houzhen
     * @Date 10:40 2019/7/11
     **/
    public <T extends ResourceSupport> void setSelfLink(T t, Class clazz) {
        t.add(new Link(this.getRequestMappingValue(clazz)).withSelfRel());
    }

    /**
     * 设置列表item link
     *
     * @Author houzhen
     * @Date 10:40 2019/7/11
     **/
    public void setItemListLink(List<? extends BaseDto> list, Class clazz) {
        if (!CollectionUtils.isEmpty(list)) {
            String baseUrl = this.getRequestMappingValue(clazz);
            for (BaseDto response : list) {
                response.add(new Link(baseUrl + "/" + response.getTid()).withRel(OperateCode.ITEM.getCode())
                        .withType(RequestMethod.GET.name()).withTitle(OperateCode.ITEM.getName()));
            }
        }
    }

    /**
     * 查新改资源下的所有link
     *
     * @Author houzhen
     * @Date 10:41 2019/7/11
     **/
    public <T extends ResourceSupport> void setResourceLink(T t, Class clazz, List permissionList, String... resources) {
        List<Link> links = new ArrayList<>();
        String baseUrl = this.getRequestMappingValue(clazz);
        // self link
        links.add(new Link(baseUrl).withType(RequestMethod.GET.name()).withTitle(OperateCode.READ.getName()));
        // other permission link
        if (!CollectionUtils.isEmpty(permissionList) && resources != null && resources.length > 0) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String auth = authority.getAuthority();
                if (!StringUtils.isEmpty(auth) && permissionList.contains(auth)) {
                    for (String resource : resources) {
                        Link link = null;
                        if (resource.equalsIgnoreCase(OperateCode.ADD.getCode())) {
                            link = new Link(baseUrl).withRel(OperateCode.ADD.getCode()).withType(RequestMethod.POST.name()).withTitle(OperateCode.ADD.getName());
                        } else if (resource.equalsIgnoreCase(OperateCode.EDIT.getCode())) {
                            link = new Link(baseUrl).withRel(OperateCode.EDIT.getCode()).withType(RequestMethod.PUT.name()).withTitle(OperateCode.EDIT.getName());
                        } else if (resource.equalsIgnoreCase(OperateCode.DELETE.getCode())) {
                            link = new Link(baseUrl).withRel(OperateCode.DELETE.getCode()).withType(RequestMethod.DELETE.name()).withTitle(OperateCode.DELETE.getName());
                        } else {
                            link = new Link(baseUrl + "/" + resource).withRel(resource).withType(RequestMethod.POST.name());
                        }
                        links.add(link);
                    }
                }
            }
        }
        t.add(links);
    }

    private String getRequestMappingValue(Class clazz) {
        //获取RequestMapping注解
        RequestMapping anno = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
        //获取类注解的value值
        String value = "";
        String[] valueArr = anno.value();
        if (valueArr != null && valueArr.length > 0) {
            value = valueArr[0];
        }
        return value;
    }
}
