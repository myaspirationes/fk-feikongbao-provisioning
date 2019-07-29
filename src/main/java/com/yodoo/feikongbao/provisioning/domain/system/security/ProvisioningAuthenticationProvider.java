package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.contract.ProvisioningConstants;
import com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.MenuTreeService;
import com.yodoo.feikongbao.provisioning.util.MD5Util;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class ProvisioningAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    ProvisioningUserDetailsService userDetailsService;

    @Autowired
    MenuTreeService menuTreeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 用户名
        String userName = (String) authentication.getPrincipal();
        // 密码
        String password = (String) authentication.getCredentials();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new BadCredentialsException("请输入用户名或密码！");
        }
        ProvisioningUserDetails userInfo = userDetailsService.loadUserByUsername(userName);
        if (!userInfo.getPassword().equals(MD5Util.MD5Encode(password))) {
            throw new BadCredentialsException("密码不正确，请重新登陆！");
        }
        // 取得session，并将权限菜单增加到session中
        List<MenuDto> menuTree = menuTreeService.getMenuTree(userInfo.getId());
        // 添加link
        this.addLink("/", menuTree);
        userInfo.setMenuTree(menuTree);
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpSession session= attr.getRequest().getSession(true);
        session.setAttribute(ProvisioningConstants.authUser, userInfo);
        // 返回
        return new UsernamePasswordAuthenticationToken(userName, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    // 为菜单添加link
    private void addLink(String basePath, List<MenuDto> menuList) {
        if (!CollectionUtils.isEmpty(menuList)) {
            for (MenuDto menuDto : menuList) {
                if (!CollectionUtils.isEmpty(menuDto.getChildren())) {
                    this.addLink(basePath, menuDto.getChildren());
                } else {
                    Link link = new Link(basePath + menuDto.getMenuCode())
                            .withType(RequestMethod.GET.name()).withTitle(menuDto.getMenuName());
                    menuDto.add(link);
                }
            }
        }
    }

}
