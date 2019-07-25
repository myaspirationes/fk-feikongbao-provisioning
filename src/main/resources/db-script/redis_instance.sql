
CREATE TABLE `redis_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `redis_group_id` int(11) NOT NULL DEFAULT 0 COMMENT 'redis组id',
  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'IP',
  `port` int(8) NOT NULL DEFAULT 0 COMMENT '端口',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `type` int(1) NOT NULL DEFAULT 0 COMMENT '实例类型，0：主，1：从',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0 没状态，1 运行中：Running，2 已停止：Stopped',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='redis实例';