package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

public class Neo4jInstance extends BaseEntity {

    private String neo4jName;

    private String ip;

    private Integer port;

    public String getNeo4jName() {
        return neo4jName;
    }

    public void setNeo4jName(String neo4jName) {
        this.neo4jName = neo4jName == null ? null : neo4jName.trim();
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