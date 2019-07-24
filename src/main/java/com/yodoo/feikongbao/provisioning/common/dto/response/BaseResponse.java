package com.yodoo.feikongbao.provisioning.common.dto.response;

import org.springframework.hateoas.ResourceSupport;

/**
 * @Date 2019/6/11 9:22
 * @Created by houzhen
 */
public class BaseResponse extends ResourceSupport {
    /**
     * 主键id
     **/
    private Integer tid;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }
}
