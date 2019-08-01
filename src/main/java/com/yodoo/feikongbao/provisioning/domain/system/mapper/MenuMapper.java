package com.yodoo.feikongbao.provisioning.domain.system.mapper;

import com.yodoo.feikongbao.provisioning.common.mapper.BaseMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 条件查询列表
     *
     * @param permissionIds
     * @return
     */
    List<Menu> getMenuByPermission(@Param("permissionIds") Set<Integer> permissionIds);
}