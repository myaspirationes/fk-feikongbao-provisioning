package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：redis 实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
public class RedisInstanceDto extends BaseDto {

    /**
     * redis组id
     **/
    @ApiModelProperty(value = "redis 组 数据库自增 id", required = true, example = "1", position = 1)
    private Integer redisGroupId;

    /**
     * IP
     **/
    @ApiModelProperty(value = "IP", required = true, example = "127.0.0.1", position = 2)
    private String ip;

    /**
     * 端口
     **/
    @ApiModelProperty(value = "端口", required = true, example = "8080", position = 3)
    private Integer port;

    /**
     * 用户名
     **/
    @ApiModelProperty(value = "用户名", required = true, example = "test", position = 4)
    private String username;

    /**
     * 密码
     **/
    @ApiModelProperty(value = "密码", required = true, example = "test", position = 5)
    private String password;

    /**
     * 实例类型，0：主，1：从
     **/
    @ApiModelProperty(value = "实例类型，0：主，1：从", required = true, example = "0", position = 6)
    private Integer type;

    /**
     * 状态，
     * 0 未被使用，
     * 1 已被使用
     * 2 运行中：Running
     * 3 已停止：Stopped
     */
    @ApiModelProperty(value = "状态 0 未被使用, 1 已被使用, 2 运行中：Running, 3 已停止：Stopped", required = false, example = "0", position = 7)
    private Integer status;

    /**
     * 公司id
     **/
    @ApiModelProperty(value = "公司id", required = false, example = "test_group", position = 8)
    private Integer companyId;

    /**
     * 公司Code
     **/
    @ApiModelProperty(value = "公司Code", required = false, example = "test_group", position = 9)
    private String companyCode;

    public Integer getRedisGroupId() {
        return redisGroupId;
    }

    public RedisInstanceDto setRedisGroupId(Integer redisGroupId) {
        this.redisGroupId = redisGroupId;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public RedisInstanceDto setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public RedisInstanceDto setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public RedisInstanceDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisInstanceDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public RedisInstanceDto setType(Integer type) {
        this.type = type;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public RedisInstanceDto setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public RedisInstanceDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public RedisInstanceDto setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
        return this;
    }
}
