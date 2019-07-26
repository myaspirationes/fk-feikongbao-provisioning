package com.yodoo.feikongbao.provisioning.entity.applymapper;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class UserGroupCondition extends BaseEntity {

    private Integer searchConditionId;

    private Integer userGroupId;

    private String operator;

    private String matchValue;

    public Integer getSearchConditionId() {
        return searchConditionId;
    }

    public void setSearchConditionId(Integer searchConditionId) {
        this.searchConditionId = searchConditionId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(String matchValue) {
        this.matchValue = matchValue == null ? null : matchValue.trim();
    }

}