package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

/**
 * 集团表
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public class Groups extends BaseEntity {

    /**
     * 集团名称
     **/
    private String groupName;

    /**
     * 集团Code
     **/
    private String groupCode;

    /**
     * 到期日
     **/
    private Date expireDate;

    /**
     * 更新周期
     **/
    private String updateCycle;

    /**
     * 下次更新日期
     **/
    private Date nextUpdateDate;

    public Groups() {
    }

    public Groups(String groupName, String groupCode) {
        this.groupName = groupName;
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getUpdateCycle() {
        return updateCycle;
    }

    public void setUpdateCycle(String updateCycle) {
        this.updateCycle = updateCycle == null ? null : updateCycle.trim();
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

}