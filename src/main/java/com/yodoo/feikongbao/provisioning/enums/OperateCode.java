package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Author houzhen
 * @Date 10:36 2019/5/15
 **/
public enum OperateCode {
    /**
     * 查询
     **/
    READ("read", "查询"),
    /**
     * 新增
     **/
    ADD("add", "新增"),
    /**
     * 编辑
     **/
    EDIT("edit", "编辑"),
    /**
     * 删除
     **/
    DELETE("delete", "删除"),
    /**
     * 明细
     **/
    ITEM("item", "明细");

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    OperateCode(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
