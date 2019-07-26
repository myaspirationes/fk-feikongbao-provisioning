package com.yodoo.feikongbao.provisioning.entity.paasmapper;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class CompanyFunctionModule extends BaseEntity {

    private Integer functionModuleId;

    private Integer companyId;

    private Integer status;

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