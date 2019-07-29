package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.contract.ProvisioningConstants;
import com.yodoo.feikongbao.provisioning.domain.system.dto.LoginDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.MenuDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.User;
import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningUserDetails;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Date 2019/7/26 13:42
 * @Created by houzhen
 */
@Service
public class LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    public LoginDto login() {
        LoginDto LoginDto = new LoginDto();
        // session中获取用户信息
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session= attr.getRequest().getSession(true);
        Object userObj = session.getAttribute(ProvisioningConstants.authUser);
        if (userObj != null) {
            ProvisioningUserDetails userInfo = (ProvisioningUserDetails) userObj;
            LoginDto.setAccount(userInfo.getUsername());
            LoginDto.setName(userInfo.getName());
            LoginDto.setMenuTree(userInfo.getMenuTree());
        }
        return LoginDto;
    }


}
