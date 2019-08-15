package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.contract.ProvisioningConstants;
import com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.MenuManagerApiService;
import com.yodoo.feikongbao.provisioning.util.Md5Util;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProvisioningAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    ProvisioningUserDetailsServiceImpl userDetailsService;

//    @Autowired
//    MenuTreeService menuTreeService;

    @Autowired
    private MenuManagerApiService menuManagerApiService;

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
        if (!userInfo.getPassword().equals(Md5Util.md5Encode(password))) {
            throw new BadCredentialsException("密码不正确，请重新登陆！");
        }
        // 取得session，并将权限菜单增加到session中
        List<com.yodoo.megalodon.permission.dto.MenuDto> menuTree = menuManagerApiService.getMenuTree(userInfo.getId());
        List<MenuDto> menuTreeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(menuTree)){
            menuTreeList = menuTree.stream().filter(Objects::nonNull).map(menuDto -> {
                MenuDto menuDtoResponse = new MenuDto();
                BeanUtils.copyProperties(menuDto, menuDtoResponse);
                menuDtoResponse.setTid(menuDto.getId());
                List<com.yodoo.megalodon.permission.dto.MenuDto> children = menuDto.getChildren();
                if (!CollectionUtils.isEmpty(children)){
                    List<MenuDto> menuDtoChildrenList = children.stream().filter(Objects::nonNull).map(menuDtoChildren -> {
                        MenuDto menuDtoChildrenResponse = new MenuDto();
                        BeanUtils.copyProperties(menuDtoChildren, menuDtoChildrenResponse);
                        menuDtoChildrenResponse.setTid(menuDtoChildren.getId());
                        return menuDtoChildrenResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                    menuDtoResponse.setChildren(menuDtoChildrenList);
                }
                return menuDtoResponse;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        // 添加link
        this.addLink("/", menuTreeList);
        userInfo.setMenuTree(menuTreeList);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute(ProvisioningConstants.AUTH_USER, userInfo);
        // 返回
        return new UsernamePasswordAuthenticationToken(userInfo.getId(), password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    /**
     * 为菜单添加link
     *
     * @param basePath
     * @param menuList
     */
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
