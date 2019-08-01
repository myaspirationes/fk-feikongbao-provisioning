package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 目标集团表
 */
public class UserPermissionTargetGroupDetails extends BaseEntity {

    /**
     * 用户权限id
     **/
    private Integer userPermissionId;

    /**
     * 集团id
     **/
    private Integer groupId;

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}