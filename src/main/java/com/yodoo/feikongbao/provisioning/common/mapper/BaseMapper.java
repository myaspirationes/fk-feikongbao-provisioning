package com.yodoo.feikongbao.provisioning.common.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
