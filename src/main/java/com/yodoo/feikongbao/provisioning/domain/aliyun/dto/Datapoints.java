package com.yodoo.feikongbao.provisioning.domain.aliyun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yodoo.feikongbao.provisioning.common.dto.BaseDto;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 使用返回参数
 * @Author jinjun_luo
 * @Date 2019/6/26 16:24
 **/
public class Datapoints extends BaseDto {

    /** 编号 **/
    private Integer order;

    /** 日期毫秒 **/
    private Long timestamp;

    /** 用户id **/
    private String userId;

    /** 实例id **/
    private String instanceId;

    /** 监控项名称 **/
    private String metricName;

    /** 最大值 **/
    @JsonProperty("Maximum")
    private Double maximum;

    /** 最小值 **/
    @JsonProperty("Minimum")
    private Double minimum;

    /** 平均值 **/
    @JsonProperty("Average")
    private Double average;

    /**数量 **/
    @JsonProperty("_count")
    private Integer count;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
