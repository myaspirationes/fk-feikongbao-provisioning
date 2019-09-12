package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class Neo4jInstanceDto extends BaseDto {

    /**
     * IP
     **/
    @ApiModelProperty(value = "IP", required = true, example = "127.0.0.1", position = 1)
    private String url;

    /**
     * 初始用户名
     */
    @ApiModelProperty(value = "初始用户名", required = true, example = "oldtest", position = 2)
    private String initialUsername;

    /**
     * 初始密码
     */
    @ApiModelProperty(value = "初始密码", required = true, example = "oldtest", position = 3)
    private String initialPassword;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = false, example = "test", position = 4)
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = false, example = "test", position = 5)
    private String password;

    /**
     * neo4j名称
     **/
    @ApiModelProperty(value = "neo4j名称", required = false, example = "test", position = 6)
    private String neo4jName;

    /**
     * 状态：0 未被使用，1 已被使用
     */
    @ApiModelProperty(value = "状态：0 未被使用，1 已被使用", required = false, example = "0", position = 7)
    private Integer status;

    @ApiModelProperty(value = "公司id", required = false, example = "0", position = 8)
    private Integer companyId;

    public String getUrl() {
        return url;
    }

    public Neo4jInstanceDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getInitialUsername() {
        return initialUsername;
    }

    public Neo4jInstanceDto setInitialUsername(String initialUsername) {
        this.initialUsername = initialUsername;
        return this;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public Neo4jInstanceDto setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Neo4jInstanceDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Neo4jInstanceDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNeo4jName() {
        return neo4jName;
    }

    public Neo4jInstanceDto setNeo4jName(String neo4jName) {
        this.neo4jName = neo4jName;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Neo4jInstanceDto setStatus(Integer status) {
        this.status = status;
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
