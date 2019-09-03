package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbSchemaDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbSchema;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbSchemaMapper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.service.ApolloService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.InstanceStatusEnum;
import com.yodoo.feikongbao.provisioning.enums.JobNameEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.JenkinsUtils;
import com.yodoo.megalodon.datasource.config.ProvisioningDataSourceConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description ：数据库信息
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class DbSchemaService {

    @Autowired
    private DbSchemaMapper dbSchemaMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApolloService apolloService;

    @Autowired
    private DbGroupService dbGroupService;

    @Autowired
    private DbInstanceService dbInstanceService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    @Autowired
    private JenkinsUtils jenkinsUtils;

    @Autowired
    private ProvisioningDataSourceConfig provisioningDataSourceConfig;

    /**
     * 条件分页查询
     * @param dbSchemaDto
     * @return
     */
    public PageInfoDto<DbSchemaDto> queryDbSchemaList(DbSchemaDto dbSchemaDto) {
        // 设置查询条件
        Example example = new Example(DbInstance.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(dbSchemaDto.getPageNum(), dbSchemaDto.getPageSize());
        List<DbSchema> dbSchemaList = dbSchemaMapper.selectByExample(example);
        List<DbSchemaDto> dbSchemaDtoList = copyProperties(dbSchemaList);
        return new PageInfoDto<DbSchemaDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), dbSchemaDtoList);
    }

    /**
     * 添加
     * @param dbSchemaDto
     */
    public Integer addDbSchema(DbSchemaDto dbSchemaDto) {
        addDbSchemaParameterCheck(dbSchemaDto);
        DbSchema dbSchema = new DbSchema();
        BeanUtils.copyProperties(dbSchemaDto, dbSchema);
        dbSchema.setStatus(InstanceStatusEnum.UNUSED.getCode());
        return dbSchemaMapper.insertSelective(dbSchema);
    }

    /**
     * 更新
     * @param dbSchemaDto
     */
    public Integer editDbSchema(DbSchemaDto dbSchemaDto) {
        DbSchema dbSchema = editDbSchemaParameterCheck(dbSchemaDto);
        BeanUtils.copyProperties(dbSchemaDto, dbSchema);
        return dbSchemaMapper.insertSelective(dbSchema);
    }

    /**
     * 删除 TODO 一台dbInstance实例和dbSchema是否是一对一关系
     * @param id
     */
    public Integer deleteDbSchema(Integer id) {
        DbSchema dbSchema = deleteDbSchemaParameterCheck(id);
        return dbSchemaMapper.deleteByPrimaryKey(id);
    }

    /**
     * 创建数据库 TODO 目前测试先注掉初始化数据库表，最后测试记得放开，返回上一步，
     *
     * @param dbSchemaDto
     * @return
     */
    public DbSchemaDto useDbSchema(DbSchemaDto dbSchemaDto) {
        // 参数校验
        List<DbSchema> dbSchemaList = useDbSchemaParameterCheck(dbSchemaDto);

        // 更新公司表信息
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(dbSchemaDto.getCompanyId());
        companyDto.setDbGroupId(dbSchemaDto.getDbGroupId());
        companyService.updateCompany(companyDto);

        // 添加apollo参数
        apolloService.createDbItems(dbSchemaDto.getCompanyCode());

        // 添加公司创建过程记录表信息
        companyCreateProcessService.insertCompanyCreateProcess(dbSchemaDto.getCompanyId(),
                CompanyCreationStepsEnum.DATABASE_STEP.getOrder(), CompanyCreationStepsEnum.DATABASE_STEP.getCode());

        // 更新 dbSchema 使用状态
        dbSchemaList.stream().filter(Objects::nonNull).forEach(dbSchema -> {
            dbSchema.setStatus(InstanceStatusEnum.USED.getCode());
            dbSchemaMapper.updateByPrimaryKeySelective(dbSchema);
        });

        // 初始化数据库表  如果返回上一步，待解决确定 String appInstanceName, String releaseVersion
        String jobName = JobNameEnum.getBykey(provisioningDataSourceConfig.provisioningApolloEvn.toUpperCase()).value;
        buildScriptMigrationData(jobName, dbSchemaDto.getCompanyCode(), dbSchemaDto.getTargetVersion());
        // 查询 build 状态成功做下步动作
        if (!jenkinsUtils.checkRunningStatusToJenkins(jobName)) {
            throw new ProvisioningException(BundleKey.BUILD_SCRIPT_MIGRATION_DATA, BundleKey.BUILD_SCRIPT_MIGRATION_DATA_MSG);
        }
        return dbSchemaDto;
    }

    /**
     * 通过 dbGroupId 查询
     * @param dbGroupId
     * @return
     */
    public List<DbSchema> getDbSchemaListByDbGroupId(Integer dbGroupId){
        Example example = new Example(DbSchema.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dbGroupId", dbGroupId);
        return dbSchemaMapper.selectByExample(example);
    }

    /**
     * 通过 dbGroupId 查询
     *
     * @param dbGroupId
     * @return
     */
    public List<DbSchemaDto> selectDbSchemaByDbGroupId(Integer dbGroupId) {
        List<DbSchema> dbSchemaList = getDbSchemaListByDbGroupId(dbGroupId);
        List<DbSchemaDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dbSchemaList)) {
            collect = dbSchemaList.stream()
                    .filter(Objects::nonNull)
                    .map(dbSchema -> {
                        DbSchemaDto dbSchemaDto = copyProperties(dbSchema);
                        DbInstanceDto dbInstanceDto = dbInstanceService.selectDbInstanceByPrimaryKey(dbSchemaDto.getDbInstanceId());
                        if (dbInstanceDto != null) {
                            dbSchemaDto.setDbInstanceDto(dbInstanceDto);
                        }
                        return dbSchemaDto;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return collect;
    }

    /**
     * 根据 类型 查询 db_schema 实例 列表
     * @param type
     * @return
     */
    public List<DbSchemaDto> getDbSchemaByType(Integer type) {
        Example example = new Example(DbSchema.class);
        example.setOrderByClause("ORDER BY create_time ASC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", type == null ? 0 : type);
        List<DbSchema> dbSchemas = dbSchemaMapper.selectByExample(example);
        return copyProperties(dbSchemas);
    }

    /**
     * 通过 db instance id 查询
     * @param dbInstanceId
     * @return
     */
    public List<DbSchema> getDbSchemaByDbInstanceId(Integer dbInstanceId) {
        Example example = new Example(DbSchema.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dbInstanceId", dbInstanceId);
        return dbSchemaMapper.selectByExample(example);
    }

    /**
     * 删除参数校验
     * @param id
     * @return
     */
    private DbSchema deleteDbSchemaParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DbSchema dbSchemaById = selectByPrimaryKey(id);
        if (dbSchemaById == null){
            throw new ProvisioningException(BundleKey.DB_SCHEMA_NOT_EXIST, BundleKey.DB_SCHEMA_NOT_EXIST_MSG);
        }
        DbGroup dbGroup = dbGroupService.selectByPrimaryKey(dbSchemaById.getDbGroupId());
        if (dbGroup != null){
            List<Company> companyList = companyService.getCompanyByDbGroupId(dbGroup.getId());
            if (!CollectionUtils.isEmpty(companyList)){
                throw new ProvisioningException(BundleKey.DB_SCHEMA_USED, BundleKey.DB_SCHEMA_USED_MSG);
            }
        }
        return dbSchemaById;
    }

    /**
     * 更新
     * @param dbSchemaDto
     */
    private DbSchema editDbSchemaParameterCheck(DbSchemaDto dbSchemaDto) {
        if (dbSchemaDto == null || dbSchemaDto.getTid() == null || dbSchemaDto.getTid() < 0 || dbSchemaDto.getDbInstanceId() == null
                || dbSchemaDto.getDbInstanceId() < 0 || dbSchemaDto.getDbGroupId() == null || dbSchemaDto.getDbGroupId() < 0
                || StringUtils.isBlank(dbSchemaDto.getSchemaName()) || StringUtils.isBlank(dbSchemaDto.getUsername())
                || StringUtils.isBlank(dbSchemaDto.getPassword()) || !Arrays.asList(0,1).contains(dbSchemaDto.getType())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DbSchema dbSchemaById = selectByPrimaryKey(dbSchemaDto.getTid());
        if (dbSchemaById == null){
            throw new ProvisioningException(BundleKey.DB_SCHEMA_NOT_EXIST, BundleKey.DB_SCHEMA_NOT_EXIST_MSG);
        }
        // 查询DB数据库组 和 DB实例表 是否为空
        checkDbGroupAndDbInstanceIsEmpty(dbSchemaDto.getDbGroupId(), dbSchemaDto.getDbInstanceId());

        Example example = new Example(DbSchema.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", dbSchemaDto.getTid());
        criteria.andEqualTo("dbInstanceId", dbSchemaDto.getDbInstanceId());
        criteria.andEqualTo("dbGroupId", dbSchemaDto.getDbGroupId());
        criteria.andEqualTo("type", dbSchemaDto.getType());
        DbSchema dbSchema = dbSchemaMapper.selectOneByExample(example);
        if (dbSchema != null){
            throw new ProvisioningException(BundleKey.DB_SCHEMA_ALREADY_EXIST, BundleKey.DB_SCHEMA_ALREADY_EXIST_MSG);
        }
        return dbSchemaById;
    }


    /**
     * 添加参数校验
     * @param dbSchemaDto
     */
    private void addDbSchemaParameterCheck(DbSchemaDto dbSchemaDto) {
        if (dbSchemaDto == null || dbSchemaDto.getDbInstanceId() == null || dbSchemaDto.getDbInstanceId() < 0 || dbSchemaDto.getDbGroupId() == null
                || dbSchemaDto.getDbGroupId() < 0 || StringUtils.isBlank(dbSchemaDto.getSchemaName()) || StringUtils.isBlank(dbSchemaDto.getUsername())
                || StringUtils.isBlank(dbSchemaDto.getPassword()) || !Arrays.asList(0,1).contains(dbSchemaDto.getType())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询DB数据库组 和 DB实例表 是否为空
        checkDbGroupAndDbInstanceIsEmpty(dbSchemaDto.getDbGroupId(), dbSchemaDto.getDbInstanceId());

        Example example = new Example(DbSchema.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dbInstanceId", dbSchemaDto.getDbInstanceId());
        criteria.andEqualTo("dbGroupId", dbSchemaDto.getDbGroupId());
        criteria.andEqualTo("type", dbSchemaDto.getType());
        DbSchema dbSchema = dbSchemaMapper.selectOneByExample(example);
        if (dbSchema != null){
            throw new ProvisioningException(BundleKey.DB_SCHEMA_ALREADY_EXIST, BundleKey.DB_SCHEMA_ALREADY_EXIST_MSG);
        }
    }

    /**
     * @param jobName
     * @param appInstanceName : 实例名
     * @param releaseVersion  ： 版本
     * @return
     */
    private String buildScriptMigrationData(String jobName, String appInstanceName, String releaseVersion) {
        // 校验参数
        if (StringUtils.isBlank(appInstanceName) || StringUtils.isBlank(releaseVersion)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 封装参数
        Map<String, String> parameters = encapsulatingRequestParameters(appInstanceName, releaseVersion);
        return jenkinsUtils.buildJobWithParameters(jobName, parameters);
    }

    /**
     * 封装参数
     *
     * @param appInstanceName : 实例名
     * @param releaseVersion  ： 版本
     * @return
     */
    private Map<String, String> encapsulatingRequestParameters(String appInstanceName, String releaseVersion) {
        // 封装 map 参数
        Map<String, String> parameters = new HashMap<>(2);
        parameters.put("appInstanceName", appInstanceName);
        parameters.put("releaseVersion", releaseVersion);
        return parameters;
    }

    /**
     * 选用数据库参数校验
     *
     * @param dbSchemaDto
     * @return
     */
    private List<DbSchema> useDbSchemaParameterCheck(DbSchemaDto dbSchemaDto) {
        if (dbSchemaDto == null || dbSchemaDto.getCompanyId() == null || dbSchemaDto.getCompanyId() < 0 || dbSchemaDto.getDbGroupId() == null
                || dbSchemaDto.getDbGroupId() < 0 || StringUtils.isBlank(dbSchemaDto.getTargetVersion())) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DbGroup dbGroup = dbGroupService.selectByPrimaryKey(dbSchemaDto.getDbGroupId());
        if (dbGroup == null){
            throw new ProvisioningException(BundleKey.DB_GROUP_NOT_EXIST, BundleKey.DB_GROUP_NOT_EXIST_MSG);
        }
        // 查询Schema 数据是否存在，不存在不操作。或是不被使用
        List<DbSchema> dbSchemaList = getDbSchemaListByDbGroupId(dbGroup.getId());
        if (CollectionUtils.isEmpty(dbSchemaList)){
            throw new ProvisioningException(BundleKey.DB_SCHEMA_NOT_EXIST, BundleKey.DB_SCHEMA_NOT_EXIST_MSG);
        }
        Long count = dbSchemaList.stream().filter(Objects::nonNull).filter(dbSchema -> dbSchema.getStatus().equals(InstanceStatusEnum.USED.getCode())).count();
        if (count != null && count > 0){
            throw new ProvisioningException(BundleKey.DB_SCHEMA_USED, BundleKey.DB_SCHEMA_USED_MSG);
        }
        dbSchemaList.stream().filter(Objects::nonNull)
                .forEach(dbSchema -> {
                    //  查询DB实例表是否存在，不存在不操作
                    DbInstance dbInstance = dbInstanceService.selectByPrimaryKey(dbSchema.getDbInstanceId());
                    if (dbInstance == null) {
                        throw new ProvisioningException(BundleKey.DB_INSTANCE_NOT_EXIST, BundleKey.DB_INSTANCE_NOT_EXIST_MSG);
                    }
                });

        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(dbSchemaDto.getCompanyId());
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        dbSchemaDto.setCompanyCode(company.getCompanyCode());
        return dbSchemaList;
    }

    /**
     * 查询DB数据库组 和 DB实例表 是否为空
     * @param dbGroupId
     * @param dbInstanceId
     */
    private void checkDbGroupAndDbInstanceIsEmpty(Integer dbGroupId, Integer dbInstanceId){
        // 查询DB数据库组是否存在
        DbGroup dbGroup = dbGroupService.selectByPrimaryKey(dbGroupId);
        if (dbGroup == null) {
            throw new ProvisioningException(BundleKey.DB_GROUP_NOT_EXIST, BundleKey.DB_GROUP_NOT_EXIST_MSG);
        }
        //  查询DB实例表是否存在，不存在不操作
        DbInstance dbInstance = dbInstanceService.selectByPrimaryKey(dbInstanceId);
        if (dbInstance == null) {
            throw new ProvisioningException(BundleKey.DB_INSTANCE_NOT_EXIST, BundleKey.DB_INSTANCE_NOT_EXIST_MSG);
        }
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    private DbSchema selectByPrimaryKey(Integer id) {
        return dbSchemaMapper.selectByPrimaryKey(id);
    }

    /**
     * 复制
     * @param dbSchemaList
     * @return
     */
    private List<DbSchemaDto> copyProperties(List<DbSchema> dbSchemaList){
        if (!CollectionUtils.isEmpty(dbSchemaList)){
            return dbSchemaList.stream()
                    .filter(Objects::nonNull)
                    .map(dbSchema -> {
                        return copyProperties(dbSchema);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param dbSchema
     * @return
     */
    private DbSchemaDto copyProperties(DbSchema dbSchema){
        if (dbSchema != null){
            DbSchemaDto dbSchemaDto = new DbSchemaDto();
            BeanUtils.copyProperties(dbSchema, dbSchemaDto);
            dbSchemaDto.setTid(dbSchema.getId());
            return dbSchemaDto;
        }
        return null;
    }
}
