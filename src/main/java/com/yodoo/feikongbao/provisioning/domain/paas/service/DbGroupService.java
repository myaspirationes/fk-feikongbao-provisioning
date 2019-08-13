package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbSchemaDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description ：数据库组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class DbGroupService {

    @Autowired
    private DbGroupMapper groupMapper;

    @Autowired
    private DbSchemaService dbSchemaService;

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public DbGroup selectByPrimaryKey(Integer id) {
        return groupMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询数据库实例
     *
     * @param dbGroupId
     * @return
     */
    public DbGroupDto selectDbGroupByCompanyId(Integer dbGroupId) {
        DbGroup dbGroup = groupMapper.selectByPrimaryKey(dbGroupId);
        DbGroupDto dbGroupDto = null;
        if (dbGroup != null) {
            dbGroupDto = new DbGroupDto();
            BeanUtils.copyProperties(dbGroup, dbGroupDto);
            dbGroupDto.setTid(dbGroup.getId());
            List<DbSchemaDto> dbSchemaDto = dbSchemaService.selectDbSchemaByDbGroupId(dbGroup.getId());
            if (!CollectionUtils.isEmpty(dbSchemaDto)) {
                dbGroupDto.setDbSchemaDtoList(dbSchemaDto);
            }
        }
        return dbGroupDto;
    }
}
