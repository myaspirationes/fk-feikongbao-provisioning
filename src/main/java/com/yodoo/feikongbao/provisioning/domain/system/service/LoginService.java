package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.contract.ProvisioningConstants;
import com.yodoo.feikongbao.provisioning.domain.system.dto.LoginDto;
import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Date 2019/7/26 13:42
 * @Author by houzhen
 */
@Service
public class LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    public LoginDto login() {
        LoginDto loginDto = new LoginDto();
        // session中获取用户信息
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Object userObj = session.getAttribute(ProvisioningConstants.AUTH_USER);
        if (userObj != null) {
            ProvisioningUserDetails userInfo = (ProvisioningUserDetails) userObj;
            loginDto.setAccount(userInfo.getUsername());
            loginDto.setName(userInfo.getName());
            loginDto.setMenuTree(userInfo.getMenuTree());
        }
        return loginDto;
    }


}
