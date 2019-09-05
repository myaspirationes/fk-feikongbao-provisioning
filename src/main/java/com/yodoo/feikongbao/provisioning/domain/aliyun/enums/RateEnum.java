package com.yodoo.feikongbao.provisioning.domain.aliyun.enums;

/**
 * @Description 使用率查询请求参数
 * @Author jinjun_luo
 * @Date 2019/6/26 9:34
 **/
public enum RateEnum {

    /** 表明监控数据所属产品 ECS **/
    acs_ecs_dashboard("acs_ecs_dashboard"),
    /** 表明监控数据所属产品 RDS **/
    acs_rds_dashboard("acs_rds_dashboard"),
    /* 排序字段*/
    average("Average");

    private String code;

    public String getCode() {
        return code;
    }

    RateEnum(String code){
        this.code = code;
    }
}
