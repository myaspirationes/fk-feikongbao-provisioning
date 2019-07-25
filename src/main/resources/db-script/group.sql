
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(150) NOT NULL DEFAULT '' COMMENT '集团名称',
  `group_code` varchar(50) NOT NULL DEFAULT '' COMMENT '集团Code',
  `expire_date` date NOT NULL DEFAULT  COMMENT '到期日',
  `update_cycle` varchar(50) NOT NULL DEFAULT '' COMMENT '更新周期',
  `next_update_date` date NOT NULL DEFAULT  COMMENT '下次更新日期',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集团表';