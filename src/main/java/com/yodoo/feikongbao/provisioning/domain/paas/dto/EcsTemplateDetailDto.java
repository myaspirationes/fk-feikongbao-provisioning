package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：ecs 模板详情
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
public class EcsTemplateDetailDto extends BaseDto {

    /**
     * 模板id
     **/
    @ApiModelProperty(value = "模板id", required = true, example = "0", position = 1)
    private Integer templateId;

    /**
     * 请求参数code
     **/
    @ApiModelProperty(value = "请求参数code", required = true, example = "securityGroupId", position = 2)
    private String requestCode;

    /**
     * 请求参数名称
     **/
    @ApiModelProperty(value = "请求参数名称", required = true, example = "安全组ID", position = 3)
    private String requestName;

    /**
     * 请求参数value
     **/
    @ApiModelProperty(value = "请求参数value", required = true, example = "sg-uf6bmji0bwywfstlad8v", position = 4)
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
