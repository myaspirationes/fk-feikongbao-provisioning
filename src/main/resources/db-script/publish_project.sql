
CREATE TABLE `publish_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL DEFAULT 0 COMMENT '公司id',
  `vm_instance_id` int(11) NOT NULL DEFAULT 0 COMMENT 'ecs实例id',
  `project_name` varchar(150) NOT NULL DEFAULT '' COMMENT '项目名称',
  `project_type` varchar(50) NOT NULL DEFAULT '' COMMENT '项目类型',
  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'IP',
  `port` int(8) NOT NULL DEFAULT 0 COMMENT '端口',
  `version` varchar(50) NOT NULL DEFAULT '' COMMENT '版本',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0 运行中：Running，1 已停止：Stopped',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  KEY `vm_instance_id` (`vm_instance_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司部署项目表';