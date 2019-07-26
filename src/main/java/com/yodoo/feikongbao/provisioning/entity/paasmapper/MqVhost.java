package com.yodoo.feikongbao.provisioning.entity.paasmapper;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class MqVhost extends BaseEntity {

    private String vhostName;

    private String ip;

    private Integer port;

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