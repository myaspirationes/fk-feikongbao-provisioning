package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * 公司部署项目表
 *
 * @Date 2019/7/9 17:28
 * @Author by houzhen
 */
public class PublishProjectDto extends BaseDto {

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
     * 版本
     **/
    private String version;

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
        this.projectName = projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}