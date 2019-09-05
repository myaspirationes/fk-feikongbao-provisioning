package com.yodoo.feikongbao.provisioning.domain.aliyun.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Description 扩容系统盘请求参数
 * @Author jinjun_luo
 * @Date 2019/6/12 17:05
 **/
public class DiskDto extends BaseDto {

    /** 实例id **/
    private String instanceId;

    /** 云盘ID */
    private String diskId;

    /**
     * 希望扩容到的云盘容量大小。单位为GiB。取值范围：
     * 普通云盘（cloud）：5~2000
     * 高效云盘（cloud_efficiency）：5~6144
     * SSD云盘（cloud_ssd）：5~6144
     * ESSD云盘（cloud_essd）：5~32768
     * 指定的新云盘容量必须比原云盘容量大。且6 TiB以下的云盘不能扩容到6 TiB以上。
     */
    private Integer newSize;

    /**
     * 扩容云盘的方式。取值范围：
     * offline（默认）：离线扩容，您需要重启实例以完成扩容。
     * online：在线扩容，无需重启实例即可完成扩容。仅支持高效云盘与SSD云盘。
     */
    private String type;

    /** 虚拟机url **/
    private String instanceUrl;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public Integer getNewSize() {
        return newSize;
    }

    public void setNewSize(Integer newSize) {
        this.newSize = newSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }
}
