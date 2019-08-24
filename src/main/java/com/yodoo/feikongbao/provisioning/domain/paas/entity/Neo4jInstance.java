package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

/**
 * neo4j 实例表
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public class Neo4jInstance extends BaseEntity {

    /**
     * IP
     **/
    private String url;

    /**
     * 初始用户名
     */
    private String initialUsername;

    /**
     * 初始密码
     */
    private String initialPassword;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * neo4j名称
     **/
    private String neo4jName;

    /**
     * 状态：0 未被使用，1 已被使用
     */
    private Integer status;

    public String getUrl() {
        return url;
    }

    public Neo4jInstance setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getInitialUsername() {
        return initialUsername;
    }

    public Neo4jInstance setInitialUsername(String initialUsername) {
        this.initialUsername = initialUsername;
        return this;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public Neo4jInstance setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Neo4jInstance setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Neo4jInstance setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNeo4jName() {
        return neo4jName;
    }

    public Neo4jInstance setNeo4jName(String neo4jName) {
        this.neo4jName = neo4jName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Neo4jInstance setStatus(Integer status) {
        this.status = status;
        return this;
    }
}