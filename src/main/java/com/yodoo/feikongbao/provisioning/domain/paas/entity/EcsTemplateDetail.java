package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * ECS 模板明细表
 */
public class EcsTemplateDetail extends BaseEntity {

    /** 模板id **/
    private Integer templateId;

    /** 请求参数code **/
    private String requestCode;

    /** 请求参数名称 **/
    private String requestName;

    /** 请求参数value **/
    private String requestValue;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode == null ? null : requestCode.trim();
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName == null ? null : requestName.trim();
    }

    public String getRequestValue() {
        return requestValue;
    }

    public void setRequestValue(String requestValue) {
        this.requestValue = requestValue == null ? null : requestValue.trim();
    }

}