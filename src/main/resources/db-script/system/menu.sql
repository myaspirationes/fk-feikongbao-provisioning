
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父类id',
  `menu_code` varchar(150) NOT NULL DEFAULT '' COMMENT '单code',
  `menu_name` varchar(150) NOT NULL DEFAULT '' COMMENT '菜单名称',
  `menu_target` varchar(150) NOT NULL DEFAULT '' COMMENT '菜单目标',
  `order_no` varchar(50) NOT NULL DEFAULT '' COMMENT '序号',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';