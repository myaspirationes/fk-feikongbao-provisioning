package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Groups;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyMapper;
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
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private GroupsService groupService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    /**
     * 分页查询
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
     * @param companyDto
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer addCompany(CompanyDto companyDto) {
        // 校验
        addCompanyParameterCheck(companyDto);

        Company company = new Company();
        BeanUtils.copyProperties(companyDto,company);
        company.setStatus(CompanyStatusEnum.CREATING.getCode());
        return companyMapper.insertSelective(company);
    }

    /**
     * 更新
     * @param companyDto
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer editCompany(CompanyDto companyDto) {
        Company company = editCompanyParameterCheck(companyDto);
        return companyMapper.updateByPrimaryKeySelective(company);
    }

    /**
     * 删除 TODO
     * @param id
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public Integer deleteCompany(Integer id) {
        deleteCompanyParameterCheck(id);
        return companyMapper.deleteByPrimaryKey(id);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public CompanyDto getCompanyDetails(Integer id) {
        Company company = selectByPrimaryKey(id);
        CompanyDto companyDto = new CompanyDto();
        if (company != null){
            BeanUtils.copyProperties(company, companyDto);
            companyDto.setTid(company.getId());
        }
        return companyDto;
    }

    /**
     * 通过集团id 查询
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
     * 删除校验
     * @param id
     */
    private void deleteCompanyParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Company company = selectByPrimaryKey(id);
        if (company == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // TODO
    }

    /**
     * 更新参数校验
     * @param companyDto
     * @return
     */
    private Company editCompanyParameterCheck(CompanyDto companyDto) {
        if (companyDto == null || companyDto.getTid() == null || companyDto.getTid() < 0 || StringUtils.isBlank(companyDto.getCompanyName())
                || StringUtils.isBlank(companyDto.getCompanyCode()) || StringUtils.isBlank(companyDto.getUpdateCycle()) || companyDto.getNextUpdateDate() == null || companyDto.getExpireDate() == null){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 不存在不能修改
        Company company = selectByPrimaryKey(companyDto.getTid());
        if (company == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // 如果 集团 id 不为空，查询是否存在，不存在不修改
        if (companyDto.getGroupId() != null && companyDto.getGroupId() > 0){
            Groups group = groupService.selectByPrimaryKey(companyDto.getGroupId());
            if (group == null){
                throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
            }
        }
        // 除了自己以外是否有相同的数据，有不修改
        Company companySelf = companyMapper.selectCompanyInAdditionToItself(companyDto.getTid(),companyDto.getGroupId(), companyDto.getCompanyName(),companyDto.getCompanyCode());
        if (companySelf != null){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST_MEG);
        }
        BeanUtils.copyProperties(companyDto,company);
        return company;
    }


    /**
     * 通过主键查询
     * @param id
     * @return
     */
    private Company selectByPrimaryKey(Integer id) {
        return companyMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加参数校验
     * @param companyDto
     */
    private void addCompanyParameterCheck(CompanyDto companyDto) {
        if (companyDto == null ||  StringUtils.isBlank(companyDto.getCompanyName()) || StringUtils.isBlank(companyDto.getCompanyCode())
                || StringUtils.isBlank(companyDto.getUpdateCycle()) || companyDto.getNextUpdateDate() == null || companyDto.getExpireDate() == null){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 如果是集团 ， 查询集团是否存在
        if (companyDto.getGroupId() != null && companyDto.getGroupId() < 0){
            Groups group = groupService.selectByPrimaryKey(companyDto.getGroupId());
            if (group == null){
                throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
            }
        }
        // 查询是否有相同的数据，有不添加
        Company company = new Company();
        if (companyDto.getGroupId() != null && companyDto.getGroupId() < 0){
            company.setGroupId(companyDto.getGroupId());
        }
        company.setCompanyName(companyDto.getCompanyName());
        company.setCompanyCode(companyDto.getCompanyCode());
        Company company1 = companyMapper.selectOne(company);
        if (company1 != null){
            throw new ProvisioningException(BundleKey.DICTIONARY_EXIST, BundleKey.DICTIONARY_EXIST_MSG);
        }
    }
}
