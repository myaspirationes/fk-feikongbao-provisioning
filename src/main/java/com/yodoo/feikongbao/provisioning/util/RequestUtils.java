package com.yodoo.feikongbao.provisioning.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @Date 2019/7/10 15:32
 * @Created by houzhen
 */
public class RequestUtils {

    /**
     * 获取服务物理路径
     *
     * @Author houzhen
     * @Date 15:33 2019/7/10
     **/
    public static String getPhysicalPath(HttpServletRequest request) {
        String getContextPath = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + getContextPath + "/";
        return basePath;
    }

    /**
     * 获取url
     *
     * @Author houzhen
     * @Date 15:34 2019/7/10
     **/
    public static String getRequestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }
}
