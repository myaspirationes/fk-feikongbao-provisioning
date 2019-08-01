package com.yodoo.feikongbao.provisioning.domain.paas.dto;

/**
 * @Description 用于接收响应体vhostName的List数据，List有很多属性，只需要name这个属性
 * @Author jinjun_luo
 * @Date 2019/5/28 9:53
 **/
public class MqDto {

    /**
     * vhosts 名称
     **/
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
