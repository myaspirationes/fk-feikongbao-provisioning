package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
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
import com.yodoo.feikongbao.provisioning.enums.SchemaStatusEnum;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.JenkinsUtils;
import com.yodoo.feikongbao.provisioning.util.RequestPrecondition;
import com.yodoo.megalodon.datasource.config.JenkinsConfig;
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
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
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
    private JenkinsConfig jenkinsConfig;

    @Autowired
    private JenkinsUtils jenkinsUtils;

    /**
     * 创建数据库 TODO 目前测试先注掉初始化数据库表，最后测试记得放开，返回上一步，
     *
     * @param dbSchemaDto
     * @return
     */
    public ProvisioningDto<?> useDbSchema(DbSchemaDto dbSchemaDto) {
        // 参数校验
        ProvisioningDto provisioningDto = useDbSchemaParameterCheck(dbSchemaDto);
        if (provisioningDto != null){
            return provisioningDto;
        }

        // 更新公司表信息
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(dbSchemaDto.getCompanyId());
        companyDto.setDbGroupId(dbSchemaDto.getDbGroupId());
        companyService.updateCompany(companyDto);

        // 添加apollo参数
        apolloService.createdbItems(dbSchemaDto.getCompanyCode());

        // 添加公司创建过程记录表信息
        companyCreateProcessService.insertCompanyCreateProcess(dbSchemaDto.getCompanyId(),
                CompanyCreationStepsEnum.DATABASE_STEP.getOrder(), CompanyCreationStepsEnum.DATABASE_STEP.getCode());

        // 更新 dbSchema 使用状态
        DbSchema dbSchema = selectByPrimaryKey(dbSchemaDto.getTid());
        dbSchema.setStatus(SchemaStatusEnum.USED.getCode());
        dbSchemaMapper.updateByPrimaryKeySelective(dbSchema);

        // 初始化数据库表 TODO 如果返回上一步，待解决
        // buildScriptMigrationData(dbSchemaDto.getCompanyCode(), ScriptMigrationDataEnum.ROLL_FORWARD.getAction(), dbSchemaDto.getTargetVersion(), Arrays.asList(dbSchema.getSchemaName()));
        // 查询 build 状态成功做下步动作
        // if (!jenkinsUtils.checkRunningStatusToJenkins(jenkinsConfig.jenkinsScriptMigrationDataJobName)) {
        //     throw new ProvisioningException(BundleKey.BUILD_SCRIPT_MIGRATION_DATA, BundleKey.BUILD_SCRIPT_MIGRATION_DATA_MSG);
        // }

        return new ProvisioningDto<DbSchemaDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, dbSchemaDto);
    }

    /**
     * 通过 dbGroupId 查询
     *
     * @param dbGroupId
     * @return
     */
    public List<DbSchemaDto> selectDbSchemaByDbGroupId(Integer dbGroupId) {
        Example example = new Example(DbSchema.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("dbGroupId", dbGroupId);
        List<DbSchema> dbSchemas = dbSchemaMapper.selectByExample(example);
        List<DbSchemaDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dbSchemas)) {
            collect = dbSchemas.stream()
                    .filter(Objects::nonNull)
                    .map(dbSchema -> {
                        DbSchemaDto dbSchemaDto = new DbSchemaDto();
                        BeanUtils.copyProperties(dbSchema, dbSchemaDto);
                        dbSchemaDto.setTid(dbSchema.getId());
                        DbInstance dbInstance = dbInstanceService.selectByPrimaryKey(dbSchemaDto.getDbInstanceId());
                        if (dbInstance != null) {
                            DbInstanceDto dbInstanceDto = new DbInstanceDto();
                            BeanUtils.copyProperties(dbInstanceDto, dbInstance);
                            dbInstanceDto.setTid(dbInstance.getId());
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
        List<DbSchemaDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dbSchemas)){
            collect = dbSchemas.stream()
                    .filter(Objects::nonNull)
                    .map(dbSchema -> {
                        DbSchemaDto dbSchemaDto = new DbSchemaDto();
                        BeanUtils.copyProperties(dbSchema, dbSchemaDto);
                        dbSchemaDto.setTid(dbSchema.getId());
                        return dbSchemaDto;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return collect;
    }

    /**
     * 数据脚本迁移，json 串不能用双引或单引号，可以用其它符号，目前用#号
     * json : {"instanceId":"dev_mysql","schemas":[{"schema":"migrate_database"}]}
     *
     * @param instanceId : 实例名
     * @param action     ：执行的动作 ScriptMigrationDataEnum
     * @param version    ： 版本
     * @param schemaList ：schemas 可以传多个
     * @return
     */
    private String buildScriptMigrationData(String instanceId, String action, String version, List<String> schemaList) {
        // 校验参数
        RequestPrecondition.checkArguments(!com.yodoo.feikongbao.provisioning.util.StringUtils.isContainEmpty(instanceId, action, version));
        if (CollectionUtils.isEmpty(schemaList)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 封装参数
        Map<String, String> parameters = encapsulatingRequestParameters(instanceId, action, version, schemaList);
        return jenkinsUtils.buildJobWithParameters(jenkinsConfig.jenkinsScriptMigrationDataJobName, parameters);
    }

    /**
     * 封装参数
     *
     * @param instanceId : 实例名
     * @param action     ：执行的动作 ScriptMigrationDataEnum
     * @param version    ： 版本
     * @param schemaList ：schemas 可以传多个
     * @return
     */
    private Map<String, String> encapsulatingRequestParameters(String instanceId, String action, String version, List<String> schemaList) {
        // 把 schema拼接成json串
        StringBuilder schemaStringBuilder = new StringBuilder();
        schemaList.stream()
                .filter(Objects::nonNull)
                .forEach(schemaName -> {
                    schemaStringBuilder.append(",{#schema#:#" + schemaName + "#}");
                });
        // 去掉第一个豆号
        String schemas = schemaStringBuilder.toString().substring(1, schemaStringBuilder.toString().length());
        // 封装 map 参数
        Map<String, String> parameters = new HashMap<>(3);
        parameters.put("instanceId", "{#instanceId#:#" + instanceId + "#,#schemas#:[" + schemas + "]}");
        parameters.put("action", action);
        parameters.put("version", version);
        return parameters;
    }

    /**
     * 先用数据库参数校验
     *
     * @param dbSchemaDto
     * @return
     */
    private ProvisioningDto<?> useDbSchemaParameterCheck(DbSchemaDto dbSchemaDto) {
        if (dbSchemaDto == null || dbSchemaDto.getCompanyId() == null || dbSchemaDto.getCompanyId() < 0
                || dbSchemaDto.getTid() == null || dbSchemaDto.getTid() < 0 || StringUtils.isBlank(dbSchemaDto.getTargetVersion())) {
            return new ProvisioningDto(SystemStatus.FAIL.getStatus(), BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询Schema 数据是否存在，不存在不操作。或是不被使用
        DbSchema dbSchema = selectByPrimaryKey(dbSchemaDto.getTid());
        if (dbSchema == null) {
            return new ProvisioningDto(SystemStatus.FAIL.getStatus(), BundleKey.DB_SCHEMA_NOT_EXIST, BundleKey.DB_SCHEMA_NOT_EXIST_MSG);
        }else if (dbSchema.getStatus().equals(SchemaStatusEnum.USED.getCode())){
            return new ProvisioningDto(SystemStatus.FAIL.getStatus(), BundleKey.DB_SCHEMA_USED, BundleKey.DB_SCHEMA_USED_MSG);
        }
        // 查询DB数据库组是否存在
        DbGroup dbGroup = dbGroupService.selectByPrimaryKey(dbSchema.getDbGroupId());
        if (dbGroup == null) {
            return new ProvisioningDto(SystemStatus.FAIL.getStatus(), BundleKey.DB_GROUP_NOT_EXIST, BundleKey.DB_GROUP_NOT_EXIST_MSG);
        }
        //  查询DB实例表是否存在，不存在不操作
        DbInstance dbInstance = dbInstanceService.selectByPrimaryKey(dbSchema.getDbInstanceId());
        if (dbInstance == null) {
            return new ProvisioningDto(SystemStatus.FAIL.getStatus(), BundleKey.DB_INSTANCE_NOT_EXIST, BundleKey.DB_INSTANCE_NOT_EXIST_MSG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(dbSchemaDto.getCompanyId());
        if (company == null) {
            return new ProvisioningDto(SystemStatus.FAIL.getStatus(), BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        dbSchemaDto.setCompanyCode(company.getCompanyCode());
        dbSchemaDto.setDbGroupId(dbSchema.getDbGroupId());

        return null;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    private DbSchema selectByPrimaryKey(Integer id) {
        return dbSchemaMapper.selectByPrimaryKey(id);
    }
}
