CREATE TABLE `dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL DEFAULT '' COMMENT '类型',
  `code` varchar(50) NOT NULL DEFAULT '' COMMENT '编码',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '名称',
  `value` varchar(150) NOT NULL DEFAULT '' COMMENT '值',
  `remark` varchar(150) NOT NULL DEFAULT '' COMMENT '备注',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  KEY `type_code_index` (`type`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';