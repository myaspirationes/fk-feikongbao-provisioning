package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * DB 数据库表
 */
public class DbSchema extends BaseEntity {

    /**
     * DB实例id
     **/
    private Integer dbInstanceId;

    /**
     * DB数据库组id
     **/
    private Integer dbGroupId;

    /**
     * 数据库名
     **/
    private String schemaName;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 密码
     **/
    private String password;

    /**
     * 状态
     **/
    private Integer status;

    /**
     * 类型，0：主，1：从
     **/
    private Integer type;

    public Integer getDbInstanceId() {
        return dbInstanceId;
    }

    public void setDbInstanceId(Integer dbInstanceId) {
        this.dbInstanceId = dbInstanceId;
    }

    public Integer getDbGroupId() {
        return dbGroupId;
    }

    public void setDbGroupId(Integer dbGroupId) {
        this.dbGroupId = dbGroupId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName == null ? null : schemaName.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}