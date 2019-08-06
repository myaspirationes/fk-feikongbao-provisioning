package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Permission;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @Description ：权限
 * @Author ：jinjun_luo
 * @Date ： 2019/8/6 0006
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 通过id 查询，统计不存在的数量
     * @param permissionIds
     * @return
     */
    public Long selectPermissionNoExistCountByIds(Set<Integer> permissionIds) {
        Long count = null;
        if (!CollectionUtils.isEmpty(permissionIds)){
            count = permissionIds.stream()
                    .filter(Objects::nonNull)
                    .map(id -> {
                        return selectByPrimaryKey(id);
                    })
                    .filter(permission -> permission == null)
                    .count();
        }
        return count;
    }

    /**
     * 通过 id  查询
     * @param id
     * @return
     */
    public Permission selectByPrimaryKey(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }
}
