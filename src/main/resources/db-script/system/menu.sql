
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


insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (1,0,'customer','客户管理','',100,'admin','admin');
insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (2,0,'system','系统管理','',200,'admin','admin');

insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (3,1,'group','集团管理','/group',100.01,'admin','admin');
insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (4,1,'company','公司集团','/company',100.02,'admin','admin');

insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (5,2,'user','用户管理','/user',200.01,'admin','admin');
insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (6,2,'permission','权限管理','/permission',200.02,'admin','admin');
insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (7,2,'menu','菜单管理','/menu',200.03,'admin','admin');

insert into `menu`(`id`,`parent_id`,`menu_code`,`menu_name`,`menu_target`,`order_no`,`create_user`,`update_user`) value (8,2,'dictionary','字典管理','/dictionary',200.99,'admin','admin');









