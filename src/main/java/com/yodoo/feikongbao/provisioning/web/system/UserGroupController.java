package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.UserManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description ：用户组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
@RestController
@RequestMapping(value = "/userGroup")
@Api(tags = "UserGroupController | 用户组")
public class UserGroupController {

    @Autowired
    private UserManagerApiService userManagerApiService;

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
            @ApiImplicitParam(name = "groupCode", value = "用户组 code", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "groupName", value = "用户组名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> queryUserGroupList(int pageNum, int pageSize, String groupCode, String groupName) {
        com.yodoo.megalodon.permission.dto.UserGroupDto userGroupDto = new com.yodoo.megalodon.permission.dto.UserGroupDto();
        userGroupDto.setPageNum(pageNum);
        userGroupDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(groupCode)){
            userGroupDto.setGroupCode(groupCode);
        }
        if (StringUtils.isNotBlank(groupName)){
            userGroupDto.setGroupName(groupName);
        }
        PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserGroupDto> pageInfoDto = userManagerApiService.queryUserGroupList(userGroupDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), UserGroupController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, UserGroupController.class, Arrays.asList("user_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(), "getUserGroupAll","userGroupBatchProcessing");
        return new ProvisioningDto<PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserGroupDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加用户组
     * @param userGroupDto
     * @return
     */
    @ApiOperation(value = "添加用户组", httpMethod = "POST")
    @ApiImplicitParam(name = "userGroupDto", value = "用户组 userGroupDto", required = true, dataType = "UserGroupDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> addUserGroup(@RequestBody UserGroupDto userGroupDto){
        userManagerApiService.addUserGroup(userGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改用户组
     * @param userGroupDto
     * @return
     */
    @ApiOperation(value = "修改用户组", httpMethod = "PUT")
    @ApiImplicitParam(name = "userGroupDto", value = "用户组 userGroupDto", required = true, dataType = "UserGroupDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> editUserGroup(@RequestBody UserGroupDto userGroupDto){
        userManagerApiService.editUserGroup(userGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除用户组
     * @param id
     * @return
     */
    @ApiOperation(value = "删除用户组", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "用户组表自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> deleteUserGroup(@PathVariable Integer id){
        userManagerApiService.deleteUserGroup(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询所有用户组
     * @return
     */
    @ApiOperation(value = "查询所有用户组", httpMethod = "POST")
    @RequestMapping(value = "/getUserGroupAll", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> getUserGroupAll(){
        List<UserGroupDto> userGroupAll = userManagerApiService.getUserGroupAll();
        return new ProvisioningDto<List<UserGroupDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, userGroupAll);
    }

    /**
     * 用户组批处理
     * @param userGroupId
     */
    @ApiOperation(value = "用户组批处理", httpMethod = "POST")
    @ApiImplicitParam(name = "userGroupId", value = "用户组表自增 userGroupId", required = false, dataType = "Integer", example = "10")
    @RequestMapping(value = "/userGroupBatchProcessing", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> userGroupBatchProcessing(Integer userGroupId){
        userManagerApiService.userGroupBatchProcessing(userGroupId);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}