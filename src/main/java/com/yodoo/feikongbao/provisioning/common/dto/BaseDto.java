package com.yodoo.feikongbao.provisioning.common.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * @Date 2019/7/26 13:17
 * @Author by houzhen
 */
public class BaseDto extends ResourceSupport {

    @ApiModelProperty(value = "数据库自增 id", required = false)
    private Integer tid;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "第几页", required = false, hidden = true)
    private int pageNum;

    /**
     * 页面大小
     */
    @ApiModelProperty(value = "多少行", required = false, hidden = true)
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
