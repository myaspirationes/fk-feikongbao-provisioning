package com.yodoo.feikongbao.provisioning.entity.applymapper;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class UserGroup extends BaseEntity {

    private String groupCode;

    private String groupName;

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