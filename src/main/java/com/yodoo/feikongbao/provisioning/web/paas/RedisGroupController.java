package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.RedisGroupService;
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

/**
 * @Description ：redis组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@RestController
@RequestMapping(value = "/redisGroup")
@Api(tags = "RedisGroupController | redis 数据库实例组")
public class RedisGroupController {

    @Autowired
    private RedisGroupService redisGroupService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param groupCode
     * @param groupName
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "groupCode", value = "redis 组编号 ", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "groupName", value = "redis 组名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage','db_manage')")
    public ProvisioningDto<?> queryRedisGroupList(int pageNum, int pageSize, String groupCode, String groupName) {
        RedisGroupDto redisGroupDto = new RedisGroupDto();
        redisGroupDto.setPageNum(pageNum);
        redisGroupDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(groupCode)){
            redisGroupDto.setGroupCode(groupCode);
        }
        if (StringUtils.isNotBlank(groupName)){
            redisGroupDto.setGroupName(groupName);
        }
        PageInfoDto<RedisGroupDto> pageInfoDto = redisGroupService.queryRedisGroupList(redisGroupDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), RedisGroupController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, RedisGroupController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<RedisGroupDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @return
     */
    @ApiOperation(value = "添加 redis 组", httpMethod = "POST")
    @ApiImplicitParam(name = "redisGroupDto", value = "redis 组 redisGroupDto", required = true, dataType = "RedisGroupDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage','db_manage')")
   public ProvisioningDto<?> addRedisGroup(@RequestBody RedisGroupDto redisGroupDto){
        redisGroupService.addRedisGroup(redisGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     * @return
     */
    @ApiOperation(value = "更新 redis 组", httpMethod = "PUT")
    @ApiImplicitParam(name = "redisGroupDto", value = "redis 组 redisGroupDto", required = true, dataType = "RedisGroupDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('company_manage','db_manage')")
   public ProvisioningDto<?> editRedisGroup(@RequestBody RedisGroupDto redisGroupDto){
        redisGroupService.editRedisGroup(redisGroupDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @return
     */
    @ApiOperation(value = "删除 redis 组", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "redis 组 数据库自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('company_manage','db_manage')")
   public ProvisioningDto<?> deleteRedisGroup(@PathVariable Integer id){
        redisGroupService.deleteRedisGroup(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

   /**
     * 查询详情
     *
     * @param id
     * @return
     */
   @ApiOperation(value = "查询 redis 组 详情", httpMethod = "POST")
   @ApiImplicitParam(name = "id", value = "redis 组 数据库自增 id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getRedisGroupDetails(@PathVariable Integer id) {
        RedisGroupDto redisGroupDto = redisGroupService.getRedisGroupDetails(id);
        return new ProvisioningDto<RedisGroupDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, redisGroupDto);
    }

}
