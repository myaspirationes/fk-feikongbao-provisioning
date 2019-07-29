package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.entity.UserGroupPermissionDetails;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.UserGroupPermissionDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description ：用户组权限组关系
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserGroupPermissionDetailsService {

    @Autowired
    private UserGroupPermissionDetailsMapper userGroupPermissionDetailsMapper;

    /**
     * 通过权限组查询
     * @param permissionGroupId
     * @return
     */
    public UserGroupPermissionDetails selectUserGroupPermissionDetailsByPermissionGroupId(Integer permissionGroupId) {
        return userGroupPermissionDetailsMapper.selectOne(new UserGroupPermissionDetails(null, permissionGroupId));
    }
}
