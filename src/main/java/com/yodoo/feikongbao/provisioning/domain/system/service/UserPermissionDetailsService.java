package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.entity.UserPermissionDetails;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.UserPermissionDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description ：用户权限
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionDetailsService {

    @Autowired
    private UserPermissionDetailsMapper userPermissionDetailsMapper;

    /**
     * 通过用户 id 查询 用户权限列表
     *
     * @param userId
     * @return
     */
    public List<UserPermissionDetails> selectUserPermissionDetailsByUserId(Integer userId) {
        UserPermissionDetails userPermissionDetails = new UserPermissionDetails();
        userPermissionDetails.setUserId(userId);
        return userPermissionDetailsMapper.select(userPermissionDetails);
    }
}
