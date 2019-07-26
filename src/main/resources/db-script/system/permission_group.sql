
CREATE TABLE `permission_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_code` varchar(50) NOT NULL DEFAULT '' COMMENT '权限组code',
  `group_name` varchar(50) NOT NULL DEFAULT '' COMMENT '权限组名称',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限组表';

insert into `permission_group`(`id`,`group_code`,`group_name`,`create_user`,`update_user`) value (1,'customer','客户管理','admin','admin');
insert into `permission_group`(`id`,`group_code`,`group_name`,`create_user`,`update_user`) value (2,'system','系统管理','admin','admin');