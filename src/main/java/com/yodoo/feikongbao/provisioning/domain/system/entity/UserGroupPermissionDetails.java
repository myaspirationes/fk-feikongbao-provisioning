package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 用户组权限表
 */
public class UserGroupPermissionDetails extends BaseEntity {

    /**
     * 用户组id
     */
    private Integer userGroupId;

    /**
     * 权限组id
     */
    private Integer permissionGroupId;

    public UserGroupPermissionDetails() {
    }

    public UserGroupPermissionDetails(Integer userGroupId, Integer permissionGroupId) {
        this.userGroupId = userGroupId;
        this.permissionGroupId = permissionGroupId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getPermissionGroupId() {
        return permissionGroupId;
    }

    public void setPermissionGroupId(Integer permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
    }

}