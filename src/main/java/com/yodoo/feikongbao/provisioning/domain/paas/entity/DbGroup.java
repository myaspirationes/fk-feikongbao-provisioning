package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import javax.persistence.Table;

/**
 * DB数据库组
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
@Table(name = "db_group")
public class DbGroup extends BaseEntity {

    /**
     * 组code
     **/
    private String groupCode;

    /**
     * 组名称
     **/
    private String groupName;

    public DbGroup() {
    }

    public DbGroup(String groupCode, String groupName) {
        this.groupCode = groupCode;
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

}