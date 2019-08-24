

CREATE TABLE `neo4j_instance` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(250) NOT NULL DEFAULT '' COMMENT 'IP',
  `initial_username` varchar(150) NOT NULL DEFAULT '' COMMENT '初始用户名',
  `initial_password` varchar(150) NOT NULL DEFAULT '' COMMENT '初始密码',
  `username` varchar(150) DEFAULT '' COMMENT '用户名',
  `password` varchar(150) DEFAULT '' COMMENT '密码',
  `neo4j_name` varchar(50) DEFAULT '' COMMENT 'neo4j名称',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态：0 未被使用，1 已被使用',
  `created_by` int(20) NOT NULL DEFAULT '0' COMMENT '创建人',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` int(20) NOT NULL DEFAULT '0' COMMENT '最后更改人',
  `last_modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='neo4j实例表';