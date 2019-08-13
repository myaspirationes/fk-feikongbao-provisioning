package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.UserManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.UserDto;
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
public class UserController {

    @Autowired
    private UserManagerApiService userManagerApiService;

    /**
     * 条件分页查询
     *
     * @param userDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> queryUserList(com.yodoo.megalodon.permission.dto.UserDto userDto) {
        PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto> pageInfoDto = userManagerApiService.queryUserList(userDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), UserController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, UserController.class, Arrays.asList("user_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(),"resetUserPassword","updateUserStatus");
        return new ProvisioningDto<PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加用户
     * @param userDto
     * @return
     */
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
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> editUser(@RequestBody UserDto userDto){
        userManagerApiService.editUser(userDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 重置用户密码
     * @param id
     * @return
     */
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
    @RequestMapping(value = "updateUserStatus/{id}/{status}", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public ProvisioningDto<?> updateUserStatus(@PathVariable Integer id, @PathVariable Integer status){
        userManagerApiService.updateUserStatus(id, status);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}