package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.FunctionModuleDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.FunctionModuleService;
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
 * @Description ：功能模块
 * @Author ：jinjun_luo
 * @Date ： 2019/7/31 0031
 */
@RestController
@RequestMapping(value = "/functionModule")
@Api(tags = "FunctionModuleController | 功能模块")
public class FunctionModuleController {

    @Autowired
    private FunctionModuleService functionModuleService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public ProvisioningDto<?> queryFunctionModuleList(int pageNum, int pageSize) {
        FunctionModuleDto functionModuleDto = new FunctionModuleDto();
        functionModuleDto.setPageNum(pageNum);
        functionModuleDto.setPageSize(pageSize);
        PageInfoDto<FunctionModuleDto> pageInfoDto = functionModuleService.queryDictionaryList(functionModuleDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), FunctionModuleController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, FunctionModuleController.class, Arrays.asList("dictionary_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<FunctionModuleDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加:
     *
     * @param functionModuleDto
     * @return
     */
    @ApiOperation(value = "添加", httpMethod = "POST")
    @ApiImplicitParam(name = "functionModuleDto", value = "功能模块 functionModuleDto", required = true, dataType = "FunctionModuleDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> addFunctionModule(@RequestBody FunctionModuleDto functionModuleDto) {
        functionModuleService.addFunctionModule(functionModuleDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改:
     *
     * @param functionModuleDto
     * @return
     */
    @ApiOperation(value = "修改", httpMethod = "PUT")
    @ApiImplicitParam(name = "functionModuleDto", value = "功能模块 functionModuleDto", required = true, dataType = "FunctionModuleDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> editFunctionModule(@RequestBody FunctionModuleDto functionModuleDto) {
        functionModuleService.editFunctionModule(functionModuleDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", httpMethod = "PUT")
    @ApiImplicitParam(name = "id", value = "功能模块数据库表自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> deleteFunctionModule(@PathVariable Integer id) {
        functionModuleService.deleteFunctionModule(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
