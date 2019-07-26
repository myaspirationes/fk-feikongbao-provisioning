package com.yodoo.feikongbao.provisioning.entity.paasmapper;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class EcsTemplate extends BaseEntity {

    private String ecsType;

    private String name;

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