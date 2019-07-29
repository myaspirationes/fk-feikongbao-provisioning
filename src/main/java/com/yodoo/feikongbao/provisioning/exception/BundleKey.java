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
     * 请求阿里服务异常
     **/
    String ALI_CALL_ERROR = PAAS_PREFIX + "ALI.CAL.ERROR";
    String ALI_CALL_ERROR_MSG = "阿里服务请求异常";

    /**
     * 虚拟机实例创建异常
     **/
    String VIRTUAL_CREATE_ERROR = PAAS_PREFIX + "VIRTUAL.CREATE.ERROR";
    String VIRTUAL_CREATE_ERROR_MSG = "虚拟机实例创建失败";

    /**
     * 虚拟机实例释放异常
     **/
    String VIRTUAL_DELETE_ERROR = PAAS_PREFIX + "VIRTUAL.DELETE.ERROR";
    String VIRTUAL_DELETE_ERROR_MSG = "虚拟机实例释放失败";

    /**
     * 虚拟机不存在
     **/
    String NO_INSTANCE_ERROR = PAAS_PREFIX + "NO.INSTANCE";
    String NO_INSTANCE_ERROR_MSG = "虚拟机实例不存在";

    /**
     * 系统盘不存在
     **/
    String SYSTEM_DISK_NO_EXIST_ERROR = PAAS_PREFIX + "SYSTEM.DISK.NO.EXIST";
    String SYSTEM_DISK_NO_EXIST_ERROR_MSG = "系统盘不存在";

    /**
     * 扩容系统盘大小超过阿里最大默认 500 G
     **/
    String SYSTEM_DISK_SIZE_ERROR = PAAS_PREFIX + "SYSTEM.DISK.SIZE.ERROR";
    String SYSTEM_DISK_SIZE_ERROR_MSG = "扩容系统盘大小超过阿里最大默认 500 G";

    /**
     * 系统盘扩容失败
     **/
    String SYSTEM_REDIZE_DISK_SIZE_ERROR = PAAS_PREFIX + "SYSTEM.REDIZE.DISK.SIZE.ERROR";
    String SYSTEM_REDIZE_DISK_SIZE_ERROR_MSG = "系统盘扩容失败";

    /**
     * 实例状态无法满足扩容
     **/
    String INSTANCE_STATUS_NO_RUNNING_AND_STOPPED_ERROR = PAAS_PREFIX + "INSTANCE.STATUS.NO.RUNNING.AND.STOPPED";
    String INSTANCE_STATUS_NO_RUNNING_AND_STOPPED_ERROR_MSG = "实例状态无法满足扩容";

    /**
     * 实例内网ip不存在
     **/
    String INSTANCE_PRIVATEIP_NOT_EXIST = PAAS_PREFIX + "INSTANCE.PRIVATEIP.NOT.EXIST";
    String INSTANCE_PRIVATEIP_NOT_EXIST_MSG = "实例内网ip不存在";

    /**
     * 集群已存在
     **/
    String CLUSTER_EXIST = PAAS_PREFIX + "CLUSTER.EXIST";
    String CLUSTER_EXIST_MSG = "集群已存在";

    /**
     * 集群不存在
     **/
    String CLUSTER_NOT_EXIST = PAAS_PREFIX + "CLUSTER.NOT.EXIST";
    String CLUSTER_NOT_EXIST_MSG = "集群不存在";

    /**
     * 命名空间已存在
     **/
    String NAMESPACE_EXIST = PAAS_PREFIX + "NAMESPACE.EXIST";
    String NAMESPACE_EXIST_MSG = "命名空间已存在";

    /**
     * item已存在
     **/
    String ITEM_EXIST = PAAS_PREFIX + "ITEM.EXIST";
    String ITEM_EXIST_MSG = "Item 已存在";

    /**
     * 命名空间不存在
     **/
    String NAMESPACE_NO_EXIST = PAAS_PREFIX + "NAMESPACE.NO.EXIST";
    String NAMESPACE_NO_EXIST_MSG = "命名空间不存在";

    /**
     * 数据库实例不存在
     **/
    String DB_INSTANCE_NO_EXIST = PAAS_PREFIX + "DB.INSTANCE.NO.EXIST";
    String DB_INSTANCE_NO_EXIST_MSG = "数据库实例不存在";

    /**
     * 数据库创建失败
     **/
    String DB_CREATE_ERROR = PAAS_PREFIX + "DB.CREATE.ERROR";
    String DB_CREATE_ERROR_MSG = "数据库创建失败";

    /**
     * redis实例不存在
     **/
    String REDIS_INSTANCE_NO_EXIST = PAAS_PREFIX + "REDIS.INSTANCE.NO.EXIST";
    String REDIS_INSTANCE_NO_EXIST_MSG = "redis实例不存在";

    /**
     * swift租户创建失败
     **/
    String SWIFT_PROJECT_CREATE_ERROR = PAAS_PREFIX + "SWIFT.PROJECT.CREATE.ERROR";
    String SWIFT_PROJECT_CREATE_ERROR_MSG = "swift创建租户失败";

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
     * 没有对应的虚拟机类型的实例
     **/
    String NO_ECS_TYPE_INSTANCE_ERROR = PAAS_PREFIX + "NO.ECS.TYPE.INSTANCE";
    String NO_ECS_TYPE_INSTANCE_ERROR_MSG = "没有对应的虚拟机类型的实例";

    /**
     * 虚拟机指标名称不存在
     **/
    String METRIC_NAME_NO_EXIST_ERROR = PAAS_PREFIX + "METRIC_NAME.NO.EXIST";
    String METRIC_NAME_NO_EXIST_ERROR_MSG = "虚拟机指标名称不存在";

    /**
     * 用户不存在
     **/
    String USER_NOT_EXIST = PAAS_PREFIX + "USER.NOT.EXIST";
    String USER_NOT_EXIST_MSG = "用户不存在";

    /**
     * 用户已存在
     **/
    String USER_EXIST = PAAS_PREFIX + "USER.EXIST";
    String USER_EXIST_MSG = "用户已存在";

    /**
     * jenkins 上的 job 不存在
     **/
    String JENKINS_JOB_NO_EXIST_ERROR = PAAS_PREFIX + "JENKINS.JOB.NO.EXIST";
    String JENKINS_JOB_NO_EXIST_ERROR_MSG = "jenkins上的 job 不存在";

    /**
     * 存在相同的 instance code
     **/
    String INSTANCE_CODE_EXIST_ERROR = PAAS_PREFIX + "INSTANCE.CODE.EXIST";
    String INSTANCE_CODE_EXIST_ERROR_MSG = "instance code 已存在";

    /**
     * 在同一个db instance 下，schema name 已存在
     **/
    String DB_INSTANCE_SCHEMA_NAME_EXIST_ERROR = PAAS_PREFIX + "DB.INSTANCE.SCHEMA.NAME.EXIST";
    String DB_INSTANCE_SCHEMA_NAME__EXIST_ERROR_MSG = "在同一个db instance 下，schema name 已存在";

    /**
     * 角色名已存在
     **/
    String ROLE_NAME_EXIST = PAAS_PREFIX + "ROLE.NAME.EXIST";
    String ROLE_NAME_EXIST_MSG = "角色名称已存在";

    /**
     * 角色不存在
     **/
    String ROLE_NAME_ON_EXIST = PAAS_PREFIX + "ROLE.NAME.ON.EXIST";
    String ROLE_NAME_ON_EXIST_MSG = "角色不存在";

    /**
     * 功能菜单已存在
     **/
    String PERMISSION_EXIST = PAAS_PREFIX + "PERMISSION.EXIST";
    String PERMISSION_EXIST_MSG = "权限菜单已存在";

    /**
     * 功能菜单不存在
     **/
    String PERMISSION_ON_EXIST = PAAS_PREFIX + "PERMISSION.ON.EXIST";
    String PERMISSION_ON_EXIST_MSG = "权限菜单不存在";

    /**
     * 还有子功能菜单
     **/
    String PERMISSION_SUBMENU_ITEMS_EXIST = PAAS_PREFIX + "PERMISSION.SUBMENU.ITEMS.EXIST";
    String PERMISSION_SUBMENU_ITEMS_EXIST_MSG = "还有子权限菜单";

    /**
     * 字典已存在
     **/
    String DICTIONARY_EXIST = PAAS_PREFIX + "DICTIONARY.EXIST";
    String DICTIONARY_EXIST_MSG = "字典已存在";

    /**
     * 字典不存在
     **/
    String DICTIONARY_ON_EXIST = PAAS_PREFIX + "DICTIONARY.ON.EXIST";
    String DICTIONARY_ON_EXIST_MSG = "字典不存在";

    /**
     * 此角色还有用户在使用不能删除
     **/
    String USER_USE_ROLE = PAAS_PREFIX + "USER.USE.ROLE";
    String USER_USE_ROLE_MSG = "此角色还有用户在使用不能删除";

    /**
     * 此角色还有权限在使用不能删除
     **/
    String ROLE_USE_PERMISSION = PAAS_PREFIX + "ROLE.USE.PERMISSION";
    String ROLE_USE_PERMISSION_MSG = "此角色还有权限在使用不能删除";

    /**
     * build jenkins 异常
     **/
    String BUILD_JENKINS_ERROR = PAAS_PREFIX + "BUILD.JENKINS.ERROR";
    String BUILD_JENKINS_ERROR_MEG = "build jenkins 异常";

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

}
