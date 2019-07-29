package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 公司创建过程
 */
public class CompanyCreateProcess extends BaseEntity {

    /** 公司id **/
    private Integer companyId;

    /** 过程code **/
    private String processCode;

    /** 过程序号 **/
    private Integer processOrder;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode == null ? null : processCode.trim();
    }

    public Integer getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(Integer processOrder) {
        this.processOrder = processOrder;
    }

}