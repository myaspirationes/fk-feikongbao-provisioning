package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.util.MD5Util;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

}
