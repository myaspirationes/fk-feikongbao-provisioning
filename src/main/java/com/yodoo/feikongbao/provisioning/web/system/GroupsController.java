package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.GroupsDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.GroupsService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
@Api(tags = "GroupsController | 集团")
public class GroupsController {

    @Autowired
    private GroupsService groupService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "groupName", value = "集团名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "groupCode", value = "集团代码", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> queryGroupList(int pageNum, int pageSize, String groupName, String groupCode) {
        GroupsDto groupDto = new GroupsDto();
        groupDto.setPageNum(pageNum);
        groupDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(groupName)){
            groupDto.setGroupName(groupName);
        }
        if (StringUtils.isNotBlank(groupCode)){
            groupDto.setGroupCode(groupCode);
        }
        PageInfoDto<GroupsDto> pageInfoDto = groupService.queryGroupList(groupDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), GroupsController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, GroupsController.class, Arrays.asList("group_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(), "groupCode");
        return new ProvisioningDto<PageInfoDto<GroupsDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 校验集团 code 是否存在
     *
     * @return
     */
    @ApiOperation(value = "校验集团 code 是否存在", httpMethod = "POST")
    @ApiImplicitParam(name = "groupCode", value = "集团 code", required = true, dataType = "String", example = "dev_test_group")
    @RequestMapping(value = "groupCode/{groupCode}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> getGroupsByGroupCode(@PathVariable String groupCode) {
        GroupsDto groupDto = groupService.getGroupsByGroupCode(groupCode);
        return new ProvisioningDto<GroupsDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, groupDto);
    }

    /**
     * 添加:
     *
     * @param groupDto
     * @return
     */
    @ApiOperation(value = "添加", httpMethod = "POST")
    @ApiImplicitParam(name = "groupDto", value = "集团 groupDto", required = true, dataType = "GroupsDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> addGroup(@RequestBody GroupsDto groupDto) {
        groupService.addGroup(groupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     *
     * @param groupDto
     * @return
     */
    @ApiOperation(value = "更新", httpMethod = "PUT")
    @ApiImplicitParam(name = "groupDto", value = "集团 groupDto", required = true, dataType = "GroupsDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> editGroup(@RequestBody GroupsDto groupDto) {
        groupService.editGroup(groupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "集团数据库表自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> deleteGroup(@PathVariable Integer id) {
        groupService.deleteGroup(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询详情", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "集团数据库表自增 id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> getGroupDetails(@PathVariable Integer id) {
        GroupsDto groupDto = groupService.getGroupDetails(id);
        return new ProvisioningDto<GroupsDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, groupDto);
    }

    /**
     * 获取当前用户能管理集团列表
     * 1、编辑公司实例页面，集团下拉列表
     *
     * @return
     */
    @ApiOperation(value = "获取当前用户能管理集团列表", httpMethod = "POST")
    @RequestMapping(value = "/groupListByUserId", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('group_manage')")
    public ProvisioningDto<?> getGroupListByUserId() {
        List<GroupsDto> groupDtoList = groupService.getGroupListByUserId();
        return new ProvisioningDto<List<GroupsDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, groupDtoList);
    }
}
