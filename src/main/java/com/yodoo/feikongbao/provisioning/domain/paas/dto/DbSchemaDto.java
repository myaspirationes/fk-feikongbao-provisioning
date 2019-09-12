package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：数据库表
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
public class DbSchemaDto extends BaseDto {

    /**
     * DB实例id
     **/
    @ApiModelProperty(value = "DB实例id", required = true, example = "1", position = 1)
    private Integer dbInstanceId;

    /**
     * DB数据库组id
     **/
    @ApiModelProperty(value = "DB数据库组id", required = true, example = "1", position = 2)
    private Integer dbGroupId;

    /**
     * 数据库名
     **/
    @ApiModelProperty(value = "数据库名", required = true, example = "test", position = 3)
    private String schemaName;

    /**
     * 用户名
     **/
    @ApiModelProperty(value = "用户名", required = true, example = "test", position = 4)
    private String username;

    /**
     * 密码
     **/
    @ApiModelProperty(value = "密码", required = true, example = "test", position = 5)
    private String password;

    /**
     * 状态：0 未被使用，1 已被使用
     **/
    @ApiModelProperty(value = "状态：0 未被使用，1 已被使用", required = false, example = "0", position = 6)
    private Integer status;

    /**
     * 类型，0：主，1：从
     **/
    @ApiModelProperty(value = "类型，0：主，1：从", required = true, example = "0", position = 7)
    private Integer type;

    /**
     * 公司id
     **/
    @ApiModelProperty(value = "公司id", required = false, example = "0", position = 8)
    private Integer companyId;

    /**
     * 公司Code
     **/
    @ApiModelProperty(value = "公司Code", required = false, example = "0", position = 9)
    private String companyCode;

    /**
     * 初始化数据库版本
     **/
    @ApiModelProperty(value = "初始化数据库版本", required = false, example = "0", position = 10)
    private String targetVersion;

    /**
     * DB实例表
     **/
    @ApiModelProperty(hidden = true)
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
