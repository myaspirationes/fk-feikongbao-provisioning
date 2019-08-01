package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
public class DbInstanceDto extends BaseDto {

    /** IP **/
    private String ip;

    /** 端口 **/
    private Integer port;

    /** 用户名 **/
    private String username;

    /** 密码 **/
    private String password;

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

}
