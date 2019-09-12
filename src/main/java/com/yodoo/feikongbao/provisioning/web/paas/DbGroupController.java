package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.DbGroupService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：数据库组
 * @Author ：jinjun_luo
 * @Date ： 2019/8/28 0028
 */
@RestController
@RequestMapping(value = "/dbGroup")
@Api(tags = "DbGroupController | mysql 数据库组")
public class DbGroupController {

    @Autowired
    private DbGroupService dbGroupService;

    /**
     *  条件分页查询
     * @param pageNum
     * @param pageSize
     * @param groupCode
     * @param groupName
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "groupCode", value = "数据库组的代码", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "groupName", value = "数据库组名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> queryDbGroupList(int pageNum, int pageSize, String groupCode, String groupName) {
        DbGroupDto dbGroupDto = new DbGroupDto();
        dbGroupDto.setPageNum(pageNum);
        dbGroupDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(groupCode)){
            dbGroupDto.setGroupCode(groupCode);
        }
        if (StringUtils.isNotBlank(groupName)){
            dbGroupDto.setGroupName(groupName);
        }
        PageInfoDto<DbGroupDto> pageInfoDto = dbGroupService.queryDbGroupList(dbGroupDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), DbGroupController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, DbGroupController.class, Arrays.asList("db_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<DbGroupDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }


    /**
     * 添加
     * @param dbGroupDto
     * @return
     */
    @ApiOperation(value = "添加数据库组", httpMethod = "POST")
    @ApiImplicitParam(name = "dbGroupDto", value = "数据库组实体 dbGroupDto", required = true, dataType = "DbGroupDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> addDbGroup(@RequestBody DbGroupDto dbGroupDto){
        dbGroupService.addDbGroup(dbGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改
     * @param dbGroupDto
     * @return
     */
    @ApiOperation(value = "修改数据库组信息", httpMethod = "PUT")
    @ApiImplicitParam(name = "dbGroupDto", value = "数据库组实体 dbGroupDto", required = true, dataType = "DbGroupDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> editDbGroup(@RequestBody DbGroupDto dbGroupDto){
        dbGroupService.editDbGroup(dbGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value="通过 id 删除数据库组")
    @ApiImplicitParam(name = "id", value = "数据库组自增 id", paramType="path",required = true, dataType = "integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> deleteDbGroup(@PathVariable Integer id){
        dbGroupService.deleteDbGroup(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
