package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.SwiftProjectDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.SwiftProjectService;
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
 * @Description ：存储
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/swiftProject")
@Api(tags = "SwiftProjectController | 存储 swift")
public class SwiftProjectController {

    @Autowired
    private SwiftProjectService swiftProjectService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param projectName
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "projectName", value = "swift 名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> querySwiftProjectList(int pageNum, int pageSize, String projectName) {
        SwiftProjectDto swiftProjectDto = new SwiftProjectDto();
        swiftProjectDto.setPageNum(pageNum);
        swiftProjectDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(projectName)){
            swiftProjectDto.setProjectName(projectName);
        }
        PageInfoDto<SwiftProjectDto> pageInfoDto = swiftProjectService.querySwiftProjectList(swiftProjectDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), SwiftProjectController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, SwiftProjectController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<SwiftProjectDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询详情", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "swift数据库自增id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getSwiftProjectDetails(@PathVariable Integer id) {
        SwiftProjectDto swiftProjectDto = swiftProjectService.getSwiftProjectDetails(id);
        return new ProvisioningDto<SwiftProjectDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, swiftProjectDto);
    }

    /**
     * 创建存储对象
     *
     * @param swiftProjectDto
     * @return
     */
    @ApiOperation(value = "创建存储对象", hidden = true)
    @RequestMapping(value = "/useSwiftProject", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useSwiftProject(@RequestBody SwiftProjectDto swiftProjectDto) {
        SwiftProjectDto swiftProjectDtoResponse = swiftProjectService.useSwiftProject(swiftProjectDto);
        return new ProvisioningDto<SwiftProjectDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, swiftProjectDtoResponse);
    }


}
