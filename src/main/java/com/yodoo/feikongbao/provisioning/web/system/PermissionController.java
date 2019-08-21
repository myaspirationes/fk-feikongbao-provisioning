package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.GroupsDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.PermissionManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.*;
import com.yodoo.megalodon.permission.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description ：权限管理
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionController {

    @Autowired
    private PermissionManagerApiService permissionManagerService;

    /**
     * 条件分页查询权限列表
     * @param permissionDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> queryPermissionList(PermissionDto permissionDto){
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionDto> pageInfoDto = permissionManagerService.queryPermissionList(permissionDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), PermissionController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, PermissionController.class, Arrays.asList("permission_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(), "getPermissionByUserId",
                "actionPermissionInUserList", "getTargetGroupsByUserId", "getAvailableGroupsByUserId", "updateUserPermissionTargetGroups",
                "getAvailableTargetCompanyByUserId", "updateUserPermissionTargetCompany", "selectUserManageTargetUserListByUserId",
                "getAvailableTargetUserListByUserId","updateUserPermissionTargetUser");
        return new ProvisioningDto<com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加权限
     * @param permissionDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> addPermission(@RequestBody PermissionDto permissionDto) {
        permissionManagerService.addPermission(permissionDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改权限
     * @param permissionDto
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> updatePermission(@RequestBody PermissionDto permissionDto) {
        permissionManagerService.updatePermission(permissionDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> deletePermission(@PathVariable Integer id){
        permissionManagerService.deletePermission(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 根据userId查询权限
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getPermissionByUserId/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getPermissionByUserId(@PathVariable Integer userId) {
        List<Permission> permissionByUserId = permissionManagerService.getPermissionByUserId(userId);
        return new ProvisioningDto<List<Permission>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, permissionByUserId);
    }

    /**
     * 在用户列表中点击权限,获取目标集团、目标公司、目标用户的list
     * @param userId
     */
    @RequestMapping(value = "/actionPermissionInUserList/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> actionPermissionInUserList(@PathVariable Integer userId){
        ActionPermissionInUserListDto actionPermissionInUserListDto = permissionManagerService.actionPermissionInUserList(userId);
        return new ProvisioningDto<ActionPermissionInUserListDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, actionPermissionInUserListDto);
    }

    /**
     * 根据用户id查询已管理的目标集团
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getTargetGroupsByUserId/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getTargetGroupsByUserId(@PathVariable Integer userId){
        List<GroupsDto> targetGroupsByUserId = permissionManagerService.getTargetGroupsByUserId(userId);
        return new ProvisioningDto<List<GroupsDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, targetGroupsByUserId);
    }

    /**
     * 通过用户id查询可管理的目标集团
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getAvailableGroupsByUserId/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getAvailableGroupsByUserId(@PathVariable Integer userId){
        List<GroupsDto> availableGroupsByUserId = permissionManagerService.getAvailableGroupsByUserId(userId);
        return new ProvisioningDto<List<GroupsDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, availableGroupsByUserId);
    }

    /**
     * 变更用户管理目标集团
     * @param userPermissionTargetDto
     */
    @RequestMapping(value = "/updateUserPermissionTargetGroups", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> updateUserPermissionTargetGroups(@RequestBody UserPermissionTargetDto userPermissionTargetDto){
        permissionManagerService.updateUserPermissionTargetGroups(userPermissionTargetDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 通过用户id查询已管理的目标公司
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserManageTargetCompanyListByUserId/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getUserManageTargetCompanyListByUserId(@PathVariable Integer userId){
        List<CompanyDto> targetCompanyList = permissionManagerService.getUserManageTargetCompanyListByUserId(userId);
        return new ProvisioningDto<List<CompanyDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG,targetCompanyList);
    }

    /**
     * 通过用户id查询可管理的目标公司
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getAvailableTargetCompanyByUserId/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getAvailableUserManageTargetCompanyListByUserId(@PathVariable Integer userId){
        List<CompanyDto> targetCompanyList = permissionManagerService.getAvailableUserManageTargetCompanyListByUserId(userId);
        return new ProvisioningDto<List<CompanyDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG,targetCompanyList);

    }

    /**
     * 更新用户管理目标公司数据
     * @param userPermissionTargetDto
     */
    @RequestMapping(value = "/updateUserPermissionTargetCompany", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> updateUserPermissionTargetCompany(@RequestBody UserPermissionTargetDto userPermissionTargetDto){
        permissionManagerService.updateUserPermissionTargetCompany(userPermissionTargetDto);
        return new ProvisioningDto<List<CompanyDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }


    /**
     * 通过用户id查询已管理的目标用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/selectUserManageTargetUserListByUserId/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> selectUserManageTargetUserListByUserId(@PathVariable Integer userId){
        List<UserDto> targetUserList = permissionManagerService.selectUserManageTargetUserListByUserId(userId);
        return new ProvisioningDto<List<UserDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, targetUserList);
    }

    /**
     * 通过用户id查询可管理的目标用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getAvailableTargetUserListByUserId/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getAvailableUserManageTargetUserListByUserId(@PathVariable Integer userId){
        List<UserDto> targetUserList = permissionManagerService.getAvailableUserManageTargetUserListByUserId(userId);
        return new ProvisioningDto<List<UserDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, targetUserList);
    }

    /**
     * 更新用户管理目标用户数据
     * @param userPermissionTargetDto
     */
    @RequestMapping(value = "/updateUserPermissionTargetUser", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> updateUserPermissionTargetUser(@RequestBody UserPermissionTargetDto userPermissionTargetDto){
        permissionManagerService.updateUserPermissionTargetUser(userPermissionTargetDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
