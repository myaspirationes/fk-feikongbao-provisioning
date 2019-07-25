
CREATE TABLE `mq_vhost` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vhost_name` varchar(50) NOT NULL DEFAULT '' COMMENT 'vhost名称',
  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'IP',
  `port` int(8) NOT NULL DEFAULT 0 COMMENT '端口',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  KEY `vhost_name` (`vhost_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息队列vhost表';