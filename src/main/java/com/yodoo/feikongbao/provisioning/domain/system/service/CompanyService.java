package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyMapper;
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
@Transactional(transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 分页查询
     * @Author houzhen
     * @Date 13:21 2019/7/29
    **/
    @PreAuthorize("hasAnyAuthority('company')")
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
                dtoList.add(dto);
            });
        }
        return new PageInfoDto<CompanyDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), dtoList);
    }
}
