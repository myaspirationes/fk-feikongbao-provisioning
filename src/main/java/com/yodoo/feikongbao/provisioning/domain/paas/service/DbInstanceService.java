package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbSchema;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbInstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description ：db实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class DbInstanceService {

    @Autowired
    private DbInstanceMapper dbInstanceMapper;

    public DbInstance selectByPrimaryKey(Integer id) {
        return dbInstanceMapper.selectByPrimaryKey(id);
    }
}
