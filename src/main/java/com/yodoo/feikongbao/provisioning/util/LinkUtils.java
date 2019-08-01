package com.yodoo.feikongbao.provisioning.util;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2019/7/26 17:40
 * @Created by houzhen
 */
public class LinkUtils {

    /**
     * 设置自身link
     *
     * @Author houzhen
     * @Date 10:40 2019/7/11
     **/
    public static <T extends ResourceSupport> void setSelfLink(T t, Class clazz) {
        t.add(new Link(LinkUtils.getRequestMappingValue(clazz)).withSelfRel());
    }


    /**
     * 设置列表item link
     *
     * @Author houzhen
     * @Date 10:40 2019/7/11
     **/
    public static void setItemListLink(List<? extends BaseDto> list, Class clazz) {
        if (!CollectionUtils.isEmpty(list)) {
            String baseUrl = LinkUtils.getRequestMappingValue(clazz);
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
    public static <T extends ResourceSupport> void setResourceLink(T t, Class clazz, List permissionList, String... resources) {
        List<Link> links = new ArrayList<>();
        String baseUrl = getRequestMappingValue(clazz);
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

    public static String getRequestMappingValue(Class clazz) {
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
