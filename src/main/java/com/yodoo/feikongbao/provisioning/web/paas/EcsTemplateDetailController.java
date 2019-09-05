package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDetailDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.EcsTemplateDetailService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：ecs 模板详情
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
@RestController
@RequestMapping(value = "/ecsTemplateDetail")
public class EcsTemplateDetailController {

    @Autowired
    private EcsTemplateDetailService ecsTemplateDetailService;

    /**
     * 条件分页查询
     *
     * @param ecsTemplateDetailDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> queryEcsTemplateDetailList(EcsTemplateDetailDto ecsTemplateDetailDto) {
        PageInfoDto<EcsTemplateDetailDto> pageInfoDto = ecsTemplateDetailService.queryEcsTemplateDetailList(ecsTemplateDetailDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), EcsTemplateDetailController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, EcsTemplateDetailController.class, Arrays.asList("company_manage","company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<EcsTemplateDetailDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @param ecsTemplateDetailDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> addEcsTemplateDetail(@RequestBody EcsTemplateDetailDto ecsTemplateDetailDto){
        ecsTemplateDetailService.addEcsTemplateDetail(ecsTemplateDetailDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改
     * @param ecsTemplateDetailDto
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> editEcsTemplateDetail(@RequestBody EcsTemplateDetailDto ecsTemplateDetailDto){
        ecsTemplateDetailService.editEcsTemplateDetail(ecsTemplateDetailDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> deleteEcsTemplateDetail(@PathVariable Integer id){
        ecsTemplateDetailService.deleteEcsTemplateDetail(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
