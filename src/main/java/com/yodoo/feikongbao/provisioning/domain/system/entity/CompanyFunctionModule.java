package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 公司功能模块关系表
 */
public class CompanyFunctionModule extends BaseEntity {

    /**
     * 功能模块id
     **/
    private Integer functionModuleId;

    /**
     * 公司id
     **/
    private Integer companyId;

    /**
     * 状态，0：启用，1：停用
     **/
    private Integer status;

    public CompanyFunctionModule() {
    }

    public CompanyFunctionModule(Integer functionModuleId, Integer companyId, Integer status) {
        this.functionModuleId = functionModuleId;
        this.companyId = companyId;
        this.status = status;
    }

    public CompanyFunctionModule(Integer functionModuleId, Integer companyId) {
        this.functionModuleId = functionModuleId;
        this.companyId = companyId;
    }

    public Integer getFunctionModuleId() {
        return functionModuleId;
    }

    public void setFunctionModuleId(Integer functionModuleId) {
        this.functionModuleId = functionModuleId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}