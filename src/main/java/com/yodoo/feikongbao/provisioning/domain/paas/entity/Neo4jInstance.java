package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * neo4j 实例表
 */
public class Neo4jInstance extends BaseEntity {

    /** neo4j名称 **/
    private String neo4jName;

    /** IP **/
    private String ip;

    /** 端口 **/
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