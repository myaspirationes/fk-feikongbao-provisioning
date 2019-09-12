package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.EcsTemplateService;
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
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
@RestController
@RequestMapping(value = "/ecsTemplate")
@Api(tags = "EcsTemplateController | ecs 模板类型")
public class EcsTemplateController {

    @Autowired
    private EcsTemplateService ecsTemplateService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param ecsType
     * @param name
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "ecsType", value = "ecs 类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "ecs 类型名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> queryEcsTemplateList(int pageNum, int pageSize, String ecsType, String name) {
        EcsTemplateDto ecsInstanceDto = new EcsTemplateDto();
        ecsInstanceDto.setPageNum(pageNum);
        ecsInstanceDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(ecsType)){
            ecsInstanceDto.setEcsType(ecsType);
        }
        if (StringUtils.isNotBlank(name)){
            ecsInstanceDto.setName(name);
        }
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
    @ApiOperation(value = "添加 ecs 模板", httpMethod = "POST")
    @ApiImplicitParam(name = "ecsInstanceDto", value = "ecs 模板 ecsInstanceDto", required = true, dataType = "EcsTemplateDto")
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
    @ApiOperation(value = "修改 ecs 模板", httpMethod = "PUT")
    @ApiImplicitParam(name = "ecsInstanceDto", value = "ecs 模板 ecsInstanceDto", required = true, dataType = "EcsTemplateDto")
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
    @ApiOperation(value = "删除 ecs 模板", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "ecs 模板自增 id", paramType="path",required = true, dataType = "integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage','company_manage')")
    public ProvisioningDto<?> deleteEcsTemplate(@PathVariable Integer id){
        ecsTemplateService.deleteEcsTemplate(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
