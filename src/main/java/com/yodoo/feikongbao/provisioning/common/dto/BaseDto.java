package com.yodoo.feikongbao.provisioning.common.dto;

import org.springframework.hateoas.ResourceSupport;

/**
 * @Date 2019/7/26 13:17
 * @Author by houzhen
 */
public class BaseDto extends ResourceSupport {

    private Integer tid;

    /**
     * 当前页
     */
    private int pageNum;

    /**
     * 页面大小
     */
    private int pageSize;

    public BaseDto() {
    }

    public BaseDto(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public int getPageNum() {
        return pageNum;
    }

    public BaseDto setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public BaseDto setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
