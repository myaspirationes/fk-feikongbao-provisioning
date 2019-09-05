package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：ecs 模板详情
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
public class EcsTemplateDetailDto extends BaseDto {

    /**
     * 模板id
     **/
    private Integer templateId;

    /**
     * 请求参数code
     **/
    private String requestCode;

    /**
     * 请求参数名称
     **/
    private String requestName;

    /**
     * 请求参数value
     **/
    private String requestValue;

    public Integer getTemplateId() {
        return templateId;
    }

    public EcsTemplateDetailDto setTemplateId(Integer templateId) {
        this.templateId = templateId;
        return this;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public EcsTemplateDetailDto setRequestCode(String requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public String getRequestName() {
        return requestName;
    }

    public EcsTemplateDetailDto setRequestName(String requestName) {
        this.requestName = requestName;
        return this;
    }

    public String getRequestValue() {
        return requestValue;
    }

    public EcsTemplateDetailDto setRequestValue(String requestValue) {
        this.requestValue = requestValue;
        return this;
    }
}
