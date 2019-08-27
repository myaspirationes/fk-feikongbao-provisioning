package com.yodoo.feikongbao.provisioning.enums;

import java.util.Objects;

/**
 * @Description ：jenkins job名称
 * @Author ：jinjun_luo
 * @Date ： 2019/8/26 0026
 */
public enum JobNameEnum {

    /**
     * FAT
     */
    FAT("FAT", "fat-md-db-upgrade"),

    /**
     * UAT
     **/
    UAT("UAT", "uat-md-db-upgrade"),

    /**
     * PRO
     **/
    PRO("PRO", "pro-md-db-upgrade"),

    /**
     * default
     */
    DEFAULT("DEFAULT", "dev-md-db-upgrade");

    /**
     * key
     */
    public String key;

    /**
     * value
     */
    public String value;

    JobNameEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private static final EnumFindHelper<JobNameEnum, String> KEY_FIND_HELPER = EnumFindHelper
            .of(JobNameEnum.class, e -> e.key);

    /**
     * 根据编码查找枚举
     *
     * @param key 编码
     * @return 编码对应的枚举
     * @throws IllegalArgumentException 未找到枚举
     */
    public static JobNameEnum getBykey(String key) {

        JobNameEnum r = KEY_FIND_HELPER.find(key, null);
        if (Objects.isNull(r)) {
            r = KEY_FIND_HELPER.find("DEFAULT", null);
        }
        return r;
    }
}
