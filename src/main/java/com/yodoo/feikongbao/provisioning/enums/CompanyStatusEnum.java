package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：公司创建
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public enum CompanyStatusEnum {
    /**
     * 创建中
     **/
    CREATING(0, "创建中"),
    /**
     * 创建完成 或 启用中
     **/
    RUNNING(1, "创建完成 或 启用中"),
    /**
     * 停用中
     **/
    STOP(2, "停用中");

    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    CompanyStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
