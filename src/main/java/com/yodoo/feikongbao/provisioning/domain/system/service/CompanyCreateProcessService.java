package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyCreateProcessDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyCreateProcess;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.CompanyCreateProcessMapper;
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
 * @Description ：公司创建过程
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class CompanyCreateProcessService {

    @Autowired
    private CompanyCreateProcessMapper companyCreateProcessMapper;


    /**
     * 条件查询
     *
     * @param companyCreateProcessDto
     * @return
     */
    public PageInfoDto<CompanyCreateProcessDto> queryCompanyCreateProcessList(CompanyCreateProcessDto companyCreateProcessDto) {
        Example example = new Example(CompanyCreateProcess.class);
        Example.Criteria criteria = example.createCriteria();
        if (companyCreateProcessDto.getCompanyId() != null && companyCreateProcessDto.getCompanyId() > 0){
            criteria.andEqualTo("companyId", companyCreateProcessDto.getCompanyId());
        }
        if (companyCreateProcessDto.getProcessOrder() != null && companyCreateProcessDto.getProcessOrder() > 0){
            criteria.andEqualTo("processOrder", companyCreateProcessDto.getProcessOrder());
        }
        if (StringUtils.isNotBlank(companyCreateProcessDto.getProcessCode())){
            criteria.andEqualTo("processCode", companyCreateProcessDto.getProcessCode());

        }
        Page<?> pages = PageHelper.startPage(companyCreateProcessDto.getPageNum(), companyCreateProcessDto.getPageSize());
        List<CompanyCreateProcess> list = companyCreateProcessMapper.selectByExample(example);
        List<CompanyCreateProcessDto> collect = copyProperties(list);
        return new PageInfoDto<CompanyCreateProcessDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加公司创建步骤记录表
     */
    public void insertCompanyCreateProcess(Integer companyId, Integer processOrder, String processCode) {
        // 查询该步骤是否存在
        Example example = new Example(CompanyCreateProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("companyId", companyId);
        criteria.andEqualTo("processOrder", processOrder);
        criteria.andEqualTo("processCode", processCode);
        List<CompanyCreateProcess> list = companyCreateProcessMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            companyCreateProcessMapper.insertSelective((new CompanyCreateProcess(companyId, processOrder, processCode)));
        }
    }

    /**
     * 复制
     * @param companyCreateProcessList
     * @return
     */
    private List<CompanyCreateProcessDto> copyProperties(List<CompanyCreateProcess> companyCreateProcessList){
        if (!CollectionUtils.isEmpty(companyCreateProcessList)){
            return companyCreateProcessList.stream()
                    .filter(Objects::nonNull)
                    .map(companyCreateProcess -> {
                        return copyProperties(companyCreateProcess);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param companyCreateProcess
     * @return
     */
    private CompanyCreateProcessDto copyProperties(CompanyCreateProcess companyCreateProcess){
        if (companyCreateProcess != null){
            CompanyCreateProcessDto companyCreateProcessDto = new CompanyCreateProcessDto();
            BeanUtils.copyProperties(companyCreateProcess, companyCreateProcessDto);
            companyCreateProcessDto.setTid(companyCreateProcess.getId());
            return companyCreateProcessDto;
        }
        return null;
    }
}
