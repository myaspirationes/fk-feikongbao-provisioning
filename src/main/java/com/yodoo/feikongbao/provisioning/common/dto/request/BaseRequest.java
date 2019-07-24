package com.yodoo.feikongbao.provisioning.common.dto.request;

/**
 * @Date 2019/6/25 19:52
 * @Created by houzhen
 */
public class BaseRequest {

    /**
     * 主键
     **/
    private Integer id;

    /**
     * 公司Id
     **/
    private String companyId;

    /**
     * 默认第一页
     **/
    private Integer pageNum;

    /**
     * 默认每页10条记录
     **/
    private Integer pageSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getPageNum() {
        return pageNum == null || pageNum <= 0 ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null || pageSize <= 0 ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
