package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.entity.PermissionGroupDetails;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.PermissionGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description ：权限组明细
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class PermissionGroupDetailsService {

    @Autowired
    private PermissionGroupDetailsMapper permissionGroupDetailsMapper;

    /**
     * 通过权限组id 查询
     * @param permissionGroupId
     * @return
     */
    public PermissionGroupDetails selectPermissionGroupDetailsByPermissionGroupId(Integer permissionGroupId) {
        return permissionGroupDetailsMapper.selectOne(new PermissionGroupDetails(permissionGroupId, null));
    }
}
