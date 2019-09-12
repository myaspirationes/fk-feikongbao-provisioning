package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：发部项目详情
 * @Author ：jinjun_luo
 * @Date ： 2019/9/4 0004
 */
public class PublishProjectDetails extends BaseDto {

    /**
     * ecs实例id
     **/
    @ApiModelProperty(value = "ecs实例id", required = true, example = "i-uf69qbbnfvxl7sl0p51d", position = 3)
    private Integer vmInstanceId;

    /**
     * 项目名称
     **/
    @ApiModelProperty(value = "项目名称", required = true, example = "provision_messaging-", position = 4)
    private String projectName;

    /**
     * 项目类型
     **/
    @ApiModelProperty(value = "项目类型", required = false, example = "test", position = 5)
    private String projectType;

    /**
     * 版本
     **/
    @ApiModelProperty(value = "版本", required = false, example = "v1", position = 6)
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
