package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 公司部署项目表
 *
 * @Date 2019/7/9 17:28
 * @Author by houzhen
 */
public class PublishProject extends BaseEntity {

    /**
     * 公司id
     **/
    private Integer companyId;

    /**
     * ecs实例id
     **/
    private Integer vmInstanceId;

    /**
     * 项目名称
     **/
    private String projectName;

    /**
     * 项目类型
     **/
    private String projectType;

    /**
     * IP
     **/
    private String ip;

    /**
     * 端口
     **/
    private Integer port;

    /**
     * 版本
     **/
    private String version;

    /**
     * 状态，0 待发布，1 运行中，2：已停止
     **/
    private Integer status;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getVmInstanceId() {
        return vmInstanceId;
    }

    public void setVmInstanceId(Integer vmInstanceId) {
        this.vmInstanceId = vmInstanceId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType == null ? null : projectType.trim();
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}