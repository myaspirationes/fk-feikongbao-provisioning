

CREATE TABLE `swift_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(50) NOT NULL DEFAULT '' COMMENT '租户名称',
  `max_size` decimal(11,2) NOT NULL DEFAULT 0 COMMENT '最大使用范围（单位M，1M=1024B）',
  `user_size` decimal(11,2) NOT NULL DEFAULT 0 COMMENT '所属用户使用范围',
  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'IP',
  `port` int(8) NOT NULL DEFAULT 0 COMMENT '端口',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  KEY `project_name` (`project_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='swift租户表';