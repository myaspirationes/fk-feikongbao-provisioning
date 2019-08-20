package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * ECS 模板表
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public class EcsTemplate extends BaseEntity {

    /**
     * ECS 类型
     **/
    private String ecsType;

    /**
     * 模板名称
     **/
    private String name;

    /**
     * 备注
     **/
    private String remark;

    public String getEcsType() {
        return ecsType;
    }

    public void setEcsType(String ecsType) {
        this.ecsType = ecsType == null ? null : ecsType.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

}