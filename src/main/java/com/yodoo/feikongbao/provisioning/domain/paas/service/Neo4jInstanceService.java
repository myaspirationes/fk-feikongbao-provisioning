package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.Neo4jInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.Neo4jInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.Neo4jInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.service.ApolloService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.InstanceStatusEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.Base64Util;
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
 * @Description ：neo4j
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class Neo4jInstanceService {

    @Autowired
    private Neo4jInstanceMapper neo4jInstanceMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    @Autowired
    private ApolloService apolloService;

    /**
     * 条件分页查询
     *
     * @param neo4jInstanceDto
     * @return
     */
    public PageInfoDto<Neo4jInstanceDto> queryNeo4jInstanceList(Neo4jInstanceDto neo4jInstanceDto) {
        Example example = new Example(Neo4jInstance.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(neo4jInstanceDto.getPageNum(), neo4jInstanceDto.getPageSize());
        List<Neo4jInstance> list = neo4jInstanceMapper.selectByExample(example);
        List<Neo4jInstanceDto> collect = copyProperties(list);
        return new PageInfoDto<Neo4jInstanceDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public Neo4jInstanceDto getNeo4jInstanceDetails(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public Neo4jInstance selectByPrimaryKey(Integer id) {
        return neo4jInstanceMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     * @param neo4jInstanceDto
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer addNeo4jInstance(Neo4jInstanceDto neo4jInstanceDto) {
        // 参数校验
        addNeo4jInstanceParameterCheck(neo4jInstanceDto);
        Neo4jInstance neo4jInstance = new Neo4jInstance();
        BeanUtils.copyProperties(neo4jInstanceDto, neo4jInstance);
        neo4jInstance.setStatus(InstanceStatusEnum.UNUSED.getCode());
        return neo4jInstanceMapper.insertSelective(neo4jInstance);
    }

    /**
     * 修改
     * @param neo4jInstanceDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer editNeo4jInstance(Neo4jInstanceDto neo4jInstanceDto) {
        Neo4jInstance neo4jInstance = editNeo4jInstanceParameterCheck(neo4jInstanceDto);
        neo4jInstance.setUrl(neo4jInstanceDto.getUrl());
        neo4jInstance.setInitialUsername(neo4jInstanceDto.getInitialUsername());
        neo4jInstance.setInitialPassword(neo4jInstanceDto.getInitialPassword());
        return neo4jInstanceMapper.updateByPrimaryKeySelective(neo4jInstance);
    }

    /**
     * 创建公司 创建流程定义 TODO
     *
     * @param neo4jInstanceDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Neo4jInstanceDto useNeo4jInstance(Neo4jInstanceDto neo4jInstanceDto) {

        // 参数校验
        Neo4jInstance neo4jInstance = useNeo4jInstanceParameterCheck(neo4jInstanceDto);
        // 更新选择使用的neo4j数据
        neo4jInstanceMapper.updateByPrimaryKeySelective(neo4jInstance);

        // 更新公司表数据
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(neo4jInstanceDto.getCompanyId());
        companyDto.setNeo4jInstanceId(neo4jInstance.getId());
        companyService.updateCompany(companyDto);

        // 添加创建公司过程记录表数据
        companyCreateProcessService.insertCompanyCreateProcess(neo4jInstanceDto.getCompanyId(),
                CompanyCreationStepsEnum.NEO4J_STEP.getOrder(), CompanyCreationStepsEnum.NEO4J_STEP.getCode());

        // 创建工作流配置到apollo TODO
        apolloService.createNeo4j(neo4jInstanceDto.getNeo4jName());

        return neo4jInstanceDto;

    }

    /**
     * 删除
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer deleteNeo4jInstance(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Neo4jInstance neo4jInstance = selectByPrimaryKey(id);
        if (neo4jInstance == null){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_NOT_EXIST, BundleKey.NEO4J_INSTANCE_NOT_EXIST_MSG);
        }else if (neo4jInstance.getStatus().equals(InstanceStatusEnum.USED.getCode())){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_ALREADY_USED, BundleKey.NEO4J_INSTANCE_ALREADY_USED_MSG);
        }
        return neo4jInstanceMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改参数校验
     * @param neo4jInstanceDto
     */
    private Neo4jInstance editNeo4jInstanceParameterCheck(Neo4jInstanceDto neo4jInstanceDto) {
        if (neo4jInstanceDto == null || neo4jInstanceDto.getTid() == null || neo4jInstanceDto.getTid() < 0
                || StringUtils.isBlank(neo4jInstanceDto.getUrl()) || StringUtils.isBlank(neo4jInstanceDto.getInitialUsername())
                || StringUtils.isBlank(neo4jInstanceDto.getInitialPassword())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Neo4jInstance neo4jInstance = selectByPrimaryKey(neo4jInstanceDto.getTid());
        if (neo4jInstance == null){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_NOT_EXIST, BundleKey.NEO4J_INSTANCE_NOT_EXIST_MSG);
        }
        // 是否存在些集群
        Example example = new Example(Neo4jInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", neo4jInstanceDto.getTid());
        criteria.andEqualTo("url", neo4jInstanceDto.getUrl());
        List<Neo4jInstance> neo4jInstances = neo4jInstanceMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(neo4jInstances)){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_ALREADY_EXIST, BundleKey.NEO4J_INSTANCE_ALREADY_EXIST_MSG);
        }
        return neo4jInstance;
    }

    /**
     * 添加参数校验
     * @param neo4jInstanceDto
     */
    private void addNeo4jInstanceParameterCheck(Neo4jInstanceDto neo4jInstanceDto) {
        if (neo4jInstanceDto == null || StringUtils.isBlank(neo4jInstanceDto.getUrl()) || StringUtils.isBlank(neo4jInstanceDto.getInitialUsername())
                || StringUtils.isBlank(neo4jInstanceDto.getInitialPassword())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 是否存在些集群
        Example example = new Example(Neo4jInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("url", neo4jInstanceDto.getUrl());
        List<Neo4jInstance> neo4jInstances = neo4jInstanceMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(neo4jInstances)){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_ALREADY_EXIST, BundleKey.NEO4J_INSTANCE_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 创建公司 创建流程定义参数校验
     *
     * @param neo4jInstanceDto
     */
    private Neo4jInstance useNeo4jInstanceParameterCheck(Neo4jInstanceDto neo4jInstanceDto) {
        if (neo4jInstanceDto == null || neo4jInstanceDto.getTid() == null || neo4jInstanceDto.getTid() < 0
                || neo4jInstanceDto.getCompanyId() == null || neo4jInstanceDto.getCompanyId() < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 检查 neo4j 实例是否存在他参数是否不为空，不存在不创建。是否被使用
        Neo4jInstance neo4jInstance = selectByPrimaryKey(neo4jInstanceDto.getTid());
        if (neo4jInstance == null || StringUtils.isBlank(neo4jInstance.getUrl()) || StringUtils.isBlank(neo4jInstance.getInitialUsername())
            || StringUtils.isBlank(neo4jInstance.getInitialPassword())){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_NOT_EXIST, BundleKey.NEO4J_INSTANCE_NOT_EXIST_MSG);
        }else if (neo4jInstance.getStatus().equals(InstanceStatusEnum.USED.getCode())){
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_ALREADY_USED, BundleKey.NEO4J_INSTANCE_ALREADY_USED_MSG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(neo4jInstanceDto.getCompanyId());
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        neo4jInstance.setUsername(company.getCompanyCode());
        neo4jInstance.setPassword(Base64Util.base64Encoder(company.getCompanyCode()));
        neo4jInstance.setNeo4jName(company.getCompanyCode());
        neo4jInstance.setStatus(InstanceStatusEnum.USED.getCode());
        neo4jInstanceDto.setNeo4jName(company.getCompanyCode());
        return neo4jInstance;
    }

    /**
     * 复制
     * @param neo4jInstanceList
     * @return
     */
    private List<Neo4jInstanceDto> copyProperties(List<Neo4jInstance> neo4jInstanceList){
        if (!CollectionUtils.isEmpty(neo4jInstanceList)){
            return neo4jInstanceList.stream()
                    .filter(Objects::nonNull)
                    .map(neo4jInstance -> {
                        return copyProperties(neo4jInstance);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param neo4jInstance
     * @return
     */
    private Neo4jInstanceDto copyProperties(Neo4jInstance neo4jInstance){
        if (neo4jInstance != null){
            Neo4jInstanceDto neo4jInstanceDto = new Neo4jInstanceDto();
            BeanUtils.copyProperties(neo4jInstance, neo4jInstanceDto);
            neo4jInstanceDto.setTid(neo4jInstance.getId());
            return neo4jInstanceDto;
        }
        return null;
    }
}
