package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description ：数据库组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class DbGroupService {

    @Autowired
    private DbGroupMapper groupMapper;

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public DbGroup selectByPrimaryKey(Integer id){
        return groupMapper.selectByPrimaryKey(id);
    }
}
