
CREATE TABLE `ecs_template_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_id` int(11) NOT NULL DEFAULT 0 COMMENT '模板id',
  `request_code` varchar(50) NOT NULL DEFAULT '' COMMENT '请求参数code',
  `request_name` varchar(50) NOT NULL DEFAULT '' COMMENT '请求参数名称',
  `request_value` varchar(50) NOT NULL DEFAULT '' COMMENT '请求参数value',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  KEY `template_id` (`template_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='ECS模板明细表';

