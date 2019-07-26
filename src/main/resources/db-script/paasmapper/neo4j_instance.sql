

CREATE TABLE `neo4j_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `neo4j_name` varchar(50) NOT NULL DEFAULT '' COMMENT 'neo4j名称',
  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'IP',
  `port` int(8) NOT NULL DEFAULT 0 COMMENT '端口',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='neo4j实例表';