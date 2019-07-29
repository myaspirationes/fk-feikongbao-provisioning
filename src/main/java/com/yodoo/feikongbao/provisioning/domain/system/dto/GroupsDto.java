package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

import java.util.Date;

/**
 * @Description ：集团
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class GroupsDto extends BaseDto {

    /** 集团名称 **/
    private String groupName;

    /** 集团Code **/
    private String groupCode;

    /** 到期日 **/
    private Date expireDate;

    /** 更新周期 **/
    private String updateCycle;

    /** 下次更新日期 **/
    private Date nextUpdateDate;

    public String getGroupName() {
        return groupName;
    }

    public GroupsDto setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public GroupsDto setGroupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public GroupsDto setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
        return this;
    }

    public String getUpdateCycle() {
        return updateCycle;
    }

    public GroupsDto setUpdateCycle(String updateCycle) {
        this.updateCycle = updateCycle;
        return this;
    }

    public Date getNextUpdateDate() {
        return nextUpdateDate;
    }

    public GroupsDto setNextUpdateDate(Date nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
        return this;
    }
}
