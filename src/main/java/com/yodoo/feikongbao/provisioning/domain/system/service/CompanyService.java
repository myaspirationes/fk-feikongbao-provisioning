package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.*;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.MqVhost;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.Neo4jInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.SwiftProject;
import com.yodoo.feikongbao.provisioning.domain.paas.service.*;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Groups;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyMapper;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.CompanyStatusEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2019/7/29 13:08
 * @Created by houzhen
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private GroupsService groupService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    @Autowired
    private ApolloService apolloService;

    @Autowired
    private DbGroupService dbGroupService;

    @Autowired
    private RedisGroupService redisGroupService;

    @Autowired
    private SwiftProjectService swiftProjectService;

    @Autowired
    private MqVhostService mqVhostService;

    @Autowired
    private Neo4jInstanceService neo4jInstanceService;

    /**
     * 分页查询
     *
     * @Author houzhen
     * @Date 13:21 2019/7/29
     **/
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public PageInfoDto<CompanyDto> queryCompanyList(CompanyDto companyDto) {
        List<CompanyDto> dtoList = new ArrayList<>();
        // 设置查询条件
        Company findParams = new Company();
        if (companyDto != null) {
            BeanUtils.copyProperties(companyDto, findParams);
        }
        Page<?> pages = PageHelper.startPage(companyDto.getPageNum(), companyDto.getPageSize());
        List<Company> companyList = companyMapper.select(findParams);
        if (!CollectionUtils.isEmpty(companyList)) {
            companyList.forEach(entity -> {
                CompanyDto dto = new CompanyDto();
                BeanUtils.copyProperties(entity, dto);
                dto.setTid(entity.getId());
                dtoList.add(dto);
            });
        }
        return new PageInfoDto<CompanyDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), dtoList);
    }

    /**
     * 添加
     *
     * @param companyDto
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public CompanyDto addCompany(CompanyDto companyDto) {
        // 校验
        addCompanyParameterCheck(companyDto);

        // 公司基础数据先落库
        Company company = new Company();
        if (companyDto.getGroupId() != null && companyDto.getGroupId() > 0) {
            company.setGroupId(companyDto.getGroupId());
        }
        company.setCompanyCode(companyDto.getCompanyCode());
        Company selectOneCompany = companyMapper.selectOne(company);
        if (selectOneCompany != null){
            BeanUtils.copyProperties(companyDto, selectOneCompany);
            selectOneCompany.setStatus(CompanyStatusEnum.CREATING.getCode());
            companyMapper.updateByPrimaryKeySelective(selectOneCompany);
        }else {
            selectOneCompany = new Company();
            BeanUtils.copyProperties(companyDto, selectOneCompany);
            selectOneCompany.setStatus(CompanyStatusEnum.CREATING.getCode());
            companyMapper.insertSelective(selectOneCompany);
        }

        // 调用 apollo 创建环境
        apolloService.createCluster(company.getCompanyCode());

        // 添加公司创建过程 记录表
        companyCreateProcessService.insertCompanyCreateProcess(company.getId(),
                CompanyCreationStepsEnum.COMPANY_STEP.getOrder(), CompanyCreationStepsEnum.COMPANY_STEP.getCode());

        // 添加完成，把数据返回，用于下步操作 TODO
        companyDto.setTid(company.getId());

        return companyDto;
    }

    /**
     * 更新公司基础信息
     *
     * @param companyDto
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer editCompany(CompanyDto companyDto) {
        Company company = editCompanyParameterCheck(companyDto);
        return companyMapper.updateByPrimaryKeySelective(company);
    }

    /**
     * 更新公司基础信息
     * dbGroupId ：DB数据库组id
     * redisGroupId ：redis组id
     * swiftProjectId ：swift租户id
     * mqVhostId ：消息队列vhost
     * neo4jInstanceId ：neo4j实例id
     * status ：状态，0：创建中，1：创建完成,启用中， 2：停用
     *
     * @param companyDto
     * @return
     */
    public Integer updateCompany(CompanyDto companyDto) {
        if (companyDto == null || companyDto.getTid() == null || companyDto.getTid() < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Company company = selectByPrimaryKey(companyDto.getTid());
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        buildUpdateParameter(company, companyDto);
        return companyMapper.updateByPrimaryKeySelective(company);
    }

    /**
     * 删除 TODO
     *
     * @param id
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer deleteCompany(Integer id) {
        deleteCompanyParameterCheck(id);
        return companyMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过集团id 查询
     *
     * @param groupsId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer selectGroupCountByGroupId(Integer groupsId) {
        Company company = new Company();
        company.setGroupId(groupsId);
        return companyMapper.selectCount(company);
    }

    /**
     * 通过公司 code 查询
     *
     * @param companyCode
     * @return
     */
    public CompanyDto getCompanyByCompanyCode(String companyCode) {
        if (StringUtils.isBlank(companyCode)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Company company = new Company();
        company.setCompanyCode(companyCode);
        Company companyResponse = companyMapper.selectOne(company);
        CompanyDto companyDto = null;
        if (companyResponse != null) {
            companyDto = new CompanyDto();
            BeanUtils.copyProperties(companyResponse, companyCode);
            companyDto.setTid(companyResponse.getId());
        }
        return companyDto;
    }

    /**
     * 详情：
     * dbGroupId ：DB 数据库组id
     * redisGroupId ： redis 组id
     * swiftProjectId ：swift 租户id
     * mqVhostId ： 消息队列 vhost
     * neo4jInstanceId ：neo4j 实例id
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public CompanyDto getCompanyDetails(Integer id) {
        Company company = selectByPrimaryKey(id);
        CompanyDto companyDto = new CompanyDto();
        if (company != null) {
            BeanUtils.copyProperties(company, companyDto);
            companyDto.setTid(company.getId());
            // DB 数据库组
            getDbGroupDto(companyDto);

            // redisGroupId ： redis 组id
            getRedisGroupDto(companyDto);

            // swiftProjectId ：swift 租户id
            getSwiftProjectDto(companyDto);

            // mqVhostId ： 消息队列 vhost
            getMqVhostDto(companyDto);

            // neo4jInstanceId ：neo4j 实例id
            getNeo4jInstanceDto(companyDto);
        }
        return companyDto;
    }

    /**
     * neo4jInstanceId ：neo4j 实例id
     *
     * @param companyDto
     */
    private void getNeo4jInstanceDto(CompanyDto companyDto) {
        if (companyDto.getNeo4jInstanceId() != null && companyDto.getNeo4jInstanceId() > 0) {
            Neo4jInstance neo4jInstance = neo4jInstanceService.selectByPrimaryKey(companyDto.getNeo4jInstanceId());
            if (neo4jInstance != null) {
                Neo4jInstanceDto neo4jInstanceDto = new Neo4jInstanceDto();
                BeanUtils.copyProperties(neo4jInstance, neo4jInstanceDto);
                neo4jInstanceDto.setTid(neo4jInstance.getId());
                companyDto.setNeo4jInstanceDto(neo4jInstanceDto);
            }
        }
    }

    /**
     * mqVhostId ： 消息队列 vhost
     *
     * @param companyDto
     */
    private void getMqVhostDto(CompanyDto companyDto) {
        if (companyDto.getMqVhostId() != null && companyDto.getMqVhostId() > 0) {
            MqVhost mqVhost = mqVhostService.selectByPrimaryKey(companyDto.getMqVhostId());
            if (mqVhost != null) {
                MqVhostDto mqVhostDto = new MqVhostDto();
                BeanUtils.copyProperties(mqVhost, mqVhostDto);
                mqVhostDto.setTid(mqVhost.getId());
                companyDto.setMqVhostDto(mqVhostDto);
            }
        }
    }

    /**
     * swiftProjectId ：swift 租户id
     *
     * @param companyDto
     */
    private void getSwiftProjectDto(CompanyDto companyDto) {
        if (companyDto.getSwiftProjectId() != null && companyDto.getSwiftProjectId() > 0) {
            SwiftProject swiftProject = swiftProjectService.selectByPrimaryKey(companyDto.getSwiftProjectId());
            if (swiftProject != null) {
                SwiftProjectDto swiftProjectDto = new SwiftProjectDto();
                BeanUtils.copyProperties(swiftProject, swiftProjectDto);
                swiftProjectDto.setTid(swiftProject.getId());
                companyDto.setSwiftProjectDto(swiftProjectDto);
            }
        }
    }

    /**
     * redisGroupId ： redis 组id
     *
     * @param companyDto
     */
    private void getRedisGroupDto(CompanyDto companyDto) {
        if (companyDto.getRedisGroupId() != null && companyDto.getRedisGroupId() > 0) {
            RedisGroupDto redisGroupDto = redisGroupService.selectRedisGroupById(companyDto.getRedisGroupId());
            if (redisGroupDto != null) {
                companyDto.setRedisGroupDto(redisGroupDto);
            }
        }
    }

    /**
     * DB 数据库组
     *
     * @param companyDto
     */
    private void getDbGroupDto(CompanyDto companyDto) {
        if (companyDto.getDbGroupId() != null && companyDto.getDbGroupId() > 0) {
            DbGroupDto dbGroupDto = dbGroupService.selectDbGroupByCompanyId(companyDto.getDbGroupId());
            if (dbGroupDto != null) {
                companyDto.setDbGroupDto(dbGroupDto);
            }
        }
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public Company selectByPrimaryKey(Integer id) {
        return companyMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除校验
     *
     * @param id
     */
    private void deleteCompanyParameterCheck(Integer id) {
        if (id == null || id < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Company company = selectByPrimaryKey(id);
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        // TODO
    }

    /**
     * 更新参数校验
     *
     * @param companyDto
     * @return
     */
    private Company editCompanyParameterCheck(CompanyDto companyDto) {
        if (companyDto == null || companyDto.getTid() == null || companyDto.getTid() < 0 || StringUtils.isBlank(companyDto.getCompanyName())
                || StringUtils.isBlank(companyDto.getCompanyCode()) || StringUtils.isBlank(companyDto.getUpdateCycle()) || companyDto.getNextUpdateDate() == null || companyDto.getExpireDate() == null) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 不存在不能修改
        Company company = selectByPrimaryKey(companyDto.getTid());
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        // 如果 集团 id 不为空，查询是否存在，不存在不修改
        if (companyDto.getGroupId() != null && companyDto.getGroupId() > 0) {
            Groups group = groupService.selectByPrimaryKey(companyDto.getGroupId());
            if (group == null) {
                throw new ProvisioningException(BundleKey.GROUPS_NOT_EXIST, BundleKey.GROUPS_NOT_EXIST_MSG);
            }
        }
        // 除了自己以外是否有相同的数据，有不修改
        Company companySelf = companyMapper.selectCompanyInAdditionToItself(companyDto.getTid(), companyDto.getGroupId(), companyDto.getCompanyName(), companyDto.getCompanyCode());
        if (companySelf != null) {
            throw new ProvisioningException(BundleKey.COMPANY_ALREADY_EXIST, BundleKey.COMPANY_ALREADY_EXIST_MSG);
        }
        // 封装修改的参数
        buildUpdateParameter(company, companyDto);
        return company;
    }

    /**
     * 封装修改参数，目的是做每步都要修改数据库，方便
     *
     * @param company
     * @param companyDto
     */
    private void buildUpdateParameter(Company company, CompanyDto companyDto) {
        /** 集团id **/
        if (companyDto.getGroupId() != null && companyDto.getGroupId() > 0) {
            company.setGroupId(companyDto.getGroupId());
        }
        /** 公司名称 **/
        if (StringUtils.isNoneBlank(companyDto.getCompanyName())) {
            company.setCompanyName(companyDto.getCompanyName());
        }
        /** 公司Code **/
        if (StringUtils.isNoneBlank(companyDto.getCompanyCode())) {
            company.setCompanyCode(companyDto.getCompanyCode());
        }
        /** 更新周期 **/
        if (StringUtils.isNoneBlank(companyDto.getUpdateCycle())) {
            company.setUpdateCycle(companyDto.getUpdateCycle());
        }
        /** 下次更新日期 **/
        if (companyDto.getNextUpdateDate() != null) {
            company.setNextUpdateDate(companyDto.getNextUpdateDate());
        }
        /** 到期日 **/
        if (companyDto.getExpireDate() != null) {
            company.setExpireDate(companyDto.getExpireDate());
        }
        /** DB数据库组id **/
        if (companyDto.getDbGroupId() != null && companyDto.getDbGroupId() > 0) {
            company.setDbGroupId(companyDto.getDbGroupId());
        }
        /** redis组id **/
        if (companyDto.getRedisGroupId() != null && companyDto.getRedisGroupId() > 0) {
            company.setRedisGroupId(companyDto.getRedisGroupId());
        }
        /** swift租户id **/
        if (companyDto.getSwiftProjectId() != null && companyDto.getSwiftProjectId() > 0) {
            company.setSwiftProjectId(companyDto.getSwiftProjectId());
        }
        /** 消息队列vhost **/
        if (companyDto.getMqVhostId() != null && companyDto.getMqVhostId() > 0) {
            company.setMqVhostId(companyDto.getMqVhostId());
        }
        /** neo4j实例id **/
        if (companyDto.getNeo4jInstanceId() != null && companyDto.getNeo4jInstanceId() > 0) {
            company.setNeo4jInstanceId(companyDto.getNeo4jInstanceId());
        }
        /** 状态，0：创建中，1：创建完成,启用中， 2：停用 **/
        if (companyDto.getStatus() != null && companyDto.getStatus() > 0) {
            company.setStatus(companyDto.getStatus());
        }
    }

    /**
     * 添加参数校验
     *
     * @param companyDto
     */
    private void addCompanyParameterCheck(CompanyDto companyDto) {
        if (companyDto == null || StringUtils.isBlank(companyDto.getCompanyName()) || StringUtils.isBlank(companyDto.getCompanyCode())
                || StringUtils.isBlank(companyDto.getUpdateCycle()) || companyDto.getNextUpdateDate() == null || companyDto.getExpireDate() == null) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 如果是集团 ， 查询集团是否存在
        if (companyDto.getGroupId() != null && companyDto.getGroupId() > 0) {
            Groups group = groupService.selectByPrimaryKey(companyDto.getGroupId());
            if (group == null) {
                throw new ProvisioningException(BundleKey.GROUPS_NOT_EXIST, BundleKey.GROUPS_NOT_EXIST_MSG);
            }
        }
    }
}
