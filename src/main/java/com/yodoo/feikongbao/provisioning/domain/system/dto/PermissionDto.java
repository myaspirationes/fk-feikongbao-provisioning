package com.yodoo.feikongbao.provisioning.domain.system.dto;


import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * 权限
 * @Author houzhen
 * @Date 15:22 2019/8/5
**/
public class PermissionDto extends BaseDto {

    /**
     * 权限code
     **/
    private String permissionCode;

    /**
     * 权限名称
     **/
    private String permissionName;

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
