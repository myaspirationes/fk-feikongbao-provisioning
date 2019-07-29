package com.yodoo.feikongbao.provisioning.domain.system.mapper;

import com.yodoo.feikongbao.provisioning.common.mapper.BaseMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.PermissionGroup;
import org.apache.ibatis.annotations.Param;

public interface PermissionGroupMapper extends BaseMapper<PermissionGroup> {

    /**
     * 查询除自身以外是否有相同的数据
     * @param id
     * @param groupCode
     * @param groupName
     * @return
     */
    PermissionGroup selectPermissionGroupInAdditionToItself(@Param("id") Integer id, @Param("groupCode") String groupCode, @Param("groupName") String groupName);
}