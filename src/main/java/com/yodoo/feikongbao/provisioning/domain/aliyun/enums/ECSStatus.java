package com.yodoo.feikongbao.provisioning.domain.aliyun.enums;

/**
 * @Author houzhen
 * @Date 10:36 2019/5/15
**/
public enum ECSStatus {
    /**
     * 状态，0什么都没做，
     */
    NULL(0, "null"),
    /**
     * 1 准备中
     */
    PENDING(1, "Pending"),
    /**
     * 2 启动中
     */
    STARTING(2, "Starting"),
    /**
     * 3 运行中
     */
    RUNNING(3, "Running"),
    /**
     * 4 停止中
     */
    STOPPING(4, "Stopping"),
    /**
     * 5 已停止
     */
    STOPPED(5, "Stopped"),
    /**
     * 6 释放中
     */
    DELETING(6, "Deleting"),
    /**
     * 7 已释放
     */
    DELETED(7, "Deleted"),
    /**
     * 8 超时
     */
    TIMEOUT(8, "Timeout"),
    /**
     * 恢复中
     */
    RESIZING(9, "Resizing"),
    /**
     * 恢复异常
     */
    RESIZE_EXCEPTION(10, "ResizeException");

    /**
     * 代码
     */
    private Integer code;

    /**
     * 状态
     */
    private String status;

    ECSStatus(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public ECSStatus setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ECSStatus setStatus(String status) {
        this.status = status;
        return this;
    }
}
