
CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL DEFAULT 0 COMMENT '集团id',
  `company_name` varchar(150) NOT NULL DEFAULT '' COMMENT '公司名称',
  `company_code` varchar(50) NOT NULL DEFAULT '' COMMENT '公司Code',
  `update_cycle` varchar(50) NOT NULL DEFAULT '' COMMENT '更新周期',
  `next_update_date` date COMMENT '下次更新日期',
  `expire_date` date  COMMENT '到期日',
  `db_group_id` int(11) NOT NULL DEFAULT 0 COMMENT 'DB数据库组id',
  `redis_group_id` int(11) NOT NULL DEFAULT 0 COMMENT 'redis组id',
  `swift_project_id` int(11) NOT NULL DEFAULT 0 COMMENT 'swift租户id',
  `mq_vhost_id` int(11) NOT NULL DEFAULT 0 COMMENT '消息队列vhost',
  `neo4j_instance_id` int(11) NOT NULL DEFAULT 0 COMMENT 'neo4j实例id',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司表';
