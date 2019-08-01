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
     * 失败
     **/
    String FAIL = PAAS_PREFIX + "FAIL";
    String FAIL_MSG = "请求失败";

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
    String DICTIONARY_ALREADY_EXIST = PAAS_PREFIX + "DICTIONARY.ALREADY.EXIST";
    String DICTIONARY_ALREADY_EXIST_MSG = "字典已存在";

    /**
     * 字典不存在
     **/
    String DICTIONARY_NOT_EXIST = PAAS_PREFIX + "DICTIONARY.NOT.EXIST";
    String DICTIONARY_NOT_EXIST_MSG = "字典不存在";

    /**
     * 数据有在使用
     */
    String THE_DATA_IS_STILL_IN_USE = PAAS_PREFIX + "THE.DATA.IS.STILL.IN.USE";
    String THE_DATA_IS_STILL_IN_USE_MEG = "数据有在使用";

    /**
     * 数数据已存在
     */
    String EXIST = "EXIST";
    String EXIST_MEG = "数据已存在";

    /**
     * 公司已存在
     */
    String COMPANY_ALREADY_EXIST = PAAS_PREFIX + "COMPANY.ALREADY.EXIST";
    String COMPANY_ALREADY_EXIST_MSG = "公司已经存在";

    /**
     * 公司不存在
     **/
    String COMPANY_NOT_EXIST = PAAS_PREFIX + "COMPANY.NOT.EXIST";
    String COMPANY_NOT_EXIST_MSG = "公司不存在";

    /**
     * 集团已存在
     */
    String GROUPS_ALREADY_EXIST = PAAS_PREFIX + "GROUPS.ALREADY.EXIST";
    String GROUPS_ALREADY_EXIST_MSG = "集团已经存在";

    /**
     * 集团不存在
     **/
    String GROUPS_NOT_EXIST = PAAS_PREFIX + "GROUPS.NOT.EXIST";
    String GROUPS_NOT_EXIST_MSG = "集团不存在";

    /**
     * 权限组已存在 PermissionGroup
     */
    String PERMISSION_GROUP_ALREADY_EXIST = PAAS_PREFIX + "PERMISSION.GROUP.ALREADY.EXIST";
    String PERMISSION_GROUP_ALREADY_EXIST_MSG = "权限组已存在";

    /**
     * 权限组不存在
     **/
    String PERMISSION_GROUP_NOT_EXIST = PAAS_PREFIX + "PERMISSION.GROUP.NOT.EXIST";
    String PERMISSION_GROUP_NOT_EXIST_MSG = "权限组不存在";

    /**
     * dbSchema已存在 dbSchema
     */
    String DB_SCHEMA_ALREADY_EXIST = PAAS_PREFIX + "DB.SCHEMA.ALREADY.EXIST";
    String DB_SCHEMA_ALREADY_EXIST_MSG = "dbSchema已存在";

    /**
     * dbSchema不存在
     **/
    String DB_SCHEMA_NOT_EXIST = PAAS_PREFIX + "DB.SCHEMA.NOT.EXIST";
    String DB_SCHEMA_NOT_EXIST_MSG = "dbSchema不存在";

    /**
     * dbGroup 已存在
     */
    String DB_GROUP_ALREADY_EXIST = PAAS_PREFIX + "DB.GROUP.ALREADY.EXIST";
    String DB_GROUP_ALREADY_EXIST_MSG = "dbGroup已存在";

    /**
     * dbGroup 不存在
     **/
    String DB_GROUP_NOT_EXIST = PAAS_PREFIX + "DB.GROUP.NOT.EXIST";
    String DB_GROUP_NOT_EXIST_MSG = "dbGroup不存在";

    /**
     * dbInstance 已存在
     */
    String DB_INSTANCE_ALREADY_EXIST = PAAS_PREFIX + "DB.INSTANCE.ALREADY.EXIST";
    String DB_INSTANCE_ALREADY_EXIST_MSG = "dbInstance 已存在";

    /**
     * dbInstance 不存在
     **/
    String DB_INSTANCE_NOT_EXIST = PAAS_PREFIX + "DB.INSTANCE.NOT.EXIST";
    String DB_INSTANCE_NOT_EXIST_MSG = "dbInstance 不存在";

    /**
     * MqVhost 已存在
     */
    String MQ_VHOST_ALREADY_EXIST = PAAS_PREFIX + "MQ.VHOST.ALREADY.EXIST";
    String MQ_VHOST_ALREADY_EXIST_MSG = "MqVhost 已存在";

    /**
     * MqVhost 不存在
     **/
    String MQ_VHOST_NOT_EXIST = PAAS_PREFIX + "MQ.VHOST.NOT.EXIST";
    String MQ_VHOST_NOT_EXIST_MSG = "MqVhost 不存在";

    /**
     * redisGroup 不存在
     **/
    String REDIS_GROUP_NOT_EXIST = PAAS_PREFIX + "REDIS.GROUP.NOT.EXIST";
    String REDIS_GROUP_NOT_EXIST_MSG = "redisGroup 不存在";

    /**
     * redisGroup 已存在
     */
    String REDIS_GROUP_ALREADY_EXIST = PAAS_PREFIX + "REDIS.GROUP.ALREADY.EXIST";
    String REDIS_GROUP_ALREADY_EXIST_MSG = "redisGroup 已存在";

    /**
     * neo4jInstance 不存在
     **/
    String NEO4J_INSTANCE_NOT_EXIST = PAAS_PREFIX + "NEO4J.INSTANCE.NOT.EXIST";
    String NEO4J_INSTANCE_NOT_EXIST_MSG = "neo4jInstance 不存在";

    /**
     * neo4jInstance 已存在
     **/
    String NEO4J_INSTANCE_ALREADY_EXIST = PAAS_PREFIX + "NEO4J.INSTANCE.ALREADY.EXIST";
    String NEO4J_INSTANCE_ALREADY_EXIST_MSG = "neo4jInstance 已存在";

    /**
     * redisInstance 不存在
     **/
    String REDIS_INSTANCE_NOT_EXIST = PAAS_PREFIX + "REDIS.INSTANCE.NOT.EXIST";
    String REDIS_INSTANCE_NOT_EXIST_MSG = "redisInstance 不存在";

    /**
     * swiftProject 不存在
     **/
    String SWIFT_PROJECT_NOT_EXIST = PAAS_PREFIX + "SWIFT.PROJECT.NOT.EXIST";
    String SWIFT_PROJECT_NOT_EXIST_MSG = "swiftProject 不存在";

    /**
     * swiftProject 已存在
     **/
    String SWIFT_PROJECT_ALREADY_EXIST = PAAS_PREFIX + "SWIFT.PROJECT.ALREADY.EXIST";
    String SWIFT_PROJECT_ALREADY_EXIST_MSG = "swiftProject 已存在";

    /**
     * 功能模块已存在
     */
    String FUNCTION_MODULE_ALREADY_EXIST = PAAS_PREFIX + "FUNCTION.MODULE.ALREADY.EXIST";
    String FUNCTION_MODULE_EXIST_MSG = "功能模块已存在";

    /**
     * 功能模块不存在
     **/
    String FUNCTION_MODULE_NOT_EXIST = PAAS_PREFIX + "FUNCTION.MODULE.NOT.EXIST";
    String FUNCTION_MODULE_NOT_EXIST_MSG = "功能模块不存在";

    /**
     * 初始化数据库异常"
     **/
    String BUILD_SCRIPT_MIGRATION_DATA = PAAS_PREFIX + "BUILD.SCRIPT.MIGRATION.DATA";
    String BUILD_SCRIPT_MIGRATION_DATA_MSG = "初始化数据库异常";

    /**
     * 创建对象存储异常"
     **/
    String SWIFT_PROJECT_ERROR = PAAS_PREFIX + "SWIFT.PROJECT.ERROR";
    String SWIFT_PROJECT_ERROR_MSG = "创建对象存储异常";

    /**
     * 创建 RabbitMQ 的 虚拟机名已存在
     **/
    String RABBITMQ_VHOST_NAME_EXIST_ERROR = PAAS_PREFIX + "RABBITMQ.VHOST.NAME.EXIST";
    String RABBITMQ_VHOST_NAME_EXIST_ERROR_MSG = "创建 RabbitMQ 的 虚拟机名已存在";

    /**
     * 创建 RabbitMQ 的 虚拟机名不存在
     **/
    String RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR = PAAS_PREFIX + "RABBITMQ.VHOST.NAME.NOT.EXIST";
    String RABBITMQ_VHOST_NAME_NOT_EXIST_ERROR_MSG = "创建 RabbitMQ 的 虚拟机名不存在";

    /**
     * RabbitMQ 虚拟机操作错误
     **/
    String RABBITMQ_VHOST_NAME_FAIL_ERROR = PAAS_PREFIX + "RABBITMQ.VHOST.NAME.FAIL";
    String RABBITMQ_VHOST_NAME_FAIL_ERROR_MSG = "RabbitMQ 虚拟机操作错误";

    /**
     * ECS不存在
     **/
    String ECS_NOT_EXIST = PAAS_PREFIX + "ECS.NOT.EXIST";
    String ECS_NOT_EXIST_MSG = "ecs实例不存在";


}
