package com.yodoo.feikongbao.provisioning.exception;

public interface BundleKey {

    /**
     * provisioning系统前缀
     **/
    String PAAS_PREFIX = "FEIKONGBAO.PROVISIONING.";

    /**
     * 成功
     **/
    String SUCCESS = PAAS_PREFIX + "SUCCESS";
    String SUCCESS_MSG = "请求成功";

    /**
     * 未定义、未捕获、未处理的异常
     **/
    String UNDEFINED = PAAS_PREFIX + "UNDEFINED";
    String UNDEFINED_MSG = "服务异常,请联系管理员";

    /**
     * 参数异常
     **/
    String PARAMS_ERROR = PAAS_PREFIX + "PARAMS.ERROR";
    String PARAMS_ERROR_MSG = "参数异常";

    /**
     * 集群不存在
     **/
    String CLUSTER_NOT_EXIST = PAAS_PREFIX + "CLUSTER.NOT.EXIST";
    String CLUSTER_NOT_EXIST_MSG = "集群不存在";


    /**
     * 数据库实例不存在
     **/
    String DB_NOT_EXIST = PAAS_PREFIX + "DB.NOT.EXIST";
    String DB_NOT_EXIST_MSG = "数据库不存在";

    /**
     * redis不存在
     **/
    String REDIS_NOT_EXIST = PAAS_PREFIX + "REDIS.NOT.EXIST";
    String REDIS_NOT_EXIST_MSG = "redis不存在";

    /**
     * 字典已存在
     **/
    String DICTIONARY_EXIST = PAAS_PREFIX + "DICTIONARY.EXIST";
    String DICTIONARY_EXIST_MSG = "字典已存在";

    /**
     * 数据不存在
     */
    String ON_EXIST = "ON.EXIST";
    String ON_EXIST_MEG = "数据不存在";

    /**
     * 数数据已存在
     */
    String EXIST = "EXIST";
    String EXIST_MEG = "数据已存在";

    /**
     * 公司不存在
     **/
    String COMPANY_NOT_EXIST = PAAS_PREFIX + "COMPANY.NOT.EXIST";
    String COMPANY_NOT_EXIST_MSG = "公司不存在";

}
