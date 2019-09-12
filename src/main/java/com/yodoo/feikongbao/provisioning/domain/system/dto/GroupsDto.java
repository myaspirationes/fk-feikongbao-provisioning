package com.yodoo.feikongbao.provisioning.domain.system.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Description ：集团
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class GroupsDto extends BaseDto {

    /**
     * 集团名称
     **/
    @ApiModelProperty(value = "集团名称", required = true, example = "test_group_name", position = 1)
    private String groupName;

    /**
     * 集团Code
     **/
    @ApiModelProperty(value = "集团Code", required = true, example = "test_group_code", position = 2)
    private String groupCode;

    /**
     * 到期日
     **/
    @ApiModelProperty(value = "到期日", required = true, example = "test_group_expire_date", position = 3)
    private Date expireDate;

    /**
     * 更新周期
     **/
    @ApiModelProperty(value = "更新周期", required = false, example = "test_update_cycle", position = 4)
    private String updateCycle;

    /**
     * 下次更新日期
     **/
    @ApiModelProperty(value = "下次更新日期", required = false, example = "test_next_update_date", position = 5)
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
