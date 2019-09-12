package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author houzhen
 * @Date 13:10 2019/7/29
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto extends BaseDto {

    /**
     * 集团id
     **/
    @ApiModelProperty(value = "集团id", required = false, example = "test", position = 1)
    private Integer groupId;

    /**
     * 公司名称
     **/
    @ApiModelProperty(value = "公司名称", required = true, example = "test_company", position = 2)
    private String companyName;

    /**
     * 公司Code
     **/
    @ApiModelProperty(value = "公司Code", required = true, example = "test_company_code", position = 3)
    private String companyCode;

    /**
     * 更新周期
     **/
    @ApiModelProperty(value = "更新周期", required = true, example = "1", position = 4)
    private String updateCycle;

    /**
     * 下次更新日期
     **/
    @ApiModelProperty(value = "下次更新日期", required = true, example = "2019-10-01 00:00:00", position = 5)
    private Date nextUpdateDate;

    /**
     * 到期日
     **/
    @ApiModelProperty(value = "到期日", required = true, example = "2019-10-01 00:00:00", position = 6)
    private Date expireDate;

    /**
     * DB数据库组id
     **/
    @ApiModelProperty(hidden = true)
    private Integer dbGroupId;

    /**
     * redis组id
     **/
    @ApiModelProperty(hidden = true)
    private Integer redisGroupId;

    /**
     * swift租户id
     **/
    @ApiModelProperty(hidden = true)
    private Integer swiftProjectId;

    /**
     * 消息队列vhost
     **/
    @ApiModelProperty(hidden = true)
    private Integer mqVhostId;

    /**
     * neo4j实例id
     **/
    @ApiModelProperty(hidden = true)
    private Integer neo4jInstanceId;

    /**
     * 状态，0：创建中，1：创建完成,启用中， 2：停用
     **/
    @ApiModelProperty(hidden = true)
    private Integer status;

    /**
     * DB 数据库组
     **/
    @ApiModelProperty(hidden = true)
    private DbGroupDto dbGroupDto;

    /**
     * redis 实例组
     **/
    @ApiModelProperty(hidden = true)
    private RedisGroupDto redisGroupDto;

    /**
     * 对象存储
     **/
    @ApiModelProperty(hidden = true)
    private SwiftProjectDto swiftProjectDto;

    /**
     * 消息信息
     **/
    @ApiModelProperty(hidden = true)
    private MqVhostDto mqVhostDto;

    /**
     * 流程实例信息
     **/
    @ApiModelProperty(hidden = true)
    private Neo4jInstanceDto neo4jInstanceDto;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getUpdateCycle() {
        return updateCycle;
    }

    public void setUpdateCycle(String updateCycle) {
        this.updateCycle = updateCycle;
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getDbGroupId() {
        return dbGroupId;
    }

    public void setDbGroupId(Integer dbGroupId) {
        this.dbGroupId = dbGroupId;
    }

    public Integer getRedisGroupId() {
        return redisGroupId;
    }

    public void setRedisGroupId(Integer redisGroupId) {
        this.redisGroupId = redisGroupId;
    }

    public Integer getSwiftProjectId() {
        return swiftProjectId;
    }

    public void setSwiftProjectId(Integer swiftProjectId) {
        this.swiftProjectId = swiftProjectId;
    }

    public Integer getMqVhostId() {
        return mqVhostId;
    }

    public void setMqVhostId(Integer mqVhostId) {
        this.mqVhostId = mqVhostId;
    }

    public Integer getNeo4jInstanceId() {
        return neo4jInstanceId;
    }

    public void setNeo4jInstanceId(Integer neo4jInstanceId) {
        this.neo4jInstanceId = neo4jInstanceId;
    }

    public Integer getStatus() {
        return status;
    }

    public CompanyDto setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public DbGroupDto getDbGroupDto() {
        return dbGroupDto;
    }

    public CompanyDto setDbGroupDto(DbGroupDto dbGroupDto) {
        this.dbGroupDto = dbGroupDto;
        return this;
    }

    public RedisGroupDto getRedisGroupDto() {
        return redisGroupDto;
    }

    public CompanyDto setRedisGroupDto(RedisGroupDto redisGroupDto) {
        this.redisGroupDto = redisGroupDto;
        return this;
    }

    public SwiftProjectDto getSwiftProjectDto() {
        return swiftProjectDto;
    }

    public CompanyDto setSwiftProjectDto(SwiftProjectDto swiftProjectDto) {
        this.swiftProjectDto = swiftProjectDto;
        return this;
    }

    public MqVhostDto getMqVhostDto() {
        return mqVhostDto;
    }

    public CompanyDto setMqVhostDto(MqVhostDto mqVhostDto) {
        this.mqVhostDto = mqVhostDto;
        return this;
    }

    public Neo4jInstanceDto getNeo4jInstanceDto() {
        return neo4jInstanceDto;
    }

    public CompanyDto setNeo4jInstanceDto(Neo4jInstanceDto neo4jInstanceDto) {
        this.neo4jInstanceDto = neo4jInstanceDto;
        return this;
    }
}
