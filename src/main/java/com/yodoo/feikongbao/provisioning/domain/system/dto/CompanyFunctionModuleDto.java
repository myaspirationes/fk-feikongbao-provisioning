package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：公司功能模块关系表
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
public class CompanyFunctionModuleDto extends BaseDto {

    /**
     * 功能模块id
     **/
    @ApiModelProperty(value = "功能模块id", required = true, example = "1", position = 1)
    private Integer functionModuleId;

    /**
     * 公司id
     **/
    @ApiModelProperty(value = "公司id", required = true, example = "1", position = 2)
    private Integer companyId;

    /**
     * 状态，0：启用，1：停用
     **/
    @ApiModelProperty(value = "状态，0：启用，1：停用", required = true, example = "0", position = 2)
    private Integer status;

    public Integer getFunctionModuleId() {
        return functionModuleId;
    }

    public CompanyFunctionModuleDto setFunctionModuleId(Integer functionModuleId) {
        this.functionModuleId = functionModuleId;
        return this;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public CompanyFunctionModuleDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public CompanyFunctionModuleDto setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
