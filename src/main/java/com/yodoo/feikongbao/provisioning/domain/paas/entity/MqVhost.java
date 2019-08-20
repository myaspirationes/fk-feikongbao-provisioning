package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * 消息队列 vhost 表
 * @Date 2019/6/10 20:03
 * @Author jinjun_luo
 */
public class MqVhost extends BaseEntity {

    /**
     * vhost 名称
     **/
    private String vhostName;

    /**
     * IP
     **/
    private String ip;

    /**
     * 端口
     **/
    private Integer port;

    public MqVhost() {
    }

    public MqVhost(String vhostName) {
        this.vhostName = vhostName;
    }

    public String getVhostName() {
        return vhostName;
    }

    public void setVhostName(String vhostName) {
        this.vhostName = vhostName == null ? null : vhostName.trim();
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

}