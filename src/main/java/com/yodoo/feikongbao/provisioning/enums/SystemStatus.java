package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Author houzhen
 * @Date 10:36 2019/5/15
 **/
public enum SystemStatus {
    NOLOGIN("-1"),
    SUCCESS("0"),
    FAIL("1"),
    FORBIDDEN("2");

    private String status;

    public String getStatus() {
        return status;
    }

    private SystemStatus(String status) {
        this.status = status;
    }
}
