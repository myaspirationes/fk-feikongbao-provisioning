package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.GroupsDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Groups;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.GroupsMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：集团
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class GroupsService {

    @Autowired
    private GroupsMapper groupsMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserPermissionTargetGroupDetailsService userPermissionTargetGroupDetailsService;

    /**
     * 条件分页查询
     * @param groupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public PageInfoDto<GroupsDto> queryGroupList(GroupsDto groupDto) {
        Groups group = new Groups();
        BeanUtils.copyProperties(groupDto, group);
        Page<?> pages = PageHelper.startPage(groupDto.getPageNum(), groupDto.getPageSize());
        List<Groups> select = groupsMapper.select(group);
        List<GroupsDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(select)){
            collect = select.stream()
                    .filter(Objects::nonNull)
                    .map(group1 -> {
                        GroupsDto dto = new GroupsDto();
                        BeanUtils.copyProperties(group1, dto);
                        dto.setTid(group1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<GroupsDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public Groups selectByPrimaryKey(Integer id) {
        return groupsMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     * @param groupDto
     */
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public Integer addGroup(GroupsDto groupDto) {
        // 参数校验
        addGroupParameterCheck(groupDto);
        Groups groups = new Groups();
        BeanUtils.copyProperties(groupDto, groups);
        return groupsMapper.insertSelective(groups);
    }

    /**
     * 修改
     * @param groupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public Integer editGroup(GroupsDto groupDto) {
        Groups groups = editGroupParameterCheck(groupDto);
        return groupsMapper.updateByPrimaryKeySelective(groups);
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public Integer deleteGroup(Integer id) {
        deleteGroupParameterCheck(id);
        return groupsMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    public GroupsDto getGroupDetails(Integer id) {
        Groups group = selectByPrimaryKey(id);
        GroupsDto groupsDto = new GroupsDto();
        if (group != null){
            BeanUtils.copyProperties(group, groupsDto);
            groupsDto.setTid(group.getId());
        }
        return groupsDto;
    }

    /**
     * 删除参数校验
     * @param id
     */
    private void deleteGroupParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Groups groups = selectByPrimaryKey(id);
        if (groups == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // 查询公司表，如果存在使用，不能删除
        Integer selectGroupsCount = companyService.selectGroupCountByGroupId(id);
        if (selectGroupsCount != null && selectGroupsCount > 0){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST_MEG);
        }
        // 查询目标集团表，如果存在使用不能删除
        Integer userPermissionTargetGroupsDetailsCount = userPermissionTargetGroupDetailsService.selectUserPermissionTargetGroupDetailsCountByGroupId(id);
        if (userPermissionTargetGroupsDetailsCount != null && userPermissionTargetGroupsDetailsCount > 0){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST);
        }
    }

    /**
     * 修改参数校验
     * 1、不存在不修改
     * 2、如果修改的数据已存在不修改
     */
    private Groups editGroupParameterCheck(GroupsDto groupsDto) {
        if (groupsDto == null || groupsDto.getTid() == null || groupsDto.getTid() < 0 || StringUtils.isBlank(groupsDto.getGroupName())
                || StringUtils.isBlank(groupsDto.getGroupCode()) || groupsDto.getExpireDate() == null){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Groups groups = selectByPrimaryKey(groupsDto.getTid());
        if (groups == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        Groups groupsItself = groupsMapper.selectGroupsInAdditionToItself(groupsDto.getTid(), groupsDto.getGroupName(), groupsDto.getGroupCode());
        if (groupsItself != null){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST_MEG);
        }
        BeanUtils.copyProperties(groupsDto,groups);
        return groups;
    }

    /**
     * 添加参数校验
     * @param groupsDto
     */
    private void addGroupParameterCheck(GroupsDto groupsDto) {
        if (groupsDto == null || StringUtils.isBlank(groupsDto.getGroupName()) || StringUtils.isBlank(groupsDto.getGroupCode())
                || groupsDto.getExpireDate() == null){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询是否有相同的数据，有不添加
        Groups groups = groupsMapper.selectOne(new Groups(groupsDto.getGroupName(), groupsDto.getGroupCode()));
        if (groups != null){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST_MEG);
        }
    }
}
