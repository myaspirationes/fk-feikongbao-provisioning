package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：默认数据库名
 * @Author ：jinjun_luo
 * @Date ： 2019/8/27 0027
 */
public enum  DefaultDataBaseEnum {

    /**
     * 默认数据库名
     **/
    DATA_BASE("mysql", "默认数据库名");

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    DefaultDataBaseEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
