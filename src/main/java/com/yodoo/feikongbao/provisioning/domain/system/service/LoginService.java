package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.domain.system.dto.LoginDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.User;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Date 2019/7/26 13:42
 * @Created by houzhen
 */
@Service
public class LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MenuTreeService menuTreeService;

    public LoginDto index() {
        LoginDto LoginDto = new LoginDto();
        // 查询用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = (String) authentication.getPrincipal();
            if (!StringUtils.isEmpty(username)) {
                User user = userService.getUserByAccount(username);
                if (user != null) {
                    // 查询用户功能菜单
                    List<MenuDto> menuTree = menuTreeService.getMenuTree(user.getId());
                    if (!CollectionUtils.isEmpty(menuTree)) {
                        this.addLink("/", menuTree);
                    }
                    LoginDto.setAccount(user.getAccount());
                    LoginDto.setName(user.getName());
                    LoginDto.setMenuTree(menuTree);
                }
            }
        }
        return LoginDto;
    }

    // 添加链接
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
