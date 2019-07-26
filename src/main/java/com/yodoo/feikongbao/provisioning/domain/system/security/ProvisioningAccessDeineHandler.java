package com.yodoo.feikongbao.provisioning.domain.system.security;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.util.JsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Date 2019/7/19 9:38
 * @Created by houzhen
 */
@Component
public class ProvisioningAccessDeineHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ProvisioningDto responseBody = new ProvisioningDto();
        responseBody.setStatus(SystemStatus.FORBIDDEN.getStatus());
        responseBody.setMessage("没有访问权限！");
        response.getWriter().write(JsonUtils.obj2json(responseBody));
    }
}
