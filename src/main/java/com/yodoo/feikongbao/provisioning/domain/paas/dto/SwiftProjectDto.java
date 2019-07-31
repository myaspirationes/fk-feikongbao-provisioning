package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

import java.math.BigDecimal;

/**
 * @Description ：存储
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class SwiftProjectDto extends BaseDto {

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

    /** 公司id **/
    private Integer companyId;

    public String getProjectName() {
        return projectName;
    }

    public SwiftProjectDto setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public BigDecimal getMaxSize() {
        return maxSize;
    }

    public SwiftProjectDto setMaxSize(BigDecimal maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public BigDecimal getUserSize() {
        return userSize;
    }

    public SwiftProjectDto setUserSize(BigDecimal userSize) {
        this.userSize = userSize;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SwiftProjectDto setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public SwiftProjectDto setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public SwiftProjectDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

}
