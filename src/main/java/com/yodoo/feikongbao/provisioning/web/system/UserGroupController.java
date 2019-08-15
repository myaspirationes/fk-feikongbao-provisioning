package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.UserManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
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
public class UserGroupController {

    @Autowired
    private UserManagerApiService userManagerApiService;

    /**
     * 条件分页查询
     *
     * @param userGroupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> queryUserGroupList(com.yodoo.megalodon.permission.dto.UserGroupDto userGroupDto) {
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
    @RequestMapping(value = "/userGroupBatchProcessing", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> userGroupBatchProcessing(Integer userGroupId){
        userManagerApiService.userGroupBatchProcessing(userGroupId);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}