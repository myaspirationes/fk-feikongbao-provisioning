package com.yodoo.feikongbao.provisioning.entity.paasmapper;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class RedisInstance extends BaseEntity {

    private Integer redisGroupId;

    private String ip;

    private Integer port;

    private String username;

    private String password;

    private Integer type;

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