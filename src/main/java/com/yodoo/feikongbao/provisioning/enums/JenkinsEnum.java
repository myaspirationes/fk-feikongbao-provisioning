package com.yodoo.feikongbao.provisioning.enums;

/**
 * @Description ：调用 jenkins 执行动作 或结果
 * @Author ：jinjun_luo
 * @Date ： 2019/7/17 0017
 */
public enum JenkinsEnum {

    /** 执行 */
    ROLL_FORWARD("roll-forward"),
    /** 回滚 */
    ROLL_BACK("rollback"),
    /** 成功 */
    SUCCESS("SUCCESS"),
    /** 失败 **/
    FAILURE("FAILURE");

    private String action;

    public String getAction() {
        return action;
    }

    private JenkinsEnum(String action){
        this.action = action;
    }
}
