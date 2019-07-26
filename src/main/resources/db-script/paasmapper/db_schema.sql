
CREATE TABLE `db_schema` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `db_instance_id` int(11) NOT NULL DEFAULT 0 COMMENT 'DB实例id',
  `db_group_id` int(11) NOT NULL DEFAULT 0 COMMENT 'DB数据库组id',
  `schema_name` varchar(50) NOT NULL DEFAULT '' COMMENT '数据库名',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态',
  `type` int(1) NOT NULL DEFAULT 0 COMMENT '类型，0：主，1：从',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  KEY `db_instance_id` (`db_instance_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='DB数据库表';