
CREATE TABLE `company_function_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `function_module_id` int(11) NOT NULL DEFAULT 0 COMMENT '功能模块id',
  `company_id` int(11) NOT NULL DEFAULT 0 COMMENT '公司id',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0：启用，1：停用',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司功能模块关系表';