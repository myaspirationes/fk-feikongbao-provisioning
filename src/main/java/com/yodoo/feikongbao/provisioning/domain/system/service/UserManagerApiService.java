package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.megalodon.permission.api.UserManagerApi;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.dto.UserDto;
import com.yodoo.megalodon.permission.dto.UserGroupDto;
import com.yodoo.megalodon.permission.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：用户管理
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
@Service
public class UserManagerApiService {

    @Autowired
    private UserManagerApi userManagerApi;

    /**
     *  条件分页查询用户列表:
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto> queryUserList(UserDto userDto){
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto> pageInfoDto = new com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<>();

        PageInfoDto<UserDto> userDtoPageInfoDto = userManagerApi.queryUserList(userDto);
        if (userDtoPageInfoDto != null){
            BeanUtils.copyProperties(userDtoPageInfoDto, pageInfoDto);
        }
        if (userDtoPageInfoDto != null && !CollectionUtils.isEmpty(userDtoPageInfoDto.getList())){
            List<com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto> collect = userDtoPageInfoDto.getList().stream()
                    .filter(Objects::nonNull)
                    .map(permissionUserDto -> {
                        com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto permissionUserDtoResponse = new com.yodoo.feikongbao.provisioning.domain.system.dto.UserDto();
                        BeanUtils.copyProperties(permissionUserDto, permissionUserDtoResponse);
                        permissionUserDtoResponse.setTid(permissionUserDto.getId());
                        return permissionUserDtoResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            pageInfoDto.setList(collect);;
        }
        return pageInfoDto;
    }

    /**
     * 添加用户：
     * 1、添加用户可以选择用户所属的用户组：Set<Integer> userGroupIds
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUser(UserDto userDto){
        return userManagerApi.addUser(userDto);
    }

    /**
     * 更新用户：
     * 1、更新用户可以更新用户所属的用户组：Set<Integer> userGroupIds
     *
     * @param userDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer editUser(UserDto userDto){
        return userManagerApi.editUser(userDto);
    }

    /**
     * 重置密码：yodoo123
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer resetUserPassword(Integer userId){
        return userManagerApi.resetUserPassword(userId);
    }

    /**
     * 启用和停用:
     * @param userId
     * @param status ：0：启用 1：停用
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer updateUserStatus(Integer userId, Integer status){
        return userManagerApi.updateUserStatus(userId, status);
    }

    /**
     * 通过账号查询
     * @param account
     * @return
     */
    public User getUserByAccount(String account){
        return userManagerApi.getUserByAccount(account);
    }

    /**
     * 条件分页查询用户组
     * @param userGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserGroupDto> queryUserGroupList(UserGroupDto userGroupDto){
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.UserGroupDto> pageInfoDto = new com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<>();

        PageInfoDto<UserGroupDto> userGroupDtoPageInfoDto = userManagerApi.queryUserGroupList(userGroupDto);
        if (userGroupDtoPageInfoDto != null){
            BeanUtils.copyProperties(pageInfoDto, userGroupDtoPageInfoDto);
        }
        if (userGroupDtoPageInfoDto != null && !CollectionUtils.isEmpty(userGroupDtoPageInfoDto.getList())){
            List<com.yodoo.megalodon.permission.dto.UserGroupDto> list = userGroupDtoPageInfoDto.getList();
            List<com.yodoo.feikongbao.provisioning.domain.system.dto.UserGroupDto> collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(permissionUserGroupDto -> {
                        com.yodoo.feikongbao.provisioning.domain.system.dto.UserGroupDto userGroupDtoResponse = new com.yodoo.feikongbao.provisioning.domain.system.dto.UserGroupDto();
                        BeanUtils.copyProperties(permissionUserGroupDto, userGroupDtoResponse);
                        userGroupDtoResponse.setTid(permissionUserGroupDto.getId());
                        return userGroupDtoResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            pageInfoDto.setList(collect);
        }
        return pageInfoDto;
    }

    /**
     * 添加用户组:
     * 1、用户组权限表
     * 2、用户管理用户组权限表
     * 3、用户组条件表
     * 4、用户管理用户组权限表
     *
     * @param userGroupDto
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer addUserGroup(UserGroupDto userGroupDto){
        return userManagerApi.addUserGroup(userGroupDto);
    }

    /**
     * 更新用户组：
     * 1、用户组权限表
     * 2、用户管理用户组权限表
     * 3、用户组条件表
     * 4、用户管理用户组权限表
     * @param userGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer editUserGroup(UserGroupDto userGroupDto) {
        return userManagerApi.editUserGroup(userGroupDto);
    }

    /**
     * 删除用户组
     * 1、删除用户组条件
     * 2、删除用户权限
     * 3、用户管理用户组权限表
     * 4、用户组权限组关系
     * 5、删除用户组
     * @param userGroupId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public Integer deleteUserGroup(Integer userGroupId){
        return userManagerApi.deleteUserGroup(userGroupId);
    }

    /**
     * 查询所有用户组信息
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user_manage')")
    public List<UserGroupDto> getUserGroupAll(){
        return userManagerApi.getUserGroupAll();
    }
}
