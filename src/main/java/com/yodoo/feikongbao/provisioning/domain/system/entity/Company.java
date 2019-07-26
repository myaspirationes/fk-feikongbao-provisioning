package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class Company extends BaseEntity {

    private Integer groupId;

    private String companyName;

    private String companyCode;

    private String updateCycle;

    private Date nextUpdateDate;

    private Date expireDate;

    private Integer dbGroupId;

    private Integer redisGroupId;

    private Integer swiftProjectId;

    private Integer mqVhostId;

    private Integer neo4jInstanceId;

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
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getUpdateCycle() {
        return updateCycle;
    }

    public void setUpdateCycle(String updateCycle) {
        this.updateCycle = updateCycle == null ? null : updateCycle.trim();
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

}