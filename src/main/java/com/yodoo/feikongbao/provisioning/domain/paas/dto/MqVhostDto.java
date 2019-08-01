package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public class MqVhostDto extends BaseDto {

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

    /**
     * 公司id
     **/
    private Integer companyId;

    public String getVhostName() {
        return vhostName;
    }

    public MqVhostDto setVhostName(String vhostName) {
        this.vhostName = vhostName;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public MqVhostDto setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public MqVhostDto setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public MqVhostDto setCompanyId(Integer companyId) {
        this.companyId = companyId;
        return this;
    }
}
