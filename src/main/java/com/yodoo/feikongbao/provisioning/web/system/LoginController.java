package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.LoginDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.LoginService;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author houzhen
 * @Date 9:33 2019/7/4
 **/
@RestController
@RequestMapping("/")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 登录初始化列表信息
     *
     * @Author houzhen
     * @Date 9:37 2019/7/4
     **/
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ProvisioningDto<LoginDto> login() {
        LoginDto loginDto = loginService.login();
        return new ProvisioningDto<LoginDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, loginDto);
    }

}
