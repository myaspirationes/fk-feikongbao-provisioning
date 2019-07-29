package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：公司创建步骤
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
public enum CompanyCreationStepsEnum {
    FIRST_STEP(1, "第一步：公司基本信息"),
    SECOND_STEP(2, "第二步：数据库"),
    THREE_STEP(3, "第三步：缓存"),
    FOUR_STEP(4, "第四步：对象存储"),
    FIVE_STEP(5, "第五步：消息队列"),
    SIX_STEP(6, "第六步：流程定义"),
    SEVEN_STEP(7, "第七步：应用实例"),
    EIGHT_STEP(8, "第七步：超级用户");

    private Integer code;

    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private CompanyCreationStepsEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
