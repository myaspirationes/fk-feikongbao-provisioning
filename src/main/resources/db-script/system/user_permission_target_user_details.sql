
CREATE TABLE `user_permission_target_user_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户id',
  `permission_id` int(11) NOT NULL DEFAULT 0 COMMENT '权限id',
  `target_user_id` int(11) NOT NULL DEFAULT 0 COMMENT '目标用户id',
  `created_by` int(20) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` int(20) NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='目标用户表';
