package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.PermissionManagerApiService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import com.yodoo.megalodon.permission.dto.SearchConditionDto;
import com.yodoo.megalodon.permission.entity.SearchCondition;
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
 * @Description ：条件
 * @Author ：jinjun_luo
 * @Date ： 2019/8/13 0013
 */
@RestController
@RequestMapping(value = "/searchCondition")
@Api(tags = "SearchConditionController | 条件")
public class SearchConditionController {

    @Autowired
    private PermissionManagerApiService permissionManagerService;

    /**
     * 条件分页查询 条件查询表
     * @param pageNum
     * @param pageSize
     * @param conditionName
     * @param conditionCode
     * @param conditionValue
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "conditionName", value = "名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "conditionCode", value = "编码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "conditionValue", value = "值", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> queryUserGroupList(int pageNum, int pageSize, String conditionName, String conditionCode, String conditionValue){
        SearchConditionDto searchConditionDto = new SearchConditionDto();
        searchConditionDto.setPageNum(pageNum);
        searchConditionDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(conditionName)){
            searchConditionDto.setConditionName(conditionName);
        }
        if (StringUtils.isNotBlank(conditionCode)){
            searchConditionDto.setConditionCode(conditionCode);
        }
        if (StringUtils.isNotBlank(conditionValue)){
            searchConditionDto.setConditionValue(conditionValue);
        }
        com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.SearchConditionDto> pageInfoDto = permissionManagerService.queryUserGroupList(searchConditionDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), SearchConditionController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, SearchConditionController.class, Arrays.asList("permission_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(),"getAllSearchCondition");
        return new ProvisioningDto<com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto<com.yodoo.feikongbao.provisioning.domain.system.dto.SearchConditionDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加条件查询表
     * @param searchConditionDto
     * @return
     */
    @ApiOperation(value = "添加条件查询表", httpMethod = "POST")
    @ApiImplicitParam(name = "searchConditionDto", value = "条件 searchConditionDto", required = true, dataType = "SearchConditionDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> addSearchCondition(@RequestBody SearchConditionDto searchConditionDto){
        permissionManagerService.addSearchCondition(searchConditionDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新条件查询表
     * @param searchConditionDto
     * @return
     */
    @ApiOperation(value = "更新条件查询表", httpMethod = "PUT")
    @ApiImplicitParam(name = "searchConditionDto", value = "条件 searchConditionDto", required = true, dataType = "SearchConditionDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> editSearchCondition(@RequestBody SearchConditionDto searchConditionDto){
        permissionManagerService.editSearchCondition(searchConditionDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除条件查询表
     * @param id
     * @return
     */
    @ApiOperation(value = "删除条件查询表", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "条件查询表数据库自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> deleteSearchCondition(@PathVariable Integer id){
        permissionManagerService.deleteSearchCondition(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询所有条件列表
     * @return
     */
    @ApiOperation(value = "删除条件查询表", httpMethod = "GET")
    @RequestMapping(value = "/getAllSearchCondition", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getAllSearchCondition(){
        List<SearchCondition> list = permissionManagerService.getAllSearchCondition();
        return new ProvisioningDto<List<SearchCondition>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG,list);
    }
}
