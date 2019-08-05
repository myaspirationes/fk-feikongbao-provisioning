package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：dbSchema 是否被使用
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
public enum  SchemaStatusEnum {

    /**
     * 未被使用
     **/
    UNUSED(0, "未被使用"),
    /**
     * 已被使用
     **/
    USED(1, "已被使用");

    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    SchemaStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
