package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyFunctionModuleDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyFunctionModule;
import com.yodoo.feikongbao.provisioning.domain.system.entity.FunctionModule;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyFunctionModuleMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Description ：公司功能模块关系
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class CompanyFunctionModuleService {

    @Autowired
    private CompanyFunctionModuleMapper companyFunctionModuleMapper;

    @Autowired
    private FunctionModuleService functionModuleService;

    @Autowired
    private CompanyService companyService;

    /**
     * 公司添加功能
     *
     * @param companyFunctionModuleDto
     */
    public Integer addOroEditCompanyFunctionModule(CompanyFunctionModuleDto companyFunctionModuleDto) {
        // 存在、修改状态
        CompanyFunctionModule companyFunctionModuleResponse = addOroEditCompanyFunctionModuleParameterCheck(companyFunctionModuleDto);
        if (companyFunctionModuleResponse != null) {
            companyFunctionModuleResponse.setStatus(companyFunctionModuleDto.getStatus());
            return companyFunctionModuleMapper.updateByPrimaryKeySelective(companyFunctionModuleResponse);
        } else {
            // 不存在、添加
            return companyFunctionModuleMapper.insertSelective(new CompanyFunctionModule(companyFunctionModuleDto.getFunctionModuleId(),
                    companyFunctionModuleDto.getCompanyId(), companyFunctionModuleDto.getStatus()));
        }
    }

    /**
     * 通过 functionModuleId 查询
     * @param functionModuleId
     * @return
     */
    public List<CompanyFunctionModule> getCompanyFunctionModuleByFunctionModuleId(Integer functionModuleId){
        if (functionModuleId == null || functionModuleId < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(CompanyFunctionModule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("functionModuleId", functionModuleId);
       return companyFunctionModuleMapper.selectByExample(example);
    }

    /**
     * 通过功能模块 id 和公司 id 查询
     *
     * @param companyFunctionModuleDto
     * @return
     */
    public CompanyFunctionModule selectCompanyFunctionModuleByFunctionModuleIdAndCompanyId(CompanyFunctionModuleDto companyFunctionModuleDto) {
        return companyFunctionModuleMapper.selectOne(new CompanyFunctionModule(companyFunctionModuleDto.getFunctionModuleId(), companyFunctionModuleDto.getCompanyId()));
    }

    /**
     * 公司添加功能参数校验
     *
     * @param companyFunctionModuleDto
     */
    private CompanyFunctionModule addOroEditCompanyFunctionModuleParameterCheck(CompanyFunctionModuleDto companyFunctionModuleDto) {
        if (companyFunctionModuleDto == null || companyFunctionModuleDto.getFunctionModuleId() == null || companyFunctionModuleDto.getFunctionModuleId() < 0
                || companyFunctionModuleDto.getCompanyId() == null || companyFunctionModuleDto.getCompanyId() < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 如果功能模块为空不操作
        FunctionModule functionModule = functionModuleService.selectByPrimaryKey(companyFunctionModuleDto.getFunctionModuleId());
        if (functionModule == null) {
            throw new ProvisioningException(BundleKey.FUNCTION_MODULE_NOT_EXIST, BundleKey.FUNCTION_MODULE_NOT_EXIST_MSG);
        }
        // 公司不存在不操作
        Company company = companyService.selectByPrimaryKey(companyFunctionModuleDto.getCompanyId());
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        // 如果当前公司功能 模块存在不操作
        return selectCompanyFunctionModuleByFunctionModuleIdAndCompanyId(companyFunctionModuleDto);
    }
}
