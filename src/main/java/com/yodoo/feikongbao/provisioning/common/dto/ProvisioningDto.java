package com.yodoo.feikongbao.provisioning.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author houzhen
 * @Date 2019/5/14 16:40
 **/
@ApiModel(description= "返回响应数据")
public class ProvisioningDto<T> {

    /**
     * 请求状态 -1：未登录 0：成功 1：失败 2：无权限
     */
    @ApiModelProperty(value = "状态 -1：未登录 0：成功 1：失败 2：无权限")
    private String status;

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private String messageBundleKey;

    /**
     * 返回操作备注信息
     */
    @ApiModelProperty(value = "状态消息")
    private String message;

    /***封装数据信息*/
    @ApiModelProperty(value = "响应数据")
    private T data;


    public ProvisioningDto() {

    }

    public ProvisioningDto(String status, String messageBundleKey, String message) {
        this.status = status;
        this.messageBundleKey = messageBundleKey;
        this.message = message;
    }

    public ProvisioningDto(String status, String messageBundleKey, String message, T data) {
        this.status = status;
        this.messageBundleKey = messageBundleKey;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessageBundleKey() {
        return messageBundleKey;
    }

    public void setMessageBundleKey(String messageBundleKey) {
        this.messageBundleKey = messageBundleKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
