package com.yodoo.feikongbao.provisioning.domain.system.mapper;

import com.yodoo.feikongbao.provisioning.common.mapper.BaseMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    public List<Permission> getPermissionByUserId(@Param("userId") Integer userId);

}