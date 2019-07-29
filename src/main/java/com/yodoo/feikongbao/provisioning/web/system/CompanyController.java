package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：公司管理
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 条件分页查询
     * @param companyDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryCompanyList(CompanyDto companyDto){
        PageInfoDto<CompanyDto> pageInfoDto = companyService.queryCompanyList(companyDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), CompanyController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, CompanyController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<CompanyDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加:
     * @param companyDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> addCompany(@RequestBody CompanyDto companyDto){
        companyService.addCompany(companyDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     * @param companyDto
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> editCompany(@RequestBody CompanyDto companyDto){
        companyService.editCompany(companyDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除 TODO
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> deleteCompany(@PathVariable Integer id){
        companyService.deleteCompany(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getCompanyDetails(@PathVariable Integer id){
        CompanyDto  companyDto = companyService.getCompanyDetails(id);
        return new ProvisioningDto<CompanyDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, companyDto);
    }
}
