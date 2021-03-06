package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * redis 实例
 *
 * @Date 2019/7/9 17:28
 * @Author by houzhen
 */
public class RedisInstance extends BaseEntity {

    /**
     * redis组id
     **/
    private Integer redisGroupId;

    /**
     * IP
     **/
    private String ip;

    /**
     * 端口
     **/
    private Integer port;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 密码
     **/
    private String password;

    /**
     * 实例类型，0：主，1：从
     **/
    private Integer type;

    /**
     * 状态
     * 0 未被使用，
     * 1 已被使用
     * 2 运行中：Running
     * 3 已停止：Stopped
     */
    private Integer status;

    public Integer getRedisGroupId() {
        return redisGroupId;
    }

    public void setRedisGroupId(Integer redisGroupId) {
        this.redisGroupId = redisGroupId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}