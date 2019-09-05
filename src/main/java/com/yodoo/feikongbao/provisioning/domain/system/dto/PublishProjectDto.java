package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

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
    private Integer companyId;

    /**
     * 发部项目列表
     */
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