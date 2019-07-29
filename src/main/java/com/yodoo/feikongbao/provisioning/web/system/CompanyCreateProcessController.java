package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyCreateProcessDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/companyCreateProcess")
public class CompanyCreateProcessController {

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    /**
     * 条件分页查询
     * @param companyCreateProcessDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> queryCompanyCreateProcessList(CompanyCreateProcessDto companyCreateProcessDto){
        PageInfoDto<CompanyCreateProcessDto> pageInfoDto = companyCreateProcessService.queryCompanyCreateProcessList(companyCreateProcessDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), CompanyCreateProcessController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, CompanyCreateProcessController.class, Arrays.asList("group_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(),OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<CompanyCreateProcessDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }
}
