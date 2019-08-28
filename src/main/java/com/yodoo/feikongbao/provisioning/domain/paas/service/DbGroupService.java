package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbSchemaDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbSchema;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbGroupMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：数据库组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class DbGroupService {

    @Autowired
    private DbGroupMapper dbGroupMapper;

    @Autowired
    private DbSchemaService dbSchemaService;

    @Autowired
    private CompanyService companyService;

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public DbGroup selectByPrimaryKey(Integer id) {
        return dbGroupMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询数据库实例
     *
     * @param dbGroupId
     * @return
     */
    public DbGroupDto selectDbGroupByCompanyId(Integer dbGroupId) {
        DbGroup dbGroup = dbGroupMapper.selectByPrimaryKey(dbGroupId);
        DbGroupDto dbGroupDto = null;
        if (dbGroup != null) {
            dbGroupDto = copyProperties(dbGroup);
            List<DbSchemaDto> dbSchemaDto = dbSchemaService.selectDbSchemaByDbGroupId(dbGroup.getId());
            if (!CollectionUtils.isEmpty(dbSchemaDto)) {
                dbGroupDto.setDbSchemaDtoList(dbSchemaDto);
            }
        }
        return dbGroupDto;
    }

    /**
     * 条件分页查询
     * @param dbGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public PageInfoDto<DbGroupDto> queryDbGroupList(DbGroupDto dbGroupDto) {
        // 设置查询条件
        Example example = new Example(DbGroup.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(dbGroupDto.getGroupCode())){
            criteria.andEqualTo("groupCode", dbGroupDto.getGroupCode());
        }
        if (StringUtils.isNotBlank(dbGroupDto.getGroupName())){
            criteria.andEqualTo("groupName", dbGroupDto.getGroupName());
        }
        Page<?> pages = PageHelper.startPage(dbGroupDto.getPageNum(), dbGroupDto.getPageSize());
        List<DbGroup> groupList = dbGroupMapper.selectByExample(example);
        List<DbGroupDto> dbGroupDtoList = copyProperties(groupList);
        return new PageInfoDto<DbGroupDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), dbGroupDtoList);
    }

    /**
     * 添加
     * @param dbGroupDto
     */
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public Integer addDbGroup(DbGroupDto dbGroupDto) {
        addDbGroupParameterCheck(dbGroupDto);
        return dbGroupMapper.insertSelective(new DbGroup(dbGroupDto.getGroupCode(), dbGroupDto.getGroupName()));
    }

    /**
     * 更新
     * @param dbGroupDto
     * @return
     */
    public Integer editDbGroup(DbGroupDto dbGroupDto) {
       DbGroup dbGroup = editDbGroupParameterCheck(dbGroupDto);
       dbGroup.setGroupCode(dbGroupDto.getGroupCode());
       dbGroup.setGroupName(dbGroupDto.getGroupName());
        return dbGroupMapper.updateByPrimaryKeySelective(dbGroup);
    }

    /**
     * 删除
     * @param id
     */
    public Integer deleteDbGroup(Integer id) {
        deleteDbGroupParameterCheck(id);
        return dbGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除校验
     * @param id
     */
    private void deleteDbGroupParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DbGroup dbGroup = selectByPrimaryKey(id);
        if (dbGroup == null){
            throw new ProvisioningException(BundleKey.DB_GROUP_NOT_EXIST, BundleKey.DB_GROUP_NOT_EXIST_MSG);
        }
        List<DbSchema> dbSchemaList = dbSchemaService.getDbSchemaListByDbGroupId(id);
        if (!CollectionUtils.isEmpty(dbSchemaList)){
            throw new ProvisioningException(BundleKey.DB_GROUP_IS_USE, BundleKey.DB_GROUP_IS_USE_MSG);
        }
        List<Company> companyList = companyService.getCompanyByDbGroupId(id);
        if (!CollectionUtils.isEmpty(companyList)){
            throw new ProvisioningException(BundleKey.DB_GROUP_IS_USE, BundleKey.DB_GROUP_IS_USE_MSG);
        }
    }

    /**
     * 修改参数 校验
     * @param dbGroupDto
     * @return
     */
    private DbGroup editDbGroupParameterCheck(DbGroupDto dbGroupDto) {
        if (dbGroupDto == null || dbGroupDto.getTid() == null || dbGroupDto.getTid() < 0 || StringUtils.isBlank(dbGroupDto.getGroupCode())
                || StringUtils.isBlank(dbGroupDto.getGroupName())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DbGroup dbGroup = selectByPrimaryKey(dbGroupDto.getTid());
        if (dbGroup == null){
            throw new ProvisioningException(BundleKey.DB_GROUP_NOT_EXIST, BundleKey.DB_GROUP_NOT_EXIST_MSG);
        }
        Example example = new Example(DbGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", dbGroupDto.getTid());
        criteria.andEqualTo("groupCode", dbGroupDto.getGroupCode());
        DbGroup dbGroupNot = dbGroupMapper.selectOneByExample(example);
        if (dbGroupNot != null){
            throw new ProvisioningException(BundleKey.DB_GROUP_ALREADY_EXIST, BundleKey.DB_GROUP_ALREADY_EXIST_MSG);
        }
        return dbGroup;
    }

    /**
     * 添加校验
     * @param dbGroupDto
     */
    private void addDbGroupParameterCheck(DbGroupDto dbGroupDto) {
        if (dbGroupDto == null || StringUtils.isBlank(dbGroupDto.getGroupCode()) || StringUtils.isBlank(dbGroupDto.getGroupName())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(DbGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", dbGroupDto.getGroupCode());
        DbGroup dbGroup = dbGroupMapper.selectOneByExample(example);
        if (dbGroup != null){
            throw new ProvisioningException(BundleKey.DB_GROUP_ALREADY_EXIST, BundleKey.DB_GROUP_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 复制
     * @param dbGroupList
     * @return
     */
    private List<DbGroupDto> copyProperties(List<DbGroup> dbGroupList){
        if (!CollectionUtils.isEmpty(dbGroupList)){
            return dbGroupList.stream()
                    .filter(Objects::nonNull)
                    .map(dbGroup -> {
                        return copyProperties(dbGroup);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param dbGroup
     * @return
     */
    private DbGroupDto copyProperties(DbGroup dbGroup){
        if (dbGroup != null){
            DbGroupDto dbGroupDto = new DbGroupDto();
            BeanUtils.copyProperties(dbGroup, dbGroupDto);
            dbGroupDto.setTid(dbGroup.getId());
            return dbGroupDto;
        }
        return null;
    }
}
