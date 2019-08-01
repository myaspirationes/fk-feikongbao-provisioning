package com.yodoo.feikongbao.provisioning.enums;

/**
 * http://www.cnblogs.com/doubleten/p/3678379.html
 *
 * @Description TODO
 * @Author jinjun_luo
 * @Date 2019/4/11 9:33
 **/
public enum MqResponseEnum {

    /**
     * 操作成功
     **/
    SUCCESS(200, "操作成功"),
    /**
     * 创建 vhost name 成功
     **/
    CREATE_SUCCESS(201, "创建 vhost name 成功"),
    /**
     * 删除 vhost name 成功
     **/
    DELETE_SUCCESS(204, "删除 vhost name 成功"),
    /**
     * 操作失败
     **/
    FAIL(1001, "操作失败"),
    /**
     * vhost name 已存在
     **/
    EXIST(1002, " vhost name 已存在"),
    /**
     * vhost name 不存在
     **/
    NO_EXIST(1003, " vhost name 不存在");

    public int code;
    public String message;

    MqResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}