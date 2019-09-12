package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.UserManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.UserDto;
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
 * @Description ：用户
 * @Author ：jinjun_luo
 * @Date ： 2019/8/5 0005
 */
@RestController
@RequestMapping(value = "/user")
@Api(tags = "UserController | 用户")
public class UserController {

    @Autowired
    private UserManagerApiService userManagerApiService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param account
     * @param name
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "account", value = "用户账号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "用户名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> queryUserList(int pageNum, int pageSize, String account, String name) {
        com.yodoo.megalodon.permission.dto.UserDto userDto = new com.yodoo.megalodon.permission.dto.UserDto();
        userDto.setPageNum(pageNum);
        userDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(account)){
            userDto.setAccount(account);
        }
        if (StringUtils.isNotBlank(name)){
            userDto.setName(name);
        }
        PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto> pageInfoDto = userManagerApiService.queryUserList(userDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), UserController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, UserController.class, Arrays.asList("user_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(),"resetUserPassword", "updateUserStatus", "updateUserPassword");
        return new ProvisioningDto<PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加用户
     * @param userDto
     * @return
     */
    @ApiOperation(value = "添加用户", httpMethod = "POST")
    @ApiImplicitParam(name = "userDto", value = "用户 userDto", required = true, dataType = "UserDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> addUser(@RequestBody UserDto userDto){
        userManagerApiService.addUser(userDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改用户
     * @param userDto
     * @return
     */
    @ApiOperation(value = "修改用户", httpMethod = "PUT")
    @ApiImplicitParam(name = "userDto", value = "用户 userDto", required = true, dataType = "UserDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> editUser(@RequestBody UserDto userDto){
        userManagerApiService.editUser(userDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改用户密码
     * @param userDto
     * @return
     */
    @ApiOperation(value = "修改用户密码", httpMethod = "POST")
    @ApiImplicitParam(name = "userDto", value = "用户 userDto", required = true, dataType = "UserDto")
    @RequestMapping(value = "/updateUserPassword", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> updateUserPassword(@RequestBody UserDto userDto){
        userManagerApiService.updateUserPassword(userDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 重置用户密码
     * @param id
     * @return
     */
    @ApiOperation(value = "重置用户密码", httpMethod = "PUT")
    @ApiImplicitParam(name = "id", value = "用户表自增 id", required = true, dataType = "Integer", example = "10")
    @RequestMapping(value = "resetUserPassword/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> resetUserPassword(@PathVariable Integer id){
        userManagerApiService.resetUserPassword(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 启用和停用
     * @param id
     * @return
     */
    @ApiOperation(value = "启用和停用", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户表自增 id", required = true, dataType = "Integer", example = "10"),
            @ApiImplicitParam(name = "status", value = "状态，0：启用 1：停用", required = true, dataType = "Integer", example = "0")
    })
    @RequestMapping(value = "updateUserStatus/{id}/{status}", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> updateUserStatus(@PathVariable Integer id, @PathVariable Integer status){
        userManagerApiService.updateUserStatus(id, status);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}