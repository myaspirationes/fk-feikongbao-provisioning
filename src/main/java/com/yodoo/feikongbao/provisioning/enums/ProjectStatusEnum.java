package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：公司创建
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public enum ProjectStatusEnum {

    /**
     * 待发布
     **/
    TOPUBLISH(0, "待发布"),
    /**
     * 运行中
     **/
    RUNNING(1, "运行中"),
    /**
     * 失败
     **/
    FAIL(2, "失败"),
    /**
     * 已停止
     **/
    STOP(3, "已停止");

    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    ProjectStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
