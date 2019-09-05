package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：发部项目详情
 * @Author ：jinjun_luo
 * @Date ： 2019/9/4 0004
 */
public class PublishProjectDetails extends BaseDto {

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

    public Integer getVmInstanceId() {
        return vmInstanceId;
    }

    public PublishProjectDetails setVmInstanceId(Integer vmInstanceId) {
        this.vmInstanceId = vmInstanceId;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public PublishProjectDetails setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getProjectType() {
        return projectType;
    }

    public PublishProjectDetails setProjectType(String projectType) {
        this.projectType = projectType;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public PublishProjectDetails setVersion(String version) {
        this.version = version;
        return this;
    }
}
