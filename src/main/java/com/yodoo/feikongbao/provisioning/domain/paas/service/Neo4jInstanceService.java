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
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyCreateProcess;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class Neo4jInstanceService {

    @Autowired
    private Neo4jInstanceMapper neo4jInstanceMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    /**
     * 条件分页查询
     *
     * @param neo4jInstanceDto
     * @return
     */
    public PageInfoDto<Neo4jInstanceDto> queryNeo4jInstanceList(Neo4jInstanceDto neo4jInstanceDto) {
        Neo4jInstance neo4jInstance = new Neo4jInstance();
        if (neo4jInstanceDto != null) {
            BeanUtils.copyProperties(neo4jInstanceDto, neo4jInstance);
        }
        Page<?> pages = PageHelper.startPage(neo4jInstanceDto.getPageNum(), neo4jInstanceDto.getPageSize());
        List<Neo4jInstance> list = neo4jInstanceMapper.select(neo4jInstance);
        List<Neo4jInstanceDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(neo4jInstance1 -> {
                        Neo4jInstanceDto dto = new Neo4jInstanceDto();
                        BeanUtils.copyProperties(neo4jInstance1, dto);
                        dto.setTid(neo4jInstance1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<Neo4jInstanceDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public Neo4jInstanceDto getNeo4jInstanceDetails(Integer id) {
        Neo4jInstance neo4jInstance = selectByPrimaryKey(id);
        Neo4jInstanceDto neo4jInstanceDto = new Neo4jInstanceDto();
        if (neo4jInstance != null) {
            BeanUtils.copyProperties(neo4jInstance, neo4jInstanceDto);
            neo4jInstanceDto.setTid(neo4jInstance.getId());
        }
        return neo4jInstanceDto;
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
     * 创建公司 创建流程定义 TODO
     *
     * @param neo4jInstanceDto
     * @return
     */
    public Neo4jInstanceDto useNeo4jInstance(Neo4jInstanceDto neo4jInstanceDto) {
        useNeo4jInstanceParameterCheck(neo4jInstanceDto);

        // 创建工作流 TODO

        // 添加 neo4jInstance表数据
        Neo4jInstance neo4jInstance = new Neo4jInstance();
        BeanUtils.copyProperties(neo4jInstanceDto, neo4jInstance);
        neo4jInstanceMapper.insertSelective(neo4jInstance);

        // 更新公司表数据
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(neo4jInstanceDto.getCompanyId());
        companyDto.setNeo4jInstanceId(neo4jInstance.getId());
        companyService.updateCompany(companyDto);

        // 添加创建公司过程记录表数据
        companyCreateProcessService.insertCompanyCreateProcess(new CompanyCreateProcess(neo4jInstanceDto.getCompanyId(),
                CompanyCreationStepsEnum.NEO4J_STEP.getOrder(), CompanyCreationStepsEnum.NEO4J_STEP.getCode()));
        return neo4jInstanceDto;
    }

    /**
     * 创建公司 创建流程定义参数校验
     *
     * @param neo4jInstanceDto
     */
    private void useNeo4jInstanceParameterCheck(Neo4jInstanceDto neo4jInstanceDto) {
        if (neo4jInstanceDto == null || neo4jInstanceDto.getCompanyId() == null || neo4jInstanceDto.getCompanyId() < 0
                || StringUtils.isBlank(neo4jInstanceDto.getNeo4jName()) || StringUtils.isBlank(neo4jInstanceDto.getIp())
                || neo4jInstanceDto.getPort() != null || neo4jInstanceDto.getPort() < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Neo4jInstance neo4jInstance = new Neo4jInstance();
        neo4jInstance.setNeo4jName(neo4jInstanceDto.getNeo4jName());
        Neo4jInstance neo4jInstanceResponse = neo4jInstanceMapper.selectOne(neo4jInstance);
        if (neo4jInstanceResponse != null) {
            throw new ProvisioningException(BundleKey.NEO4J_INSTANCE_ALREADY_EXIST, BundleKey.NEO4J_INSTANCE_ALREADY_EXIST_MSG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(neo4jInstanceDto.getCompanyId());
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
    }
}
