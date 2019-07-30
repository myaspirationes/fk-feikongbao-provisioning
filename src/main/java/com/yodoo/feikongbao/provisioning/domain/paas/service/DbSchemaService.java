package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbSchemaDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.DbSchema;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.DbSchemaMapper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyCreateProcess;
import com.yodoo.feikongbao.provisioning.domain.system.service.ApolloService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.JenkinsEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.megalodon.datasource.config.JenkinsConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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
    private JenkinsManagerService jenkinsManagerService;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    /**
     * 创建数据库
     * @param dbSchemaDto
     * @return
     */
    public DbSchemaDto useDbSchema(DbSchemaDto dbSchemaDto) {
        // 参数校验
        DbSchema dbSchema = useDbSchemaParameterCheck(dbSchemaDto);

        // 初始化数据库表
        jenkinsManagerService.buildScriptMigrationData(dbSchemaDto.getCompanyCode(), JenkinsEnum.ROLL_FORWARD.getAction(), dbSchemaDto.getTargetVersion(), Arrays.asList(dbSchema.getSchemaName()));
        // 查询 build 状态成功做下步动作
        if (!jenkinsManagerService.checkRunningStatusToJenkins(jenkinsConfig.jenkinsScriptMigrationDataJobName)){
            jenkinsManagerService.buildScriptMigrationData(dbSchemaDto.getCompanyCode(), JenkinsEnum.ROLL_BACK.getAction(), dbSchemaDto.getTargetVersion(), Arrays.asList(dbSchema.getSchemaName()));
            throw new ProvisioningException(BundleKey.BUILD_SCRIPT_MIGRATION_DATA, BundleKey.BUILD_SCRIPT_MIGRATION_DATA_MSG);
        }

        // 更新公司表信息
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(dbSchemaDto.getCompanyId());
        companyDto.setDbGroupId(dbSchema.getDbGroupId());
        companyService.updateCompany(companyDto);

        // 添加apollo参数
        apolloService.createDBItems(dbSchemaDto.getCompanyCode());

        // 添加公司创建过程记录表信息
        companyCreateProcessService.insertCompanyCreateProcess(new CompanyCreateProcess(dbSchemaDto.getCompanyId(),
                CompanyCreationStepsEnum.SECOND_STEP.getCode(),CompanyCreationStepsEnum.SECOND_STEP.getName()));

        return dbSchemaDto;
    }

    /**
     * 先用数据库参数校验
     * @param dbSchemaDto
     * @return
     */
    private DbSchema useDbSchemaParameterCheck(DbSchemaDto dbSchemaDto) {
        if (dbSchemaDto == null || dbSchemaDto.getCompanyId() == null || dbSchemaDto.getCompanyId() < 0
                || dbSchemaDto.getTid() == null || dbSchemaDto.getTid() < 0 || StringUtils.isBlank(dbSchemaDto.getTargetVersion())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询Schema 数据是否存在，不存在不操作
        DbSchema dbSchema = dbSchemaMapper.selectByPrimaryKey(dbSchemaDto.getTid());
        if (dbSchema == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // 查询DB数据库组是否存在
        DbGroup dbGroup = dbGroupService.selectByPrimaryKey(dbSchema.getDbGroupId());
        if (dbGroup == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        //  查询DB实例表是否存在，不存在不操作
        DbInstance dbInstance = dbInstanceService.selectByPrimaryKey(dbSchema.getDbInstanceId());
        if (dbInstance == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(dbSchemaDto.getCompanyId());
        if (company == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        dbSchemaDto.setCompanyCode(company.getCompanyCode());
        return dbSchema;
    }
}
