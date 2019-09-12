package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyFunctionModuleDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyFunctionModuleService;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description ：公司功能模块关系
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
@RestController
@RequestMapping(value = "/companyFunctionModule")
@Api(tags = "CompanyFunctionModuleController | 公司功能模块关系表")
public class CompanyFunctionModuleController {

    @Autowired
    private CompanyFunctionModuleService companyFunctionModuleService;

    /**
     * 启用 和禁用功能模块: status 状态，0：启用，1：停用
     *
     * @param companyFunctionModuleDto
     * @return
     */
    @ApiOperation(value = "启用 和禁用功能模块: status 状态，0：启用，1：停用", httpMethod = "POST")
    @ApiImplicitParam(name = "companyFunctionModuleDto", value = "启用 或 禁用功能模块", required = true, dataType = "CompanyFunctionModuleDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> addOroEditCompanyFunctionModule(@RequestBody CompanyFunctionModuleDto companyFunctionModuleDto) {
        companyFunctionModuleService.addOroEditCompanyFunctionModule(companyFunctionModuleDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
