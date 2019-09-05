package com.yodoo.feikongbao.provisioning.domain.aliyun.dto;

import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;

import java.util.List;

/**
 * @Description 使用率查询请求
 * @Author jinjun_luo
 * @Date 2019/6/26 9:34
 **/
public class EcsRateDto extends BaseDto {

    /** 虚拟机类型：用于java 实例、redis实例还是mysql实例等 **/
    private String ecsType;

    /** 用于传多个实例 id **/
    private List<String> instanceIds;

    /** 监控项名称 **/
    private String metricName;

    /** 命名空间，表明监控数据所属产品，例如 “acs_ecs_dashboard”,“acs_rds_dashboard”等 **/
    private String namespace;

    /** 序字段:和阿里确认，数据倒序排序 **/
    private String orderby;

    /** 开始时间，可以传入距离1970年1月1日0点的毫秒数，也可以传入format时间格式数据，如2015-10-20 00:00:00 **/
    private String startTime;

    /** 结束时间，可以传入距离1970年1月1日 0点的毫秒数，也可以传入format时间格式数据，如2015-10-20 00:00:00 **/
    private String endTime;

    /** 用于设置统计周期 秒 **/
    private String period;

    /** 实例Id ：在业务中用于传参 **/
    private String instanceId;

    /** 状态码 **/
    private String code;

    private List<Datapoints> datapoints;

    public String getEcsType() {
        return ecsType;
    }

    public void setEcsType(String ecsType) {
        this.ecsType = ecsType;
    }

    public List<String> getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(List<String> instanceIds) {
        this.instanceIds = instanceIds;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getCode() {
        return code;
    }

    public EcsRateDto setCode(String code) {
        this.code = code;
        return this;
    }

    public List<Datapoints> getDatapoints() {
        return datapoints;
    }

    public EcsRateDto setDatapoints(List<Datapoints> datapoints) {
        this.datapoints = datapoints;
        return this;
    }
}
