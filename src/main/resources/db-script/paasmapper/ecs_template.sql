
CREATE TABLE `ecs_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ecs_type` varchar(50) NOT NULL DEFAULT '' COMMENT 'ECS类型',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '模板名称',
  `remark` varchar(150) NOT NULL DEFAULT '' COMMENT '备注',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ecs_type` (`ecs_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ECS模板表';