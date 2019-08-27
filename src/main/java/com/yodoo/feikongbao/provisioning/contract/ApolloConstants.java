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
     * 公司所在 namespace
     **/
    String DB_CONNECTION_NAMESPACE = "application";

    // ################################## mysql 数据库 开始 ##################################

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


    // ################################## mysql 数据库 结束 ##################################

    // ********************************** redis 数据库 开始 **********************************

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

    // ********************************** redis 数据库 结束 **********************************

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& openStack swift 开始 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    /**
     * openstack-用户名
     */
    String OPENSTACK_USER_NAME = "openstack_user_name";

    /**
     * openstack-密码
     */
    String OPENSTACK_USER_PASSWORD = "openstack_user_password";

    /**
     * openstack-地址
     */
    String OPENSTACK_ENDPOINT_URL = "openstack_endpoint_url";

    /**
     * openstack-默认域id
     */
    String OPENSTACK_DOMAIN_ID = "openstack_domain_id";

    /**
     * openstack-默认租户名称
     */
    String OPENSTACK_PROJECT_NAME = "openstack_project_name";

    /**
     * openstack-默认角色
     */
    String OPENSTACK_ROLE_NAME = "openstack_role_name";

    /**
     * openstack-对象存储地址
     */
    String OPENSTACK_SWIFT_URL = "openstack_swift_url";

    /**
     * 最大使用范围（单位M，1M=1024B）
     */
    String OPENSTACK_DEFAULT_STORAGE_MAXSIZE = "openstack_default_storage_maxSize";

    /**
     * 用户租户名称
     */
    String OPENSTACK_CONSUMER_PROJECT_NAME = "openstack_consumer_project_name";

    // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& openStack swift 开始 结束 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ rabbit mq 开始 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    /** 普通用户名 **/
    String RABBITMQ_GENERAL_USERNAME = "rabbitmq_general_username";

    /** 普通用户密码 **/
    String RABBITMQ_GENERAL_PASSWORD = "rabbitmq_general_password";

    /**  地址 **/
    String RABBITMQ_URL_HOST = "rabbitmq_url_host";

    /** 服务端口号 5672 **/
    String RABBITMQ_URL_SERVICE_PORT = "rabbitmq_url_service_port";


    /** rabbitmq 消息发送到交换机确认机制，是否确认回调: true 回调，false不回调 **/
    String RABBITMQ_PUBLISHER_CONFIRMS = "rabbitmq_publisher_confirms";

    /** rabbitmq 消息发送到队列确认机制，是否确认返回回调 **/
    String RABBITMQ_PUBLISHER_RETURNS = "rabbitmq_publisher_returns";

    /** rabbitmq发送消息时设置强制标志,设置为true时return callback才生效 **/
    String RABBITMQ_TEMPLATE_MANDATORY = "rabbitmq_template_mandatory";

    /** 等待答复时间 */
    String RABBITMQ_REPLY_TIMEOUT = "rabbitmq_reply_timeout";

    /** 发送失败重发次数 **/
    String RABBITMQ_CONFIRM_RETRY_COUNT = "rabbitmq_confirm_retry_count";

    /** feikongbao vhost name **/
    String RABBITMQ_VHOST = "rabbitmq_vhost";

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ rabbit mq 结束 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! neo4j 配置开始 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    /**
     * 连接地址
     */
    String NEO4J_URL = "neo4j_url";

    /**
     * 用户名
     */
    String NEO4J_USERNAME = "neo4j_username";

    /**
     * 密码
     */
    String NEO4J_PASSWORD = "neo4j_password";

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! neo4j 配置开始 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

}
