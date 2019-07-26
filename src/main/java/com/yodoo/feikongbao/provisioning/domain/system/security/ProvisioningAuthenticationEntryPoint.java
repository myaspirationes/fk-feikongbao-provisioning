package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.util.JsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录信息
 *
 * @Date 2019/7/9 17:28
 * @Created by houzhen
 */
@Component
public class ProvisioningAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ProvisioningDto responseBody = new ProvisioningDto();
        responseBody.setStatus(SystemStatus.NOLOGIN.getStatus());
        responseBody.setMessage("用户未登录，请先登录！");
        httpServletResponse.getWriter().write(JsonUtils.obj2json(responseBody));
    }
}
