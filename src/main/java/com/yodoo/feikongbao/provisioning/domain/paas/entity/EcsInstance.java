package com.yodoo.feikongbao.provisioning.domain.paas.entity;

import com.yodoo.feikongbao.provisioning.common.entity.BaseEntity;

import java.util.Date;

/**
 * ECS 实例表
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public class EcsInstance extends BaseEntity {

    /**
     * ecs 实例类型
     **/
    private String ecsType;

    /**
     * 实例id
     **/
    private String instanceId;

    /**
     * 地域ID
     **/
    private String regionId;

    /**
     * 镜像ID
     **/
    private String imageId;

    /**
     * 安全组ID集合
     **/
    private String securityGroupIds;

    /**
     * 到期日
     **/
    private Date expireDate;

    /**
     * 虚拟交换机ID
     **/
    private String vSwitchId;

    /**
     * 实例的资源规格
     **/
    private String instanceType;

    /**
     * 公网出带宽最大值
     **/
    private Integer internetMaxBandwidthIn;

    /**
     * 公网入带宽最大值
     **/
    private Integer internetMaxBandwidthOut;

    /**
     * 实例名称
     **/
    private String instanceName;

    /**
     * 云服务器的主机名
     **/
    private String hostName;

    /**
     * 内网IP
     **/
    private String innerIp;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 密码
     **/
    private String password;

    /**
     * ssh登陆端口
     **/
    private Integer port;

    /**
     * 实例的描述
     **/
    private String description;

    /**
     * 系统盘大小
     **/
    private String systemDiskSize;

    /**
     * 系统盘的磁盘种类
     **/
    private String systemDiskCategory;

    /**
     * 自动释放时间（按量）
     **/
    private String autoReleaseTime;

    /**
     * 网络计费类型，PayByBandwidth：按固定带宽计费，PayByTraffic（默认）：按使用流量计费
     **/
    private String internetChargeType;

    /**
     * 购买资源的时长
     **/
    private Integer period;

    /**
     * 是否要自动续费，0：不自动续费 1：自动续费
     **/
    private Integer autoRenew;

    /**
     * 实例的付费方式，PrePaid：预付费，包年包月，PostPaid（默认）：按量付费
     **/
    private String instanceChargeType;

    /**
     * 状态，0什么都没做，
     * 1 准备中：Pending，
     * 2 启动中：Starting，
     * 3 运行中：Running，
     * 4 停止中：Stopping，
     * 5 已停止：Stopped，
     * 6 释放：deleted
     */
    private Integer status;

    /**
     * 购买资源的时长，Week Month
     **/
    private String periodUnit;

    public String getEcsType() {
        return ecsType;
    }

    public void setEcsType(String ecsType) {
        this.ecsType = ecsType == null ? null : ecsType.trim();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId == null ? null : imageId.trim();
    }

    public String getSecurityGroupIds() {
        return securityGroupIds;
    }

    public void setSecurityGroupIds(String securityGroupIds) {
        this.securityGroupIds = securityGroupIds == null ? null : securityGroupIds.trim();
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId == null ? null : vSwitchId.trim();
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType == null ? null : instanceType.trim();
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
        this.instanceName = instanceName == null ? null : instanceName.trim();
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName == null ? null : hostName.trim();
    }

    public String getInnerIp() {
        return innerIp;
    }

    public void setInnerIp(String innerIp) {
        this.innerIp = innerIp == null ? null : innerIp.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
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
        this.description = description == null ? null : description.trim();
    }

    public String getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(String systemDiskSize) {
        this.systemDiskSize = systemDiskSize == null ? null : systemDiskSize.trim();
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory == null ? null : systemDiskCategory.trim();
    }

    public String getAutoReleaseTime() {
        return autoReleaseTime;
    }

    public void setAutoReleaseTime(String autoReleaseTime) {
        this.autoReleaseTime = autoReleaseTime == null ? null : autoReleaseTime.trim();
    }

    public String getInternetChargeType() {
        return internetChargeType;
    }

    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType == null ? null : internetChargeType.trim();
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

    public String getInstanceChargeType() {
        return instanceChargeType;
    }

    public void setInstanceChargeType(String instanceChargeType) {
        this.instanceChargeType = instanceChargeType == null ? null : instanceChargeType.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit == null ? null : periodUnit.trim();
    }

}