package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbSchema;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbInstanceMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：db实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class DbInstanceService {

    @Autowired
    private DbInstanceMapper dbInstanceMapper;

    @Autowired
    private DbSchemaService dbSchemaService;

    /**
     * 通过id查询
     * @param id
     * @return
     */
    public DbInstance selectByPrimaryKey(Integer id) {
        return dbInstanceMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    public DbInstanceDto selectDbInstanceByPrimaryKey(Integer id){
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 条件分页查询
     * @param dbInstanceDto
     * @return
     */
    public PageInfoDto<DbInstanceDto> queryDbInstanceList(DbInstanceDto dbInstanceDto) {
        // 设置查询条件
        Example example = new Example(DbInstance.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(dbInstanceDto.getIp())){
            criteria.andLike("id", "%" + dbInstanceDto.getIp() + "%");
        }
        Page<?> pages = PageHelper.startPage(dbInstanceDto.getPageNum(), dbInstanceDto.getPageSize());
        List<DbInstance> groupList = dbInstanceMapper.selectByExample(example);
        List<DbInstanceDto> dbGroupDtoList = copyProperties(groupList);
        return new PageInfoDto<DbInstanceDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), dbGroupDtoList);
    }

    /**
     * 添加
     * @param dbInstanceDto
     */
    public Integer addDbInstance(DbInstanceDto dbInstanceDto) {
        addDbInstanceParameterCheck(dbInstanceDto);
        DbInstance dbInstance = new DbInstance();
        BeanUtils.copyProperties(dbInstanceDto, dbInstance);
        return dbInstanceMapper.insertSelective(dbInstance);
    }

    /**
     * 修改
     * @param dbInstanceDto
     */
    public Integer editDbInstance(DbInstanceDto dbInstanceDto) {
        DbInstance dbInstance = editDbInstanceParameterCheck(dbInstanceDto);
        BeanUtils.copyProperties(dbInstanceDto,dbInstance);
        return dbInstanceMapper.updateByPrimaryKeySelective(dbInstance);
    }

    /**
     * 删除
     * @param id
     */
    public Integer deleteDbInstance(Integer id) {
        deleteDbInstanceParameterCheck(id);
        return dbInstanceMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除参数校验
     * @param id
     */
    private void deleteDbInstanceParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DbInstance dbInstance = selectByPrimaryKey(id);
        if (dbInstance == null){
            throw new ProvisioningException(BundleKey.DB_INSTANCE_NOT_EXIST, BundleKey.DB_INSTANCE_NOT_EXIST_MSG);
        }
        List<DbSchema> dbSchemaList = dbSchemaService.getDbSchemaByDbInstanceId(id);
        if (!CollectionUtils.isEmpty(dbSchemaList)){
            throw new ProvisioningException(BundleKey.DB_INSTANCE_IS_USE, BundleKey.DB_INSTANCE_IS_USE_MSG);
        }
    }

    /**
     * 修改参数校验
     * @param dbInstanceDto
     * @return
     */
    private DbInstance editDbInstanceParameterCheck(DbInstanceDto dbInstanceDto) {
        if (dbInstanceDto == null || dbInstanceDto.getTid() == null || dbInstanceDto.getTid() < 0 || StringUtils.isBlank(dbInstanceDto.getIp())
                || dbInstanceDto.getPort() == null || dbInstanceDto.getPort() < 0 || StringUtils.isBlank(dbInstanceDto.getUsername())
                || StringUtils.isBlank(dbInstanceDto.getPassword())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DbInstance dbInstance = selectByPrimaryKey(dbInstanceDto.getTid());
        if (dbInstance == null){
            throw new ProvisioningException(BundleKey.DB_INSTANCE_NOT_EXIST, BundleKey.DB_INSTANCE_NOT_EXIST_MSG);
        }
        Example example = new Example(DbInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", dbInstanceDto.getTid());
        criteria.andEqualTo("ip", dbInstanceDto.getIp());
        criteria.andEqualTo("port", dbInstanceDto.getPort());
        DbInstance dbInstanceNot = dbInstanceMapper.selectOneByExample(example);
        if (dbInstanceNot != null){
            throw new ProvisioningException(BundleKey.DB_INSTANCE_ALREADY_EXIST, BundleKey.DB_INSTANCE_ALREADY_EXIST_MSG);
        }
        return dbInstance;
    }

    /**
     * 添加参数校验
     * @param dbInstanceDto
     */
    private void addDbInstanceParameterCheck(DbInstanceDto dbInstanceDto) {
        if (dbInstanceDto == null || StringUtils.isBlank(dbInstanceDto.getIp()) || dbInstanceDto.getPort() == null || dbInstanceDto.getPort() < 0
                || StringUtils.isBlank(dbInstanceDto.getUsername()) || StringUtils.isBlank(dbInstanceDto.getPassword())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(DbInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ip", dbInstanceDto.getIp());
        criteria.andEqualTo("port", dbInstanceDto.getPort());
        DbInstance dbInstance = dbInstanceMapper.selectOneByExample(example);
        if (dbInstance != null){
            throw new ProvisioningException(BundleKey.DB_INSTANCE_ALREADY_EXIST, BundleKey.DB_INSTANCE_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 复制
     * @param dbInstanceList
     * @return
     */
    private List<DbInstanceDto> copyProperties(List<DbInstance> dbInstanceList){
        if (!CollectionUtils.isEmpty(dbInstanceList)){
            return dbInstanceList.stream()
                    .filter(Objects::nonNull)
                    .map(dbInstance -> {
                        return copyProperties(dbInstance);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param dbInstance
     * @return
     */
    private DbInstanceDto copyProperties(DbInstance dbInstance){
        if (dbInstance != null){
            DbInstanceDto dbInstanceDto = new DbInstanceDto();
            BeanUtils.copyProperties(dbInstance, dbInstanceDto);
            dbInstanceDto.setTid(dbInstance.getId());
            return dbInstanceDto;
        }
        return null;
    }
}
