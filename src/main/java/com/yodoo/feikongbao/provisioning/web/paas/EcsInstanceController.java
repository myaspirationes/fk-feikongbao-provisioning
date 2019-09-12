package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.aliyun.service.EcsInstanceService;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsInstanceDto;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@Api(tags = "EcsInstanceController | ecs 创建和失放")
public class EcsInstanceController {

    @Autowired
    private EcsInstanceService ecsInstanceService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param instanceId
     * @param ecsType
     * @param instanceName
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "instanceId", value = "ecs实例id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ecsType", value = "实例类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "instanceName", value = "实例名称", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> queryEcsInstanceList(int pageNum, int pageSize, String instanceId, String ecsType, String instanceName) {
        EcsInstanceDto ecsInstanceDto = new EcsInstanceDto();
        ecsInstanceDto.setPageNum(pageNum);
        ecsInstanceDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(instanceId)){
            ecsInstanceDto.setInstanceId(instanceId);
        }
        if (StringUtils.isNotBlank(ecsType)){
            ecsInstanceDto.setEcsType(ecsType);
        }
        if (StringUtils.isNotBlank(instanceName)){
            ecsInstanceDto.setInstanceName(instanceName);
        }
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
     * @param ecsType : companyCode, ecsType
     * @return
     */
    @ApiOperation(value = "创建 ecs", httpMethod = "POST")
    @ApiImplicitParam(name = "ecsType", value = "ecs 类型", required = true, dataType = "String", example = "test")
    @RequestMapping(value = "/{ecsType}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> createRunInstance(@RequestBody String ecsType){
        ecsInstanceService.createRunInstance(ecsType);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * : 释放虚拟机
     * @param ecsInstanceId
     * @return
     */
    @ApiOperation(value = "释放 ecs", httpMethod = "DELETE")
    @ApiImplicitParam(name = "ecsInstanceId", value = "虚拟机 ecs id", paramType="path",required = true, dataType = "String", example = "test")
    @RequestMapping(value = "/{ecsInstanceId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> deleteRunInstance(@PathVariable String ecsInstanceId){
        ecsInstanceService.deleteRunInstance(ecsInstanceId);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
