package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.GroupsDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.GroupsService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description ：集团
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/group")
public class GroupsController {

    @Autowired
    private GroupsService groupService;

    /**
     * 条件分页查询
     * @param groupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> queryGroupList(GroupsDto groupDto){
        PageInfoDto<GroupsDto> pageInfoDto = groupService.queryGroupList(groupDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), GroupsController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, GroupsController.class, Arrays.asList("group_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(),OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<GroupsDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加:
     * @param groupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> addGroup(@RequestBody GroupsDto groupDto){
        groupService.addGroup(groupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     * @param groupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> editGroup(@RequestBody GroupsDto groupDto){
        groupService.editGroup(groupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> deleteGroup(@PathVariable Integer id){
        groupService.deleteGroup(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> getGroupDetails(@PathVariable Integer id){
        GroupsDto groupDto = groupService.getGroupDetails(id);
        return new ProvisioningDto<GroupsDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, groupDto);
    }

    /**
     * 获取当前用户能管理集团列表
     * 1、编辑公司实例页面，集团下拉列表
     * @return
     */
    @RequestMapping(value = "groupListByUserId", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> getGroupListByUserId(){
        List<GroupsDto> groupDtoList = groupService.getGroupListByUserId();
        return new ProvisioningDto<List<GroupsDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, groupDtoList);
    }
}
