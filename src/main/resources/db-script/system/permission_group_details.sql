
CREATE TABLE `permission_group_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_group_id` int(11) NOT NULL DEFAULT 0 COMMENT '权限组id',
  `permission_id` int(11) NOT NULL DEFAULT 0 COMMENT '权限id',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限组明细表';