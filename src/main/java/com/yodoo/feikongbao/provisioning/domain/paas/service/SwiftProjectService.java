package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.feikongbao.storageclient.api.StorageManagerApi;
import com.feikongbao.storageclient.entity.ProjectEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.SwiftProjectDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.SwiftProject;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.SwiftProjectMapper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyCreateProcess;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
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
 * @Description ：存储
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class SwiftProjectService {

    @Autowired
    private SwiftProjectMapper swiftProjectMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    @Autowired
    private StorageManagerApi storageManagerApi;

    /**
     * 条件分页查询
     * @param swiftProjectDto
     * @return
     */
    public PageInfoDto<SwiftProjectDto> querySwiftProjectList(SwiftProjectDto swiftProjectDto) {
        SwiftProject swiftProject = new SwiftProject();
        if (swiftProjectDto != null) {
            BeanUtils.copyProperties(swiftProjectDto, swiftProject);
        }
        Page<?> pages = PageHelper.startPage(swiftProjectDto.getPageNum(), swiftProjectDto.getPageSize());
        List<SwiftProject> list = swiftProjectMapper.select(swiftProject);
        List<SwiftProjectDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(swiftProject1 -> {
                        SwiftProjectDto dto = new SwiftProjectDto();
                        BeanUtils.copyProperties(swiftProject1, dto);
                        dto.setTid(swiftProject1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<SwiftProjectDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public SwiftProjectDto getSwiftProjectDetails(Integer id) {
        SwiftProject swiftProject = selectByPrimaryKey(id);
        SwiftProjectDto swiftProjectDto = new SwiftProjectDto();
        if (swiftProject != null){
            BeanUtils.copyProperties(swiftProject, swiftProjectDto);
            swiftProjectDto.setTid(swiftProject.getId());
        }
        return swiftProjectDto;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public SwiftProject selectByPrimaryKey(Integer id){
        return swiftProjectMapper.selectByPrimaryKey(id);
    }

    /**
     * 使用存储对象
     * @param swiftProjectDto
     * @return
     */
    public SwiftProjectDto useSwiftProject(SwiftProjectDto swiftProjectDto) {
        useSwiftProjectParameterCheck(swiftProjectDto);
        // 创建租户
        ProjectEntity project = storageManagerApi.createProject(new ProjectEntity(swiftProjectDto.getCompanyCode()));
        // 更新公司表
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(swiftProjectDto.getTid());
        //companyDto.setSwiftProjectId(swiftProject.getId());
        companyService.updateCompany(companyDto);

        // 添加创建公司流程记录表
        companyCreateProcessService.insertCompanyCreateProcess(new CompanyCreateProcess(swiftProjectDto.getCompanyId(),
                CompanyCreationStepsEnum.FOUR_STEP.getCode(), CompanyCreationStepsEnum.FOUR_STEP.getName()));


        return swiftProjectDto;
    }

    /**
     * 使用存储对象参数校验
     * @param swiftProjectDto
     */
    private void useSwiftProjectParameterCheck(SwiftProjectDto swiftProjectDto) {
        if (swiftProjectDto == null || swiftProjectDto.getCompanyId() == null || swiftProjectDto.getCompanyId() < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询公司数据是否存在
        Company company = companyService.selectByPrimaryKey(swiftProjectDto.getCompanyId());
        if (company == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        swiftProjectDto.setCompanyCode(company.getCompanyCode());
    }
}
