package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：调用 jenkins 执行动作 或结果
 * @Author ：jinjun_luo
 * @Date ： 2019/7/17 0017
 */
public enum ScriptMigrationDataEnum {

    /**
     * 执行
     */
    ROLL_FORWARD("roll-forward"),
    /**
     * 回滚
     */
    ROLL_BACK("rollback");

    private String action;

    public String getAction() {
        return action;
    }

    ScriptMigrationDataEnum(String action) {
        this.action = action;
    }
}
