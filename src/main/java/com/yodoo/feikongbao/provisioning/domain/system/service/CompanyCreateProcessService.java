package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyCreateProcessDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyCreateProcess;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyCreateProcessMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Description ：公司创建过程
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class CompanyCreateProcessService {

    @Autowired
   private CompanyCreateProcessMapper companyCreateProcessMapper;


    /**
     * 条件查询
     * @param companyCreateProcessDto
     * @return
     */
    public PageInfoDto<CompanyCreateProcessDto> queryCompanyCreateProcessList(CompanyCreateProcessDto companyCreateProcessDto) {
        CompanyCreateProcess companyCreateProcess = new CompanyCreateProcess();
        BeanUtils.copyProperties(companyCreateProcessDto, companyCreateProcess);
        Page<?> pages = PageHelper.startPage(companyCreateProcessDto.getPageNum(), companyCreateProcessDto.getPageSize());
        List<CompanyCreateProcess> select = companyCreateProcessMapper.select(companyCreateProcess);
        List<CompanyCreateProcessDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(select)){
            collect = select.stream()
                    .filter(Objects::nonNull)
                    .map(companyCreateProcess1 -> {
                        CompanyCreateProcessDto dto = new CompanyCreateProcessDto();
                        BeanUtils.copyProperties(companyCreateProcess1, dto);
                        dto.setTid(companyCreateProcess1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<CompanyCreateProcessDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加公司创建步骤记录表
     * @param companyCreateProcess
     */
    public void insertCompanyCreateProcess(CompanyCreateProcess companyCreateProcess) {
        companyCreateProcessMapper.insertSelective(companyCreateProcess);
    }

    /**
     * 添加公司创建步骤记录表
     */
    public void insertCompanyCreateProcess(Integer companyId, Integer processOrder, String processCode) {
        CompanyCreateProcess companyCreateProcess = new CompanyCreateProcess();
        companyCreateProcess.setCompanyId(companyId);
        companyCreateProcess.setProcessOrder(processOrder);
        companyCreateProcess.setProcessCode(processCode);
        this.insertCompanyCreateProcess(companyCreateProcess);
    }
}
