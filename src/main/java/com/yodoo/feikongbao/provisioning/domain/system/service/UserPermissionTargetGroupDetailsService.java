package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.entity.UserPermissionTargetGroupDetails;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.UserPermissionTargetGroupDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description ：目标集团
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class UserPermissionTargetGroupDetailsService {

    @Autowired
    private UserPermissionTargetGroupDetailsMapper userPermissionTargetGroupDetailsMapper;

    /**
     * 通过集团 id 查询
     *
     * @param groupId
     * @return
     */
    public Integer selectUserPermissionTargetGroupDetailsCountByGroupId(Integer groupId) {
        UserPermissionTargetGroupDetails userPermissionTargetGroupDetails = new UserPermissionTargetGroupDetails();
        userPermissionTargetGroupDetails.setGroupId(groupId);
        return userPermissionTargetGroupDetailsMapper.selectCount(userPermissionTargetGroupDetails);
    }

    /**
     * 通过用户权限 id 查询
     *
     * @param userPermissionId
     * @return
     */
    public List<UserPermissionTargetGroupDetails> selectUserPermissionTargetGroupDetailsByUserPermissionId(Integer userPermissionId) {
        UserPermissionTargetGroupDetails userPermissionTargetGroupDetails = new UserPermissionTargetGroupDetails();
        userPermissionTargetGroupDetails.setUserPermissionId(userPermissionId);
        return userPermissionTargetGroupDetailsMapper.select(userPermissionTargetGroupDetails);
    }
}
