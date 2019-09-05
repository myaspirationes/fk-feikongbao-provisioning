package com.yodoo.feikongbao.provisioning.domain.aliyun.enums;

/**
 * @Author houzhen
 * @Date 10:36 2019/5/15
**/
public enum DiskType {
    ALL("all"),
    SYSTEM("system"),
    DATA("data");

    private String type;

    public String getType() {
        return type;
    }

    private DiskType(String type){
        this.type = type;
    }
}
