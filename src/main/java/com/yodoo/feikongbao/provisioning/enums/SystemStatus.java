package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Author houzhen
 * @Date 10:36 2019/5/15
 **/
public enum SystemStatus {
    /**
     * 未登陆
     **/
    NOLOGIN("-1"),
    /**
     * 成功
     **/
    SUCCESS("0"),
    /**
     * 失败
     **/
    FAIL("1"),
    /**
     * 禁止
     **/
    FORBIDDEN("2");

    private String status;

    public String getStatus() {
        return status;
    }

    SystemStatus(String status) {
        this.status = status;
    }
}
