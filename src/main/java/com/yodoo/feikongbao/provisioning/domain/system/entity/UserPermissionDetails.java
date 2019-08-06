package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 用户权限表
 */
public class UserPermissionDetails extends BaseEntity {

    /**
     * 用户id
     **/
    private Integer userId;

    /**
     * 权限id
     **/
    private Integer permissionId;

    public UserPermissionDetails() {
    }

    public UserPermissionDetails(Integer userId, Integer permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

}