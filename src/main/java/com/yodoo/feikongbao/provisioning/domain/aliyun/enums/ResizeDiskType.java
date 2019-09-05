package com.yodoo.feikongbao.provisioning.domain.aliyun.enums;

/**
 * @Description 用户固定参数值
 * @Author jinjun_luo
 * @Date 2019/6/10 16:54
 **/
public enum ResizeDiskType {

    /** 离线扩容，您需要重启实例以完成扩容 **/
    OFF_LINE("offline"),

    /** 在线扩容 **/
    ON_LINE("online");

    private String value;

    public String getValue() {
        return value;
    }

    ResizeDiskType(String value){
        this.value = value;
    }

}
