package com.yodoo.feikongbao.provisioning.domain.paas.dto;

/**
 * @Description ：创建数据库
 * @Author ：jinjun_luo
 * @Date ： 2019/8/27 0027
 */
public class CreateDataBaseDto {

    /** 数据库连接地址 **/
    private String ip;

    /** 端口 **/
    private Integer port;

    /** 用户名 **/
    private String username;

    /** 密码 **/
    private String password;

    /** 数据库名 **/
    private String schemaName;

    public String getIp() {
        return ip;
    }

    public CreateDataBaseDto setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public CreateDataBaseDto setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CreateDataBaseDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CreateDataBaseDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public CreateDataBaseDto setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }
}
