
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_code` varchar(50) NOT NULL DEFAULT '' COMMENT '权限code',
  `permission_name` varchar(150) NOT NULL DEFAULT '' COMMENT '权限名称',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

insert into `permission`(`id`,`permission_code`,`permission_name`,`create_user`,`update_user`) value (1,'group','集团管理','admin','admin');
insert into `permission`(`id`,`permission_code`,`permission_name`,`create_user`,`update_user`) value (2,'company','公司集团','admin','admin');
insert into `permission`(`id`,`permission_code`,`permission_name`,`create_user`,`update_user`) value (3,'user','用户管理','admin','admin');
insert into `permission`(`id`,`permission_code`,`permission_name`,`create_user`,`update_user`) value (4,'permission','权限管理','admin','admin');
insert into `permission`(`id`,`permission_code`,`permission_name`,`create_user`,`update_user`) value (5,'menu','菜单管理','admin','admin');
insert into `permission`(`id`,`permission_code`,`permission_name`,`create_user`,`update_user`) value (6,'dictionary','字典管理','admin','admin');
