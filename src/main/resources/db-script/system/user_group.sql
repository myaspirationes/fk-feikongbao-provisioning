
CREATE TABLE `user_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_code` varchar(50) NOT NULL DEFAULT '' COMMENT '用户组code',
  `group_name` varchar(150) NOT NULL DEFAULT '' COMMENT '用户组名称',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组表';

insert into `user_group`(`id`,`group_code`,`group_name`,`create_user`,`update_user`) value (1,'user-admin-group','用户管理员组','admin','admin');
insert into `user_group`(`id`,`group_code`,`group_name`,`create_user`,`update_user`) value (2,'dev-group','开发组','admin','admin');
insert into `user_group`(`id`,`group_code`,`group_name`,`create_user`,`update_user`) value (3,'test-group','测试组','admin','admin');