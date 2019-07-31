package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.FunctionModuleDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.FunctionModuleService;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description ：功能模块
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
@RestController
@RequestMapping(value = "/functionModule")
public class FunctionModuleController {

    @Autowired
    private FunctionModuleService functionModuleService;

    /**
     * 添加:
     * @param functionModuleDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> addFunctionModule(@RequestBody FunctionModuleDto functionModuleDto){
        functionModuleService.addFunctionModule(functionModuleDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改:
     * @param functionModuleDto
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> editFunctionModule(@RequestBody FunctionModuleDto functionModuleDto){
        functionModuleService.editFunctionModule(functionModuleDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
