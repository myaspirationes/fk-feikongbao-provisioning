package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：功能模块
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
public class FunctionModuleDto extends BaseDto {

    /** 父类id **/
    private Integer parentId;

    /** 功能模块名称 **/
    private String name;

    /** 描述 **/
    private String description;

    /** 序号 **/
    private String orderNo;

    public Integer getParentId() {
        return parentId;
    }

    public FunctionModuleDto setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getName() {
        return name;
    }

    public FunctionModuleDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FunctionModuleDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public FunctionModuleDto setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }
}
