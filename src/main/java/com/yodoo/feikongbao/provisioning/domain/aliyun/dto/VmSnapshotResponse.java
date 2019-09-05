package com.yodoo.feikongbao.provisioning.domain.aliyun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Date 2019/6/10 19:59
 * @Created by houzhen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VmSnapshotResponse extends BaseDto {

    /** 快照id **/
    private String snapshotId;

    /** 快照名称 **/
    private String snapshotName;

    /** 原磁盘id **/
    private String sourceDiskId;

    /** 源磁盘属性 **/
    private String sourceDiskType;

    /** 源磁盘大小 **/
    private Integer sourceDiskSize;

    /** 描述 **/
    private String description;

    /** 快照完成时间 **/
    private String creationTime;

    /** 状态 **/
    private String status;

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getSourceDiskId() {
        return sourceDiskId;
    }

    public void setSourceDiskId(String sourceDiskId) {
        this.sourceDiskId = sourceDiskId;
    }

    public String getSourceDiskType() {
        return sourceDiskType;
    }

    public void setSourceDiskType(String sourceDiskType) {
        this.sourceDiskType = sourceDiskType;
    }

    public Integer getSourceDiskSize() {
        return sourceDiskSize;
    }

    public void setSourceDiskSize(Integer sourceDiskSize) {
        this.sourceDiskSize = sourceDiskSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
