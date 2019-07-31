package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：公司创建步骤
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public enum CompanyCreationStepsEnum {
    COMPANY_STEP(1, "company"),
    DATABASE_STEP(2, "database"),
    REDIS_STEP(3, "redis"),
    SWIFT_STEP(4, "swift"),
    RABBITMQ_STEP(5, "rabbitmq"),
    NEO4J_STEP(6, "neo4j"),
    PUBLISH_STEP(7, "publish"),
    SUPERUSER_STEP(8, "superuser");

    private Integer order;

    private String code;

    public Integer getOrder() {
        return order;
    }

    public String getCode() {
        return code;
    }

    private CompanyCreationStepsEnum(Integer order, String code) {
        this.order = order;
        this.code = code;
    }
}
