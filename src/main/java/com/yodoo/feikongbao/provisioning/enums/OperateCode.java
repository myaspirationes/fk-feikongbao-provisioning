package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Author houzhen
 * @Date 10:36 2019/5/15
 **/
public enum OperateCode {
    READ("read", "查询"),
    ADD("add", "新增"),
    EDIT("edit", "编辑"),
    DELETE("delete", "删除"),
    ITEM("item", "明细");

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private OperateCode(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
