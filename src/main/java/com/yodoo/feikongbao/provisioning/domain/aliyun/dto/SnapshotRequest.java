package com.yodoo.feikongbao.provisioning.domain.aliyun.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Date 2019/6/13 10:46
 * @Created by houzhen
 */
public class SnapshotRequest extends BaseDto {

    /** 实例Id **/
    private String instanceId;

    /** 自动快照策略Id **/
    private String autoSnapshotPolicyId;

    /** 磁盘Id **/
    private String diskId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAutoSnapshotPolicyId() {
        return autoSnapshotPolicyId;
    }

    public void setAutoSnapshotPolicyId(String autoSnapshotPolicyId) {
        this.autoSnapshotPolicyId = autoSnapshotPolicyId;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
