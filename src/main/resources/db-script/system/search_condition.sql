
CREATE TABLE `search_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `condition_name` varchar(150) NOT NULL DEFAULT '' COMMENT '条件名称',
  `condition_code` varchar(50) NOT NULL DEFAULT '' COMMENT '条件code',
  `condition_value` varchar(50) NOT NULL DEFAULT '' COMMENT '条件code',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='查询条件';
