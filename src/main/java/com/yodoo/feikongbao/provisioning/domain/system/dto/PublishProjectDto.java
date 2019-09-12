package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

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
    @ApiModelProperty(value = "公司id", required = true, example = "test_group_name", position = 1)
    private Integer companyId;

    /**
     * 发部项目列表
     */
    @ApiModelProperty(value = "发部项目列表", required = true, example = "[{vmInstanceId,projectName},{vmInstanceId,projectName}]", position = 2)
    private List<PublishProjectDetails> publishProjectDetailsList;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<PublishProjectDetails> getPublishProjectDetailsList() {
        return publishProjectDetailsList;
    }

    public PublishProjectDto setPublishProjectDetailsList(List<PublishProjectDetails> publishProjectDetailsList) {
        this.publishProjectDetailsList = publishProjectDetailsList;
        return this;
    }
}