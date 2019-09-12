package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.RedisInstanceService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：Redis 实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@RestController
@RequestMapping(value = "/redisInstance")
@Api(tags = "RedisInstanceController | redis 实例")
public class RedisInstanceController {

    @Autowired
    private RedisInstanceService redisInstanceService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param redisGroupId
     * @param type
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "redisGroupId", value = "redis 组数据库自增 id ", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "redis 实例类型", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage','db_manage')")
    public ProvisioningDto<?> queryRedisInstanceList(int pageNum, int pageSize, Integer redisGroupId, Integer type) {
        RedisInstanceDto redisInstanceDto = new RedisInstanceDto();
        redisInstanceDto.setPageNum(pageNum);
        redisInstanceDto.setPageSize(pageSize);
        if (redisGroupId != null){
            redisInstanceDto.setRedisGroupId(redisGroupId);
        }
        if (type != null){
            redisInstanceDto.setTid(type);
        }
        PageInfoDto<RedisInstanceDto> pageInfoDto = redisInstanceService.queryRedisInstanceList(redisInstanceDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), RedisInstanceController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, RedisInstanceController.class, Arrays.asList("company_manage","db_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<RedisInstanceDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @param redisInstanceDto
     * @return
     */
    @ApiOperation(value = "添加 redis 实例", httpMethod = "POST")
    @ApiImplicitParam(name = "redisInstanceDto", value = "redis 实例 redisInstanceDto", required = true, dataType = "RedisInstanceDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> addRedisInstance(@RequestBody RedisInstanceDto redisInstanceDto){
        redisInstanceService.addRedisInstance(redisInstanceDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     * @param redisInstanceDto
     * @return
     */
    @ApiOperation(value = "更新 redis 实例", httpMethod = "PUT")
    @ApiImplicitParam(name = "redisInstanceDto", value = "redis 实例 redisInstanceDto", required = true, dataType = "RedisInstanceDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> editRedisInstance(@RequestBody RedisInstanceDto redisInstanceDto){
        redisInstanceService.editRedisInstance(redisInstanceDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除 redis 实例", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "redis 实例 数据库自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> deleteRedisInstance(@RequestBody Integer id){
        redisInstanceService.deleteRedisInstance(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询 redis 实例详情", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "redis 实例 数据库自增 id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getRedisInstanceDetails(@PathVariable Integer id) {
        RedisInstanceDto redisInstanceDto = redisInstanceService.getRedisInstanceDetails(id);
        return new ProvisioningDto<RedisInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, redisInstanceDto);
    }

    /**
     * 使用缓存
     *
     * @param redisInstanceDto
     * @return
     */
    @ApiOperation(value = "使用缓存", hidden = true)
    @RequestMapping(value = "/useRedisInstance", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useRedisInstance(@RequestBody RedisInstanceDto redisInstanceDto) {
        RedisInstanceDto redisInstanceDtoResponse = redisInstanceService.useRedisInstance(redisInstanceDto);
        return new ProvisioningDto<RedisInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, redisInstanceDtoResponse);
    }

}
