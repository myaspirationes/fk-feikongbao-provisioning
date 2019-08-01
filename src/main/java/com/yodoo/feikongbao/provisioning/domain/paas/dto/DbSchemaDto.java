package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：数据库表
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
public class DbSchemaDto extends BaseDto {

    /** DB实例id **/
    private Integer dbInstanceId;

    /** DB数据库组id **/
    private Integer dbGroupId;

    /** 数据库名 **/
    private String schemaName;

    /** 用户名 **/
    private String username;

    /** 密码 **/
    private String password;

    /** 状态 **/
    private Integer status;

    /** 类型，0：主，1：从 **/
    private Integer type;

    /** 公司id **/
    private Integer companyId;

    /** 公司Code **/
    private String companyCode;

    /** 初始化数据库版本 **/
    private String targetVersion;

    /** DB实例表 **/
    private DbInstanceDto dbInstanceDto;

    public Integer getDbInstanceId() {
        return dbInstanceId;
    }

    public DbSchemaDto setDbInstanceId(Integer dbInstanceId) {
        this.dbInstanceId = dbInstanceId;
        return this;
    }

    public Integer getDbGroupId() {
        return dbGroupId;
    }

    public DbSchemaDto setDbGroupId(Integer dbGroupId) {
        this.dbGroupId = dbGroupId;
        return this;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public DbSchemaDto setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DbSchemaDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DbSchemaDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public DbSchemaDto setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public DbSchemaDto setType(Integer type) {
        this.type = type;
        return this;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public DbSchemaDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public DbSchemaDto setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public DbSchemaDto setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
        return this;
    }

    public DbInstanceDto getDbInstanceDto() {
        return dbInstanceDto;
    }

    public DbSchemaDto setDbInstanceDto(DbInstanceDto dbInstanceDto) {
        this.dbInstanceDto = dbInstanceDto;
        return this;
    }
}
