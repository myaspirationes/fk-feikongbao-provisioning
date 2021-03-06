package com.yodoo.feikongbao.provisioning.domain.system.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 公司创建过程
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public class CompanyCreateProcess extends BaseEntity {

    /**
     * 公司id
     **/
    private Integer companyId;

    /**
     * 过程序号
     **/
    private Integer processOrder;

    /**
     * 过程code
     **/
    private String processCode;

    public CompanyCreateProcess() {
    }

    public CompanyCreateProcess(Integer companyId, Integer processOrder, String processCode) {
        this.companyId = companyId;
        this.processOrder = processOrder;
        this.processCode = processCode;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getProcessOrder() {
        return processOrder;
    }

    public void setProcessOrder(Integer processOrder) {
        this.processOrder = processOrder;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode == null ? null : processCode.trim();
    }

}