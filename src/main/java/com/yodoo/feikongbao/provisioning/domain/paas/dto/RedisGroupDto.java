package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：redis 组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
public class RedisGroupDto extends BaseDto {

    /** 组code **/
    private String groupCode;

    /** 组名称 **/
    private String groupName;

    public String getGroupCode() {
        return groupCode;
    }

    public RedisGroupDto setGroupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public RedisGroupDto setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }
}
