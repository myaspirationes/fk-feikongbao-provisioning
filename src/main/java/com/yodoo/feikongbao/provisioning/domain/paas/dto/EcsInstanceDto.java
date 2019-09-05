package com.yodoo.feikongbao.provisioning.domain.paas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

/**
 * @Date 2019/6/10 20:21
 * @Auther by houzhen
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EcsInstanceDto extends BaseDto {

    /** 实例id **/
    private String instanceId;

    /** ecs类型 **/
    private String ecsType;

    /** 地域ID **/
    private String regionId;

    /** 镜像ID **/
    private String imageId;

    /** 安全组ID **/
    private String securityGroupIds;

    /** 虚拟交换机ID **/
    private String vSwitchId;

    /** 实例的资源规格 **/
    private String instanceType;

    /** 公网入带宽最大值 **/
    private Integer internetMaxBandwidthIn;

    /** 公网出带宽最大值 **/
    private Integer internetMaxBandwidthOut;

    /** 实例名称 **/
    private String instanceName;

    /** 私有id地址 **/
    private String privateIpAddress;

    /** 云服务器的主机名 **/
    private String hostName;

    /**  虚拟机用户名 **/
    private String username;

    /**  虚拟机密码 **/
    private String password;

    /**  虚拟机SSH登陆端口 **/
    private Integer port;

    /** 实例的描述 **/
    private String description;

    /** 系统盘id **/
    private String systemDiskId;

    /** 系统盘大小 **/
    private String systemDiskSize;

    /** 系统盘的磁盘种类 **/
    private String systemDiskCategory;

    /** 自动释放时间（按量） **/
    private String autoReleaseTime;

    /** 网络计费类型，PayByBandwidth：按固定带宽计费，PayByTraffic（默认）：按使用流量计费 **/
    private String internetChargeType;

    /** 购买资源的时长 **/
    private Integer period;

    /** 是否要自动续费，0：不自动续费 1：自动续费 **/
    private Integer autoRenew;

    /** 购买资源的时长，Week Month **/
    private String periodUnit;

    /** 实例的付费方式，PrePaid：预付费，包年包月，PostPaid（默认）：按量付费 **/
    private String instanceChargeType;

    /** 状态，准备中：Pending，启动中：Starting，运行中：Running，停止中：Stopping，已停止：Stopped，释放：deleted **/
    private String status;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getEcsType() {
        return ecsType;
    }

    public void setEcsType(String ecsType) {
        this.ecsType = ecsType;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSecurityGroupIds() {
        return securityGroupIds;
    }

    public void setSecurityGroupIds(String securityGroupIds) {
        this.securityGroupIds = securityGroupIds;
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Integer getInternetMaxBandwidthIn() {
        return internetMaxBandwidthIn;
    }

    public void setInternetMaxBandwidthIn(Integer internetMaxBandwidthIn) {
        this.internetMaxBandwidthIn = internetMaxBandwidthIn;
    }

    public Integer getInternetMaxBandwidthOut() {
        return internetMaxBandwidthOut;
    }

    public void setInternetMaxBandwidthOut(Integer internetMaxBandwidthOut) {
        this.internetMaxBandwidthOut = internetMaxBandwidthOut;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemDiskId() {
        return systemDiskId;
    }

    public void setSystemDiskId(String systemDiskId) {
        this.systemDiskId = systemDiskId;
    }

    public String getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(String systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }

    public String getAutoReleaseTime() {
        return autoReleaseTime;
    }

    public void setAutoReleaseTime(String autoReleaseTime) {
        this.autoReleaseTime = autoReleaseTime;
    }

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Integer autoRenew) {
        this.autoRenew = autoRenew;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getInstanceChargeType() {
        return instanceChargeType;
    }

    public void setInstanceChargeType(String instanceChargeType) {
        this.instanceChargeType = instanceChargeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}