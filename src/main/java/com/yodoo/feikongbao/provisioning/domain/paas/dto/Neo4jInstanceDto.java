package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class Neo4jInstanceDto extends BaseDto {

    /**
     * neo4j名称
     **/
    private String neo4jName;

    /**
     * IP
     **/
    private String ip;

    /**
     * 端口
     **/
    private Integer port;

    private Integer companyId;

    public String getNeo4jName() {
        return neo4jName;
    }

    public Neo4jInstanceDto setNeo4jName(String neo4jName) {
        this.neo4jName = neo4jName;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Neo4jInstanceDto setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public Neo4jInstanceDto setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Neo4jInstanceDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }
}
