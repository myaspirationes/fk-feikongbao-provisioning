
CREATE TABLE `permission_group_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_group_id` int(11) NOT NULL DEFAULT 0 COMMENT '权限组id',
  `permission_id` int(11) NOT NULL DEFAULT 0 COMMENT '权限id',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限组明细表';

insert into `permission_group_details`(`id`,`permission_group_id`,`permission_id`,`create_user`,`update_user`) value (1,1,1,'admin','admin');
insert into `permission_group_details`(`id`,`permission_group_id`,`permission_id`,`create_user`,`update_user`) value (2,1,2,'admin','admin');
insert into `permission_group_details`(`id`,`permission_group_id`,`permission_id`,`create_user`,`update_user`) value (3,1,3,'admin','admin');
insert into `permission_group_details`(`id`,`permission_group_id`,`permission_id`,`create_user`,`update_user`) value (4,2,4,'admin','admin');
insert into `permission_group_details`(`id`,`permission_group_id`,`permission_id`,`create_user`,`update_user`) value (5,2,5,'admin','admin');
insert into `permission_group_details`(`id`,`permission_group_id`,`permission_id`,`create_user`,`update_user`) value (6,2,6,'admin','admin');