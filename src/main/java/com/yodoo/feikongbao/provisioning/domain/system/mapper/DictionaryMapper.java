package com.yodoo.feikongbao.provisioning.domain.system.mapper;

import com.yodoo.feikongbao.provisioning.common.mapper.BaseMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Dictionary;
import org.apache.ibatis.annotations.Param;

/**
 * 字典表
 * @Date 2019/6/10 20:03
 * @Author by houzhen
 */
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    /**
     * 查询除自身以外是否有相同的数据
     *
     * @param id
     * @param type
     * @param code
     * @param name
     * @param value
     * @return
     */
    Dictionary selectDictionaryInAdditionToItself(@Param("id") Integer id, @Param("type") String type, @Param("code") String code, @Param("name") String name, @Param("value") String value);
}