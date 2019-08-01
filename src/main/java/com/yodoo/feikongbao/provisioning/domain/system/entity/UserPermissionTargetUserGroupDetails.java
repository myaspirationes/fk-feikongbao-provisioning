package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 用户管理用户组权限表
 */
public class UserPermissionTargetUserGroupDetails extends BaseEntity {

    /**
     * 用户组id
     */
    private Integer userGroupId;

    /**
     * 用户权限组id
     */
    private Integer userPermissionId;

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

}