package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Description ：redis 组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
public class RedisGroupDto extends BaseDto {

    /**
     * redis 组 code
     **/
    @ApiModelProperty(value = "redis 组 code", required = true, example = "test_group", position = 1)
    private String groupCode;

    /**
     * redis 组名称
     **/
    @ApiModelProperty(value = "redis 组名称", required = true, example = "test_group", position = 2)
    private String groupName;

    /**
     * redis 实例
     **/
    @ApiModelProperty(hidden = true)
    private List<RedisInstanceDto> redisInstanceDtoList;

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

    public List<RedisInstanceDto> getRedisInstanceDtoList() {
        return redisInstanceDtoList;
    }

    public RedisGroupDto setRedisInstanceDtoList(List<RedisInstanceDto> redisInstanceDtoList) {
        this.redisInstanceDtoList = redisInstanceDtoList;
        return this;
    }
}
