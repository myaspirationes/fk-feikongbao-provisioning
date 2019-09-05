package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.aliyun.service.EcsInstanceService;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsInstanceDto;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：创建 java ECS
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
@RestController
@RequestMapping(value = "/ecsInstance")
public class EcsInstanceController {

    @Autowired
    private EcsInstanceService ecsInstanceService;

    /**
     * 条件分页查询
     *
     * @param ecsInstanceDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> queryEcsInstanceList(EcsInstanceDto ecsInstanceDto) {
        PageInfoDto<EcsInstanceDto> pageInfoDto = ecsInstanceService.queryEcsInstanceList(ecsInstanceDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), EcsInstanceController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, EcsInstanceController.class, Arrays.asList("company_manage","company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<EcsInstanceDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 创建: java ecs
     * @param ecsInstanceDto : companyCode, ecsType
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> createRunInstance(@RequestBody EcsInstanceDto ecsInstanceDto){
        ecsInstanceService.createRunInstance(ecsInstanceDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * : 释放虚拟机
     * @param ecsInstanceId
     * @return
     */
    @RequestMapping(value = "/{ecsInstanceId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> deleteRunInstance(@PathVariable String ecsInstanceId){
        ecsInstanceService.deleteRunInstance(ecsInstanceId);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
