package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.PermissionGroupDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.PermissionGroup;
import com.yodoo.feikongbao.provisioning.domain.system.entity.PermissionGroupDetails;
import com.yodoo.feikongbao.provisioning.domain.system.entity.UserGroupPermissionDetails;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.PermissionGroupMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @Description ：权限组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class PermissionGroupService {

    private static Logger logger = LoggerFactory.getLogger(PermissionGroupService.class);

    @Autowired
    private PermissionGroupMapper permissionGroupMapper;

    @Autowired
    private UserGroupPermissionDetailsService userGroupPermissionDetailsService;

    @Autowired
    private PermissionGroupDetailsService permissionGroupDetailsService;

    /**
     * 条件分页查询
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission')")
    public PageInfoDto<PermissionGroupDto> queryPermissionGroupList(PermissionGroupDto permissionGroupDto) {
        PermissionGroup permissionGroup = new PermissionGroup();
        BeanUtils.copyProperties(permissionGroupDto, permissionGroup);
        Page<?> pages = PageHelper.startPage(permissionGroupDto.getPageNum(), permissionGroupDto.getPageSize());
        List<PermissionGroup> select = permissionGroupMapper.select(permissionGroup);
        List<PermissionGroupDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(select)){
            collect = select.stream()
                    .filter(Objects::nonNull)
                    .map(permissionGroup1 -> {
                        PermissionGroupDto dto = new PermissionGroupDto();
                        BeanUtils.copyProperties(permissionGroup1, dto);
                        dto.setTid(permissionGroup1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<PermissionGroupDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission')")
    public Integer addPermissionGroup(PermissionGroupDto permissionGroupDto) {
        addPermissionGroupParameterCheck(permissionGroupDto);
        return permissionGroupMapper.insertSelective(new PermissionGroup(permissionGroupDto.getGroupCode(), permissionGroupDto.getGroupName()));
    }

    /**
     * 更新
     * @param permissionGroupDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission')")
    public Integer editPermissionGroup(PermissionGroupDto permissionGroupDto) {
        editPermissionGroupParameterCheck(permissionGroupDto);
        PermissionGroup permissionGroup = new PermissionGroup(permissionGroupDto.getGroupCode(), permissionGroupDto.getGroupName());
        return permissionGroupMapper.updateByPrimaryKeySelective(permissionGroup);
    }

    /**
     * 删除：
     * 1、不存在不修改
     * 2、查询权限组明细存在不修改
     * 3、查询用户组权限组关系表存在不修改
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission')")
    public Integer deletePermissionGroup(Integer id) {
        deletePermissionGroupParameterCheck(id);
        return permissionGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission')")
    public PermissionGroupDto getPermissionGroupDetails(Integer id) {
        PermissionGroup permissionGroup = selectByPrimaryKey(id);
        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();;
        if (permissionGroup != null){
            BeanUtils.copyProperties(permissionGroup, permissionGroupDto);
            permissionGroupDto.setTid(permissionGroup.getId());
        }
        return permissionGroupDto;
    }

    /**
     * 更新参数校验
     * @param permissionGroupDto
     * @return
     */
    private void editPermissionGroupParameterCheck(PermissionGroupDto permissionGroupDto) {
        if (permissionGroupDto == null || permissionGroupDto.getTid() == null || permissionGroupDto.getTid() < 0
                || StringUtils.isBlank(permissionGroupDto.getGroupCode()) || StringUtils.isBlank(permissionGroupDto.getGroupName())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 不存在不能修改
        PermissionGroup permissionGroup = selectByPrimaryKey(permissionGroupDto.getTid());
        if (permissionGroup == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        PermissionGroup permissionGroupSelf = permissionGroupMapper.selectPermissionGroupInAdditionToItself(permissionGroupDto.getTid(),permissionGroupDto.getGroupCode(), permissionGroupDto.getGroupName());
        if (permissionGroupSelf != null){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST_MEG);
        }
    }

    /**
     * 通过主键查询
     * @param tid
     * @return
     */
    private PermissionGroup selectByPrimaryKey(Integer tid) {
        return permissionGroupMapper.selectByPrimaryKey(tid);
    }

    /**
     * 添加参数校验
     * @param permissionGroupDto
     * @return
     */
    private void addPermissionGroupParameterCheck(PermissionGroupDto permissionGroupDto) {
        if (permissionGroupDto == null || StringUtils.isBlank(permissionGroupDto.getGroupCode()) || StringUtils.isBlank(permissionGroupDto.getGroupName())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询是否有相同的数据，有不添加
        PermissionGroup permissionGroup = permissionGroupMapper.selectOne(new PermissionGroup(permissionGroupDto.getGroupCode(),permissionGroupDto.getGroupName()));
        if (permissionGroup != null){
            throw new ProvisioningException(BundleKey.DICTIONARY_EXIST, BundleKey.DICTIONARY_EXIST_MSG);
        }
    }

    /**
     * 删除参数校验：
     * 1、不存在不修改
     * 2、查询权限组明细存在不修改
     * 3、查询用户组权限组关系表存在不修改
     * @param id
     * @return
     */
    private void deletePermissionGroupParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        PermissionGroup permissionGroup = selectByPrimaryKey(id);
        if (permissionGroup == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // 查询权限组明细，如果存在使用，不能删除
        PermissionGroupDetails permissionGroupDetails = permissionGroupDetailsService.selectPermissionGroupDetailsByPermissionGroupId(id);
        if (permissionGroupDetails != null){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST_MEG);
        }
        // 查询用户组权限组关系表，如果存在使用不能删除
        UserGroupPermissionDetails userGroupPermissionDetails = userGroupPermissionDetailsService.selectUserGroupPermissionDetailsByPermissionGroupId(id);
        if (userGroupPermissionDetails != null){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST);
        }
    }
}