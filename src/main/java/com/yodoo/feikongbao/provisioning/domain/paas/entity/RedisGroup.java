package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * redis 实例组表
 *
 * @Date 2019/7/9 17:28
 * @Author by houzhen
 */
public class RedisGroup extends BaseEntity {

    /**
     * 组code
     **/
    private String groupCode;

    /**
     * 组名称
     **/
    private String groupName;

    public RedisGroup() {
    }

    public RedisGroup(String groupCode, String groupName) {
        this.groupCode = groupCode;
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

}