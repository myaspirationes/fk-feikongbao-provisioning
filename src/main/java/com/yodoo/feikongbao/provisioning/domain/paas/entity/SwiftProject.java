package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.math.BigDecimal;

/**
 * swift 租户表
 */
public class SwiftProject extends BaseEntity {

    /** 租户名称 **/
    private String projectName;

    /** 最大使用范围（单位M，1M=1024B） **/
    private BigDecimal maxSize;

    /** 所属用户使用范围 **/
    private BigDecimal userSize;

    /** IP **/
    private String ip;

    /** 端口 **/
    private Integer port;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public BigDecimal getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(BigDecimal maxSize) {
        this.maxSize = maxSize;
    }

    public BigDecimal getUserSize() {
        return userSize;
    }

    public void setUserSize(BigDecimal userSize) {
        this.userSize = userSize;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}