package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class CompanyCreateProcessDto  extends BaseDto {

    /** 公司id **/
    private Integer companyId;

    /** 过程code **/
    private String processCode;

    /** 过程序号 **/
    private Integer processOrder;

    public Integer getCompanyId() {
        return companyId;
    }

    public CompanyCreateProcessDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getProcessCode() {
        return processCode;
    }

    public CompanyCreateProcessDto setProcessCode(String processCode) {
        this.processCode = processCode;
        return this;
    }

    public Integer getProcessOrder() {
        return processOrder;
    }

    public CompanyCreateProcessDto setProcessOrder(Integer processOrder) {
        this.processOrder = processOrder;
        return this;
    }
}
