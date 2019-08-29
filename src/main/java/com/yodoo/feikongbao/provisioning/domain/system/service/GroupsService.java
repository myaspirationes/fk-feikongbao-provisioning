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
import com.yodoo.feikongbao.provisioning.util.UserUtils;
import com.yodoo.megalodon.permission.entity.UserPermissionDetails;
import com.yodoo.megalodon.permission.service.UserPermissionDetailsService;
import com.yodoo.megalodon.permission.service.UserPermissionTargetGroupDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description ：集团
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class GroupsService {

    @Autowired
    private GroupsMapper groupsMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserPermissionTargetGroupDetailsService userPermissionTargetGroupDetailsService;

    @Autowired
    private UserPermissionDetailsService userPermissionDetailsService;

    /**
     * 条件分页查询
     *
     * @param groupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public PageInfoDto<GroupsDto> queryGroupList(GroupsDto groupDto) {
        Example example = new Example(Groups.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(groupDto.getGroupName())){
            criteria.andEqualTo("groupName", groupDto.getGroupName());
        }
        if (StringUtils.isNotBlank(groupDto.getGroupCode())){
            criteria.andEqualTo("groupCode", groupDto.getGroupCode());
        }
        Page<?> pages = PageHelper.startPage(groupDto.getPageNum(), groupDto.getPageSize());
        List<Groups> groupsList = groupsMapper.selectByExample(example);
        List<GroupsDto> dtoList = copyProperties(groupsList);
        return new PageInfoDto<GroupsDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), dtoList);
    }

    /**
     * 添加
     *
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
     *
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
     *
     * @param id
     * @return
     */
    public GroupsDto getGroupDetails(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 通过 groupCode 查询
     *
     * @param groupCode
     * @return
     */
    public GroupsDto getGroupsByGroupCode(String groupCode) {
        if (StringUtils.isBlank(groupCode)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(Groups.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", groupCode);
        Groups groupsResponse = groupsMapper.selectOneByExample(example);
        return copyProperties(groupsResponse);
    }

    /**
     * 获取当前用户下管理的集团列表
     * TODO 用户id 从  session中获取
     * @return
     */
    public List<GroupsDto> getGroupListByUserId() {
        Integer userId = UserUtils.getUserId();
        // 通过用户id 查询用户权限表
        List<UserPermissionDetails> userPermissionDetailsList = userPermissionDetailsService.selectUserPermissionDetailsByUserId(userId);
        if (!CollectionUtils.isEmpty(userPermissionDetailsList)) {
            // 通过用户权限id 查询目标集团表
            Set<Integer> groupIdList = userPermissionTargetGroupDetailsService.getGroupIdsByUserIdAndPermissionId(userPermissionDetailsList);
            if (!CollectionUtils.isEmpty(groupIdList)){
                return groupIdList.stream()
                        .filter(Objects::nonNull)
                        .map(groupId -> {
                            return copyProperties(selectByPrimaryKey(groupId));
                        }).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 查询除ids 以外的集团
     * @param groupsIdsListSet
     * @return
     */
    public List<GroupsDto> selectGroupsNotInIds(Set<Integer> groupsIdsListSet) {
        if (!CollectionUtils.isEmpty(groupsIdsListSet)){
            List<Groups> groupsList = groupsMapper.selectGroupInNotIn(groupsIdsListSet);
            return copyProperties(groupsList);
        }
        return null;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public GroupsDto selectGroupById(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public Groups selectByPrimaryKey(Integer id) {
        return groupsMapper.selectByPrimaryKey(id);
    }


    /**
     * 通过id 查询，统计不存在的数量
     * @param groupsIds
     * @return
     */
    public Long selectGroupsNoExistCountByIds(Set<Integer> groupsIds) {
        Long count = null;
        if (!CollectionUtils.isEmpty(groupsIds)){
            count = groupsIds.stream()
                    .filter(Objects::nonNull)
                    .map(id -> {
                        return selectByPrimaryKey(id);
                    })
                    .filter(groups -> groups == null)
                    .count();
        }
        return count;
    }

    /**
     * 删除参数校验
     *
     * @param id
     */
    private void deleteGroupParameterCheck(Integer id) {
        if (id == null || id < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Groups groups = selectByPrimaryKey(id);
        if (groups == null) {
            throw new ProvisioningException(BundleKey.GROUPS_NOT_EXIST, BundleKey.GROUPS_NOT_EXIST_MSG);
        }
        // 查询公司表，如果存在使用，不能删除
        Integer selectGroupsCount = companyService.selectGroupCountByGroupId(id);
        if (selectGroupsCount != null && selectGroupsCount > 0) {
            throw new ProvisioningException(BundleKey.THE_DATA_IS_STILL_IN_USE, BundleKey.THE_DATA_IS_STILL_IN_USE_MEG);
        }
        // 查询目标集团表，如果存在使用不能删除
        Integer userPermissionTargetGroupsDetailsCount = userPermissionTargetGroupDetailsService.selectUserPermissionTargetGroupDetailsCountByGroupId(id);
        if (userPermissionTargetGroupsDetailsCount != null && userPermissionTargetGroupsDetailsCount > 0) {
            throw new ProvisioningException(BundleKey.THE_DATA_IS_STILL_IN_USE, BundleKey.THE_DATA_IS_STILL_IN_USE_MEG);
        }
    }

    /**
     * 修改参数校验
     * 1、不存在不修改
     * 2、如果修改的数据已存在不修改
     */
    private Groups editGroupParameterCheck(GroupsDto groupsDto) {
        if (groupsDto == null || groupsDto.getTid() == null || groupsDto.getTid() < 0 || StringUtils.isBlank(groupsDto.getGroupName())
                || groupsDto.getExpireDate() == null) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Groups groups = selectByPrimaryKey(groupsDto.getTid());
        if (groups == null) {
            throw new ProvisioningException(BundleKey.GROUPS_NOT_EXIST, BundleKey.GROUPS_NOT_EXIST_MSG);
        }
        Groups groupsItself = groupsMapper.selectGroupsInAdditionToItself(groupsDto.getTid(), groupsDto.getGroupCode());
        if (groupsItself != null) {
            throw new ProvisioningException(BundleKey.GROUPS_ALREADY_EXIST, BundleKey.GROUPS_ALREADY_EXIST_MSG);
        }
        buildUpdateParameter(groups, groupsDto);
        return groups;
    }

    /**
     * 更新参数封装
     *
     * @param groups
     * @param groupsDto
     */
    private void buildUpdateParameter(Groups groups, GroupsDto groupsDto) {
        /** 集团名称 **/
        if (StringUtils.isNoneBlank(groupsDto.getGroupName())) {
            groups.setGroupName(groupsDto.getGroupName());
        }
        /** 到期日 **/
        if (groupsDto.getExpireDate() != null) {
            groups.setExpireDate(groupsDto.getExpireDate());
        }
        /** 更新周期 **/
        if (StringUtils.isNoneBlank(groupsDto.getUpdateCycle())) {
            groups.setUpdateCycle(groupsDto.getUpdateCycle());
        }
        /** 下次更新日期 **/
        if (groupsDto.getNextUpdateDate() != null) {
            groups.setNextUpdateDate(groupsDto.getNextUpdateDate());
        }
    }

    /**
     * 添加参数校验
     *
     * @param groupsDto
     */
    private void addGroupParameterCheck(GroupsDto groupsDto) {
        if (groupsDto == null || StringUtils.isBlank(groupsDto.getGroupName()) || StringUtils.isBlank(groupsDto.getGroupCode())
                || groupsDto.getExpireDate() == null) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询是否有相同的数据，有不添加
        Example example = new Example(Groups.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", groupsDto.getGroupCode());
        Groups groups = groupsMapper.selectOneByExample(example);
        if (groups != null) {
            throw new ProvisioningException(BundleKey.GROUPS_ALREADY_EXIST, BundleKey.GROUPS_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 复制
     * @param groupsList
     * @return
     */
    private List<GroupsDto> copyProperties(List<Groups> groupsList){
        if (!CollectionUtils.isEmpty(groupsList)){
            return groupsList.stream()
                    .filter(Objects::nonNull)
                    .map(groups -> {
                        return copyProperties(groups);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param groups
     * @return
     */
    private GroupsDto copyProperties(Groups groups){
        if (groups != null){
            GroupsDto groupsDto = new GroupsDto();
            BeanUtils.copyProperties(groups, groupsDto);
            groupsDto.setTid(groups.getId());
            return groupsDto;
        }
        return null;
    }
}
