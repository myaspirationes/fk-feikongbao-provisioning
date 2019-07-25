

CREATE TABLE `user_permission_target_group_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_permission_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户权限组id',
  `company_id` int(11) NOT NULL DEFAULT 0 COMMENT '集团id',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户管理公司权限表';