package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDetailDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.EcsTemplateDetailService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "EcsTemplateDetailController | ecs 模板详情")
public class EcsTemplateDetailController {

    @Autowired
    private EcsTemplateDetailService ecsTemplateDetailService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param templateId
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "templateId", value = "ecs 类型", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> queryEcsTemplateDetailList(int pageNum, int pageSize, Integer templateId) {
        EcsTemplateDetailDto ecsTemplateDetailDto = new EcsTemplateDetailDto();
        ecsTemplateDetailDto.setPageNum(pageNum);
        ecsTemplateDetailDto.setPageSize(pageSize);
        if (templateId != null){
            ecsTemplateDetailDto.setTemplateId(templateId);
        }
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
    @ApiOperation(value = "添加 ecs 模板详情", httpMethod = "POST")
    @ApiImplicitParam(name = "ecsTemplateDetailDto", value = "ecs 模板详情 ecsTemplateDetailDto", required = true, dataType = "EcsTemplateDetailDto")
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
    @ApiOperation(value = "修改 ecs 模板详情", httpMethod = "PUT")
    @ApiImplicitParam(name = "ecsTemplateDetailDto", value = "ecs 模板详情 ecsTemplateDetailDto", required = true, dataType = "EcsTemplateDetailDto")
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
    @ApiOperation(value = "删除 ecs 模板详情", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "ecs 模板详情自增 id", paramType="path",required = true, dataType = "integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> deleteEcsTemplateDetail(@PathVariable Integer id){
        ecsTemplateDetailService.deleteEcsTemplateDetail(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
