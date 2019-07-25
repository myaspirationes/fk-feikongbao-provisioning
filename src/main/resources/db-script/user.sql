

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT 0 COMMENT '父id',
  `account` varchar(150) NOT NULL DEFAULT '' COMMENT '账号',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '用户名称',
  `password` varchar(150) NOT NULL DEFAULT '' COMMENT '密码',
  `email` varchar(150) NOT NULL DEFAULT '' COMMENT '邮箱',
  `region` varchar(150) NULL DEFAULT '' COMMENT '区域',
  `post` varchar(150) NULL DEFAULT '' COMMENT '岗位',
  `sex` int(1) NOT NULL DEFAULT 0 COMMENT '性别：0 没指定性别，1 男， 2 女',
  `birthday` date(150) NULL DEFAULT '' COMMENT '出生日期',
  `phone` varchar(50) NULL DEFAULT '' COMMENT '电话',
  `status` int(1) NOT DEFAULT 0 COMMENT '状态，0：启用 1：停用',
  `create_user` varchar(150) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(150) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
