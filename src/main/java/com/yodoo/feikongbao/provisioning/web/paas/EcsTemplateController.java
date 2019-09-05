package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.EcsTemplateService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
@RestController
@RequestMapping(value = "/ecsTemplate")
public class EcsTemplateController {

    @Autowired
    private EcsTemplateService ecsTemplateService;

    /**
     * 条件分页查询
     *
     * @param ecsInstanceDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> queryEcsTemplateList(EcsTemplateDto ecsInstanceDto) {
        PageInfoDto<EcsTemplateDto> pageInfoDto = ecsTemplateService.queryEcsTemplateList(ecsInstanceDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), EcsTemplateController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, EcsTemplateController.class, Arrays.asList("company_manage","company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<EcsTemplateDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @param ecsInstanceDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> addEcsTemplate(@RequestBody EcsTemplateDto ecsInstanceDto){
        ecsTemplateService.addEcsTemplate(ecsInstanceDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改
     * @param ecsInstanceDto
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> editEcsTemplate(@RequestBody EcsTemplateDto ecsInstanceDto){
        ecsTemplateService.editEcsTemplate(ecsInstanceDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> deleteEcsTemplate(@PathVariable Integer id){
        ecsTemplateService.deleteEcsTemplate(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
