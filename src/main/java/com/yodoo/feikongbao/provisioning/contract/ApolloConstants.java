package com.yodoo.feikongbao.provisioning.contract;

/**
 * @Author houzhen
 * @Date 16:40 2019/6/17
 **/
public interface ApolloConstants {

    /**
     * 默认操作人
     **/
    String OPERATE = "apollo";

    /**
     * 数据库所在namespace
     **/
    String DB_CONNECTION_NAMESPACE = "application";

    /**
     * apollo数据库url key
     **/
    String COMPANY_DB_CONNECTION_URL = "company_db_connection_url";

    /**
     * apollo数据库user key
     **/
    String COMPANY_DB_CONNECTION_USER = "company_db_connection_user";

    /**
     * apollo数据库password key（base64加密）
     **/
    String COMPANY_DB_CONNECTION_PASSWORD = "company_db_connection_password";

    /**
     * 最大连接数
     **/
    String COMPANY_DB_POOL_SIZE_MAX = "company_db_pool_size_max";

    String COMPANY_DB_POOL_SIZE_MAX_VALUE = "10";


    /**
     * 最小连接数
     **/
    String COMPANY_DB_POOL_SIZE_MIN = "company_db_pool_size_min";

    String COMPANY_DB_POOL_SIZE_MIN_VALUE = "5";

    /**
     * Redis namespace
     **/
    String REDIS_NAMESPACE = "platform-configs";

    /**
     * redis 地址 key
     **/
    String REDIS_HOST = "redis_host";

    /**
     * redis 端口 key
     **/
    String REDIS_PORT = "redis_port";

    /**
     * redis 密码 key（base64加密）
     **/
    String REDIS_PASSWORD = "redis_password";
}
