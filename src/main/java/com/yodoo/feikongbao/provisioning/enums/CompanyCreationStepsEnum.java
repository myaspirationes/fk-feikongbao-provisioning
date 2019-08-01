package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：公司创建步骤
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public enum CompanyCreationStepsEnum {
    /**
     * 公司基本信息
     **/
    COMPANY_STEP(1, "company"),
    /**
     * db 实例
     **/
    DATABASE_STEP(2, "database"),
    /**
     * 缓存
     **/
    REDIS_STEP(3, "redis"),
    /**
     * 对象存储
     **/
    SWIFT_STEP(4, "swift"),
    /**
     * 消息队列
     **/
    RABBITMQ_STEP(5, "rabbitmq"),
    /**
     * 流程定义
     **/
    NEO4J_STEP(6, "neo4j"),
    /**
     * 实例应用
     **/
    PUBLISH_STEP(7, "publish"),
    /**
     * 超级用户
     **/
    SUPERUSER_STEP(8, "superuser");

    private Integer order;

    private String code;

    public Integer getOrder() {
        return order;
    }

    public String getCode() {
        return code;
    }

    CompanyCreationStepsEnum(Integer order, String code) {
        this.order = order;
        this.code = code;
    }
}
