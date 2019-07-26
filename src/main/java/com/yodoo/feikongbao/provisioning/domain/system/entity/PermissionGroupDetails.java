package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 权限组明细表
 */
public class PermissionGroupDetails extends BaseEntity {

    /** 权限组id **/
    private Integer permissionGroupId;

    /** 权限id **/
    private Integer permissionId;

    public Integer getPermissionGroupId() {
        return permissionGroupId;
    }

    public void setPermissionGroupId(Integer permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

}