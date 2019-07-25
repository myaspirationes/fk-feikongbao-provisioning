
CREATE TABLE `db_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_code` varchar(50) NOT NULL DEFAULT '' COMMENT '组code',
  `group_name` varchar(150) NOT NULL DEFAULT '' COMMENT '组名称',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='DB数据库组';