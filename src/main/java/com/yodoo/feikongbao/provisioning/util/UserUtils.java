package com.yodoo.feikongbao.provisioning.util;

import com.yodoo.feikongbao.provisioning.contract.ProvisioningConstants;
import com.yodoo.feikongbao.provisioning.domain.system.security.ProvisioningUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Description ：用户
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
@Component
public class UserUtils {

    /**
     * session中获取用户信息
     * @return
     */
    public static Integer getUserId(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Object userObj = session.getAttribute(ProvisioningConstants.AUTH_USER);
        if (userObj != null) {
            ProvisioningUserDetails userInfo = (ProvisioningUserDetails) userObj;
            return userInfo.getId();
        }
        return null;
    }
}
