package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.PermissionManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.PermissionGroupDto;
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
 * @Description ：权限组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/permissionGroup")
@Api(tags = "PermissionGroupController | 权限组")
public class PermissionGroupController {

    @Autowired
    private PermissionManagerApiService permissionManagerService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param groupCode
     * @param groupName
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "groupCode", value = "权限组code", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "groupName", value = "权限组名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> queryPermissionGroupList(int pageNum, int pageSize, String groupCode, String groupName) {
        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
        permissionGroupDto.setPageNum(pageNum);
        permissionGroupDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(groupCode)){
            permissionGroupDto.setGroupCode(groupCode);
        }
        if (StringUtils.isNotBlank(groupName)){
            permissionGroupDto.setGroupName(groupName);
        }
        PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto> pageInfoDto = permissionManagerService.queryPermissionGroupList(permissionGroupDto);

        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), PermissionGroupController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, PermissionGroupController.class, Arrays.asList("permission_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     *
     * @param permissionGroupDto
     * @return
     */
    @ApiOperation(value = "添加", httpMethod = "POST")
    @ApiImplicitParam(name = "permissionGroupDto", value = "权限组 permissionGroupDto", required = true, dataType = "PermissionGroupDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> addPermissionGroup(@RequestBody PermissionGroupDto permissionGroupDto) {
        permissionManagerService.addPermissionGroup(permissionGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     *
     * @param permissionGroupDto
     * @return
     */
    @ApiOperation(value = "更新", httpMethod = "PUT")
    @ApiImplicitParam(name = "permissionGroupDto", value = "权限组 permissionGroupDto", required = true, dataType = "PermissionGroupDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> editPermissionGroup(@RequestBody PermissionGroupDto permissionGroupDto) {
        permissionManagerService.editPermissionGroup(permissionGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "权限组数据库表自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> deletePermissionGroup(@PathVariable Integer id) {
        permissionManagerService.deletePermissionGroup(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "权限组数据库表自增 id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getPermissionGroupDetails(@PathVariable Integer id) {
        PermissionGroupDto permissionGroupDetails = permissionManagerService.getPermissionGroupDetails(id);
        return new ProvisioningDto<PermissionGroupDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, permissionGroupDetails);
    }
}
