package com.yodoo.feikongbao.provisioning.domain.aliyun.contract;

/**
 * @Date 2019/6/4 10:06
 * @Created by houzhen
 */
public interface ECSConstants {

    /**
     * 是否只预检此次请求。true：发送检查请求，不会创建实例，也不会产生费用；false：发送正常请求，通过检查后直接创建实例，并直接产生费用
     */
    boolean dryRun = true;

    /**
     * 自动策略快照标志
     */
    String autoSnapshotPolicy = "autoSnapshotPolicy";

    /** 实例账号标志 **/
    String instanceAccount = "account";

    /**  实例端口号 **/
    String instancePort = "port";

    /**  实例类型 **/
    String instanceType = "ecsType";

    /** 示例命令表示为系统盘的第一个分区扩容 **/
    String system_Disk_growpart = "growpart /dev/vda 1";

    /** 示例命令表示为系统盘的/dev/vda1分区扩容文件系统 **/
    String system_Disk_resize2fs = "resize2fs /dev/vda1";

    /** 读取超时时间 毫秒 **/
    Integer readTimeoutMillis = 60000;
}
