package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

import java.util.List;

/**
 * @Description ：ecs 模板
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
public class EcsTemplateDto extends BaseDto {

    /**
     * ECS 类型
     **/
    private String ecsType;

    /**
     * 模板名称
     **/
    private String name;

    /**
     * 备注
     **/
    private String remark;

    /**
     * ecs 模板详情列表
     */
    private List<EcsTemplateDetailDto> ecsTemplateDetailDtoList;

    public String getEcsType() {
        return ecsType;
    }

    public EcsTemplateDto setEcsType(String ecsType) {
        this.ecsType = ecsType;
        return this;
    }

    public String getName() {
        return name;
    }

    public EcsTemplateDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public EcsTemplateDto setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public List<EcsTemplateDetailDto> getEcsTemplateDetailDtoList() {
        return ecsTemplateDetailDtoList;
    }

    public EcsTemplateDto setEcsTemplateDetailDtoList(List<EcsTemplateDetailDto> ecsTemplateDetailDtoList) {
        this.ecsTemplateDetailDtoList = ecsTemplateDetailDtoList;
        return this;
    }
}
