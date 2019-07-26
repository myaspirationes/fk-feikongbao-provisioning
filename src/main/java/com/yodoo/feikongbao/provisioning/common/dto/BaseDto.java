package com.yodoo.feikongbao.provisioning.common.dto;

import org.springframework.hateoas.ResourceSupport;

/**
 * @Date 2019/7/26 13:17
 * @Created by houzhen
 */
public class BaseDto extends ResourceSupport {

    private Integer tid;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
}
