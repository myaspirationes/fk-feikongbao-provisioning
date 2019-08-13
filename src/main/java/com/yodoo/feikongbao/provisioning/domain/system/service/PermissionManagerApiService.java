package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.GroupsDto;
import com.yodoo.feikongbao.provisioning.util.UserUtils;
import com.yodoo.megalodon.permission.api.PermissionManagerApi;
import com.yodoo.megalodon.permission.common.PageInfoDto;
import com.yodoo.megalodon.permission.dto.*;
import com.yodoo.megalodon.permission.entity.Permission;
import com.yodoo.megalodon.permission.entity.SearchCondition;
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
 * @Description ：权限管理
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
@Service
public class PermissionManagerApiService {

    @Autowired
    private PermissionManagerApi permissionManagerApi;

    /**
     * 在用户列表中点击权限,获取目标集团、目标公司、目标用户的list
     * @param userId
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ActionPermissionInUserListDto actionPermissionInUserList(Integer userId){
        return permissionManagerApi.actionPermissionInUserList(userId == null ? UserUtils.getUserId() : userId);
    }

    /**
     * 根据用户id查询已管理的目标集团
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<GroupsDto> getTargetGroupsByUserId(Integer userId){
        return permissionManagerApi.getTargetGroupsByUserId(userId == null ? UserUtils.getUserId() : userId);
    }

    /**
     * 通过用户id查询可管理的目标集团
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<GroupsDto> getAvailableGroupsByUserId(Integer userId){
        return permissionManagerApi.getAvailableGroupsByUserId(userId == null ? UserUtils.getUserId() : userId);
    }

    /**
     * 变更用户管理目标集团 todo
     * @param userPermissionTargetDto
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public void updateUserPermissionTargetGroups(UserPermissionTargetDto userPermissionTargetDto){
        permissionManagerApi.updateUserPermissionTargetGroups(userPermissionTargetDto);
    }

    /**
     * 通过用户id查询已管理的目标公司
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<CompanyDto> getUserManageTargetCompanyListByUserId(Integer userId){
        return permissionManagerApi.getUserManageTargetCompanyListByUserId(userId == null ? UserUtils.getUserId() : userId);
    }

    /**
     * 通过用户id查询可管理的目标公司
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<CompanyDto> getAvailableUserManageTargetCompanyListByUserId(Integer userId){
        return permissionManagerApi.getAvailableUserManageTargetCompanyListByUserId(userId == null ? UserUtils.getUserId() : userId);
    }

    /**
     * 更新用户管理目标公司数据
     * @param userPermissionTargetDto
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public void updateUserPermissionTargetCompany(UserPermissionTargetDto userPermissionTargetDto){
        permissionManagerApi.updateUserPermissionTargetCompany(userPermissionTargetDto);
    }

    /**
     * 通过用户id查询已管理的目标用户
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<UserDto> selectUserManageTargetUserListByUserId(Integer userId){
        return permissionManagerApi.selectUserManageTargetUserListByUserId(userId == null ? UserUtils.getUserId() : userId);
    }

    /**
     * 通过用户id查询可管理的目标用户
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<UserDto> getAvailableUserManageTargetUserListByUserId(Integer userId){
        return permissionManagerApi.getAvailableUserManageTargetUserListByUserId(userId == null ? UserUtils.getUserId() : userId);
    }

    /**
     * 更新用户管理目标用户数据
     * @param userPermissionTargetDto
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public void updateUserPermissionTargetUser(UserPermissionTargetDto userPermissionTargetDto){
        permissionManagerApi.updateUserPermissionTargetUser(userPermissionTargetDto);
    }

    /**
     * 条件分页查询权限列表
     * @param permissionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionDto> queryPermissionList(PermissionDto permissionDto){
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionDto> pageInfoDto = new com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<>();

        PageInfoDto<PermissionDto> permissionDtoPageInfoDto = permissionManagerApi.queryPermissionList(permissionDto);
        if (permissionDtoPageInfoDto != null){
            BeanUtils.copyProperties(permissionDtoPageInfoDto, pageInfoDto);
        }
        if (permissionDtoPageInfoDto != null && !CollectionUtils.isEmpty(permissionDtoPageInfoDto.getList())){
            List<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionDto> collect = permissionDtoPageInfoDto.getList().stream()
                    .filter(Objects::nonNull)
                    .map(permissionDtoPermission -> {
                        com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionDto permissionDtoProvisioning = new com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionDto();
                        BeanUtils.copyProperties(permissionDtoPermission, permissionDtoProvisioning);
                        permissionDtoProvisioning.setTid(permissionDtoPermission.getId());
                        return permissionDtoProvisioning;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)){
                pageInfoDto.setList(collect);
            }
        }
        return pageInfoDto;

    }

    /**
     * 添加权限
     * @param permissionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addPermission(PermissionDto permissionDto) {
        return permissionManagerApi.addPermission(permissionDto);
    }

    /**
     * 修改权限
     * @param permissionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer updatePermission(PermissionDto permissionDto) {
        return permissionManagerApi.updatePermission(permissionDto);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer deletePermission(Integer id){
        return permissionManagerApi.deletePermission(id);
    }

    /**
     * 根据userId查询权限
     * @param userId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public List<Permission> getPermissionByUserId(Integer userId) {
        return permissionManagerApi.getPermissionByUserId(userId);
    }

    /**
     * 条件分页查询权限组列表
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto> queryPermissionGroupList(PermissionGroupDto permissionGroupDto){
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto> pageInfoDto = new com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<>();
        PageInfoDto<PermissionGroupDto> permissionGroupDtoPageInfoDto = permissionManagerApi.queryPermissionGroupList(permissionGroupDto);
        if (permissionGroupDtoPageInfoDto != null){
            BeanUtils.copyProperties(permissionGroupDtoPageInfoDto, pageInfoDto);
        }
        if (permissionGroupDtoPageInfoDto != null && !CollectionUtils.isEmpty(permissionGroupDtoPageInfoDto.getList())){
            List<com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto> collect = permissionGroupDtoPageInfoDto.getList().stream()
                    .filter(Objects::nonNull)
                    .map(permissionGroupDtoPermission -> {
                        com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto permissionGroupDtoResponse = new com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto();
                        BeanUtils.copyProperties(permissionGroupDtoPermission, permissionGroupDtoResponse);
                        permissionGroupDtoResponse.setTid(permissionGroupDtoPermission.getId());
                        return permissionGroupDtoResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            pageInfoDto.setList(collect);
        }
        return pageInfoDto;
    }

    /**
     * 添加权限组：
     * 1、选权限
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addPermissionGroup(PermissionGroupDto permissionGroupDto) {
        return permissionManagerApi.addPermissionGroup(permissionGroupDto);
    }

    /**
     * 更新权限组：
     * 1、更新权限
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer editPermissionGroup(PermissionGroupDto permissionGroupDto) {
        return permissionManagerApi.editPermissionGroup(permissionGroupDto);
    }

    /**
     * 删除权限组 TODO
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer deletePermissionGroup(Integer id) {
        return permissionManagerApi.deletePermissionGroup(id);
    }

    /**
     * 查询权限组详情
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public PermissionGroupDto getPermissionGroupDetails(Integer id) {
        return permissionManagerApi.getPermissionGroupDetails(id);
    }

    /**
     * 条件分页查询 条件查询表
     * @param searchConditionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.SearchConditionDto> queryUserGroupList(SearchConditionDto searchConditionDto){
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.SearchConditionDto> pageInfoDto = new com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<>();

        PageInfoDto<SearchConditionDto> searchConditionDtoPageInfoDto = permissionManagerApi.queryUserGroupList(searchConditionDto);
        if (searchConditionDtoPageInfoDto != null){
            BeanUtils.copyProperties(searchConditionDtoPageInfoDto, pageInfoDto);
        }
        if (searchConditionDtoPageInfoDto != null && !CollectionUtils.isEmpty(searchConditionDtoPageInfoDto.getList())){
            List<com.yodoo.feikongbao.provisioning.domain.system.dto.SearchConditionDto> collect = searchConditionDtoPageInfoDto.getList().stream()
                    .filter(Objects::nonNull)
                    .map(searchConditionDtoPermission -> {
                        com.yodoo.feikongbao.provisioning.domain.system.dto.SearchConditionDto searchConditionDtoResponse = new com.yodoo.feikongbao.provisioning.domain.system.dto.SearchConditionDto();
                        BeanUtils.copyProperties(searchConditionDtoPermission, searchConditionDtoResponse);
                        searchConditionDtoResponse.setTid(searchConditionDtoPermission.getId());
                        return searchConditionDtoResponse;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
            pageInfoDto.setList(collect);
        }
        return pageInfoDto;
    }

    /**
     * 添加 条件查询表
     * @param searchConditionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer addSearchCondition(SearchConditionDto searchConditionDto){
        return permissionManagerApi.addSearchCondition(searchConditionDto);
    }

    /**
     * 更新条件查询表
     * @param searchConditionDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer editSearchCondition(SearchConditionDto searchConditionDto){
        return permissionManagerApi.editSearchCondition(searchConditionDto);
    }

    /**
     * 删除条件查询表
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public Integer deleteSearchCondition(Integer id){
        return permissionManagerApi.deleteSearchCondition(id);
    }

    /**
     * 通过权限组id 查询数量
     *
     * @param permissionGroupId
     * @return
     */
    public Integer selectPermissionGroupDetailsCountByPermissionGroupId(Integer permissionGroupId){
        return permissionManagerApi.selectPermissionGroupDetailsCountByPermissionGroupId(permissionGroupId);
    }

    /**
     *  查询所有条件
     * @return
     */
    public List<SearchCondition> getAllSearchCondition() {
        return permissionManagerApi.getAllSearchCondition();
    }
}
