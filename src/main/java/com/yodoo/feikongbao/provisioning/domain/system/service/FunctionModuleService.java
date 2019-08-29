package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.FunctionModuleDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyFunctionModule;
import com.yodoo.feikongbao.provisioning.domain.system.entity.FunctionModule;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.FunctionModuleMapper;
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
 * @Description ：功能模块
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class FunctionModuleService {

    @Autowired
    private FunctionModuleMapper functionModuleMapper;

    @Autowired
    private CompanyFunctionModuleService companyFunctionModuleService;

    /**
     * 条件分页查询
     * @param functionModuleDto
     * @return
     */
    public PageInfoDto<FunctionModuleDto> queryDictionaryList(FunctionModuleDto functionModuleDto) {
        Example example = new Example(FunctionModule.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(functionModuleDto.getPageNum(), functionModuleDto.getPageSize());
        List<FunctionModule> select = functionModuleMapper.selectByExample(example);
        List<FunctionModuleDto> collect = copyProperties(select);
        return new PageInfoDto<FunctionModuleDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public FunctionModule selectByPrimaryKey(Integer id) {
        return functionModuleMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param functionModuleDto
     * @return
     */
    public Integer addFunctionModule(FunctionModuleDto functionModuleDto) {
        addFunctionModuleParameterCheck(functionModuleDto);
        FunctionModule functionModule = new FunctionModule();
        BeanUtils.copyProperties(functionModuleDto, functionModule);
        return functionModuleMapper.insertSelective(functionModule);
    }

    /**
     * 修改
     *
     * @param functionModuleDto
     * @return
     */
    public Integer editFunctionModule(FunctionModuleDto functionModuleDto) {
        FunctionModule functionModule = editFunctionModuleParameterCheck(functionModuleDto);
        BeanUtils.copyProperties(functionModuleDto, functionModule);
        return functionModuleMapper.updateByPrimaryKeySelective(functionModule);
    }

    /**
     * 删除
     * @param id
     */
    public Integer deleteFunctionModule(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        FunctionModule functionModule = selectByPrimaryKey(id);
        if (functionModule == null){
            throw new ProvisioningException(BundleKey.FUNCTION_MODULE_NOT_EXIST, BundleKey.FUNCTION_MODULE_NOT_EXIST_MSG);
        }
        List<CompanyFunctionModule> companyFunctionModuleList = companyFunctionModuleService.getCompanyFunctionModuleByFunctionModuleId(id);
        if (!CollectionUtils.isEmpty(companyFunctionModuleList)){
            throw new ProvisioningException(BundleKey.FUNCTION_MODULE_IS_USE, BundleKey.FUNCTION_MODULE_IS_USE_MSG);
        }
        return functionModuleMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改参数校验
     *
     * @param functionModuleDto
     * @return
     */
    private FunctionModule editFunctionModuleParameterCheck(FunctionModuleDto functionModuleDto) {
        if (functionModuleDto == null || functionModuleDto.getTid() == null || functionModuleDto.getTid() < 0
                || StringUtils.isBlank(functionModuleDto.getName()) || StringUtils.isBlank(functionModuleDto.getDescription())
                || StringUtils.isBlank(functionModuleDto.getOrderNo())) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        FunctionModule functionModule = selectByPrimaryKey(functionModuleDto.getTid());
        if (functionModule == null) {
            throw new ProvisioningException(BundleKey.FUNCTION_MODULE_NOT_EXIST, BundleKey.FUNCTION_MODULE_NOT_EXIST_MSG);
        }
        // 查询除了自己以外是否有相同的数据
        Example example = new Example(FunctionModule.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", functionModuleDto.getTid());
        if (functionModuleDto.getParentId() != null && functionModuleDto.getParentId() > 0) {
            criteria.andEqualTo("parentId", functionModuleDto.getParentId());
        }
        criteria.andEqualTo("name", functionModuleDto.getName());
        FunctionModule functionModuleResponse = functionModuleMapper.selectOneByExample(example);
        if (functionModuleResponse != null) {
            throw new ProvisioningException(BundleKey.FUNCTION_MODULE_ALREADY_EXIST, BundleKey.FUNCTION_MODULE_EXIST_MSG);
        }
        return functionModule;
    }

    /**
     * 添加参数校验
     *
     * @param functionModuleDto
     */
    private void addFunctionModuleParameterCheck(FunctionModuleDto functionModuleDto) {
        if (functionModuleDto == null || StringUtils.isBlank(functionModuleDto.getName()) || StringUtils.isBlank(functionModuleDto.getDescription())
                || StringUtils.isBlank(functionModuleDto.getOrderNo())) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询是否存在
        Example example = new Example(FunctionModule.class);
        Example.Criteria criteria = example.createCriteria();
        if (functionModuleDto.getParentId() != null && functionModuleDto.getParentId() > 0) {
            criteria.andEqualTo("parentId", functionModuleDto.getParentId());
        }
        criteria.andEqualTo("name", functionModuleDto.getName());
        FunctionModule functionModuleResponse = functionModuleMapper.selectOneByExample(example);
        if (functionModuleResponse != null) {
            throw new ProvisioningException(BundleKey.FUNCTION_MODULE_ALREADY_EXIST, BundleKey.FUNCTION_MODULE_EXIST_MSG);
        }
    }

    /**
     * 复制
     * @param functionModuleList
     * @return
     */
    private List<FunctionModuleDto> copyProperties(List<FunctionModule> functionModuleList){
        if (!CollectionUtils.isEmpty(functionModuleList)){
            return functionModuleList.stream()
                    .filter(Objects::nonNull)
                    .map(functionModule -> {
                        return copyProperties(functionModule);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param functionModule
     * @return
     */
    private FunctionModuleDto copyProperties(FunctionModule functionModule){
        if (functionModule != null){
            FunctionModuleDto functionModuleDto = new FunctionModuleDto();
            BeanUtils.copyProperties(functionModule, functionModuleDto);
            functionModuleDto.setTid(functionModule.getId());
            return functionModuleDto;
        }
        return null;
    }
}
