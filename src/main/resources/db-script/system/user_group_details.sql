

CREATE TABLE `user_group_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_group_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户组id',
  `user_id` int(11) NOT NULL DEFAULT 0 COMMENT '用户id',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组关系明细表';

insert into `user_group_details`(`id`,`user_group_id`,`user_id`,`create_user`,`update_user`) value (1,1,1,'admin','admin');
insert into `user_group_details`(`id`,`user_group_id`,`user_id`,`create_user`,`update_user`) value (2,2,2,'admin','admin');
insert into `user_group_details`(`id`,`user_group_id`,`user_id`,`create_user`,`update_user`) value (3,3,1,'admin','admin');