package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
public class DbInstanceDto extends BaseDto {

    /**
     * IP
     **/
    @ApiModelProperty(value = "db实例的 ip 地址", required = true, example = "127.0.0.1", position = 1)
    private String ip;

    /**
     * 端口
     **/
    @ApiModelProperty(value = "db实例的端口号", required = true, example = "8080", position = 2)
    private Integer port;

    /**
     * 用户名
     **/
    @ApiModelProperty(value = "db实例的用户名", notes = "db实例的用户名", required = true, example = "test", position = 3)
    private String username;

    /**
     * 密码
     **/
    @ApiModelProperty(value = "db实例的密码", required = true, example = "test123", position = 4)
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
