

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父id',
  `account` varchar(150) NOT NULL DEFAULT '' COMMENT '账号',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '用户名称',
  `password` varchar(150) NOT NULL DEFAULT '' COMMENT '密码',
  `email` varchar(150) NOT NULL DEFAULT '' COMMENT '邮箱',
  `region` varchar(150) DEFAULT '' COMMENT '区域',
  `post` varchar(150) DEFAULT '' COMMENT '岗位',
  `sex` int(1) NOT NULL DEFAULT 0 COMMENT '性别：0 没指定性别，1 男， 2 女',
  `birthday` date COMMENT '出生日期',
  `phone` varchar(50) DEFAULT '' COMMENT '电话',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态，0：启用 1：停用',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

insert into `user`(`id`,`parent_id`,`account`,`name`,`password`,`email`,`region`,`post`,`sex`,`birthday`,`phone`,`status`,`create_user`,`update_user`) value (1,1,'admin','管理员','21232F297A57A5A743894A0E4A801FC3','test@126.com','Shanghai','运维工程师',1,'2000-01-01','13803838438',0,'admin','admin');
insert into `user`(`id`,`parent_id`,`account`,`name`,`password`,`email`,`region`,`post`,`sex`,`birthday`,`phone`,`status`,`create_user`,`update_user`) value (2,1,'test','测试用户','098F6BCD4621D373CADE4E832627B4F6','test@126.com','Shanghai','运维工程师',1,'2000-01-01','13803838438',0,'admin','admin');
