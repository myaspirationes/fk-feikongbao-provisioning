package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbSchemaDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.DbSchemaService;
import com.yodoo.feikongbao.provisioning.domain.paas.service.RedisInstanceService;
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
 * @Description ：DB数据库表
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@RestController
@RequestMapping(value = "/dbSchema")
@Api(tags = "DbSchemaController | mysql数据库")
public class DbSchemaController {

    @Autowired
    private DbSchemaService dbSchemaService;

    @Autowired
    private RedisInstanceService redisInstanceService;

    /**
     * 条件分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param dbInstanceId
     * @param dbGroupId
     * @param schemaName
     * @param status
     * @param type
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "dbInstanceId", value = "db实例自增id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "dbGroupId", value = "db数据库组自增id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "schemaName", value = "db数据库名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "db数据库状态", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "db数据库类型", required = false, dataType = "Integer", paramType = "query"),
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> queryDbSchemaList(int pageNum, int pageSize, Integer dbInstanceId, Integer dbGroupId, String schemaName, Integer status, Integer type) {
        DbSchemaDto dbSchemaDto = new DbSchemaDto();
        dbSchemaDto.setPageNum(pageNum);
        dbSchemaDto.setPageSize(pageSize);
        if (dbInstanceId != null){
            dbSchemaDto.setDbInstanceId(dbInstanceId);
        }
        if (dbGroupId != null){
            dbSchemaDto.setDbInstanceId(dbGroupId);
        }
        if (status != null){
            dbSchemaDto.setStatus(status);
        }
        if (type != null){
            dbSchemaDto.setType(type);
        }
        if (StringUtils.isNotBlank(schemaName)){
            dbSchemaDto.setSchemaName(schemaName);
        }
        PageInfoDto<DbSchemaDto> pageInfoDto = dbSchemaService.queryDbSchemaList(dbSchemaDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), DbSchemaController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, DbSchemaController.class, Arrays.asList("db_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<DbSchemaDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @param dbSchemaDto
     * @return
     */
    @ApiOperation(value = "添加数据库", httpMethod = "POST")
    @ApiImplicitParam(name = "dbSchemaDto", value = "db数据库 dbSchemaDto", required = true, dataType = "DbSchemaDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> addDbSchema(@RequestBody DbSchemaDto dbSchemaDto){
        dbSchemaService.addDbSchema(dbSchemaDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 更新
     * @param dbSchemaDto
     * @return
     */
    @ApiOperation(value = "修改数据库", httpMethod = "PUT")
    @ApiImplicitParam(name = "dbSchemaDto", value = "db数据库 dbSchemaDto", required = true, dataType = "DbSchemaDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> editDbSchema(@RequestBody DbSchemaDto dbSchemaDto){
        dbSchemaService.editDbSchema(dbSchemaDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除 TODO 一台dbInstance实例和dbSchema是否是一对一关系
     * @param id
     * @return
     */
    @ApiOperation(value = "删除数据库", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "数据库自增 id", paramType="path",required = true, dataType = "integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> deleteDbSchema(@RequestBody Integer id){
        dbSchemaService.deleteDbSchema(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 创建公司，选用数据库
     *
     * @param dbSchemaDto
     * @return
     */
    @ApiOperation(value = "创建公司，选用数据库", hidden = true)
    @RequestMapping(value = "/useDbSchema", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useDbSchema(@RequestBody DbSchemaDto dbSchemaDto) {
        DbSchemaDto dbSchemaDtoResponse = dbSchemaService.useDbSchema(dbSchemaDto);
        return new ProvisioningDto<DbSchemaDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, dbSchemaDtoResponse);
    }

    /**
     * 根据 类型 查询 redis 实例 列表
     * @param type
     * @return
     */
    @ApiOperation(value = "根据 类型 查询 redis 实例 列表", httpMethod = "GET")
    @ApiImplicitParam(name = "type", value = "redis 实例类型 0/1", paramType="path",required = true, dataType = "integer", example = "0")
    @RequestMapping(value = "redisInstance/{type}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getRedisInstanceByType(@PathVariable Integer type){
        List<RedisInstanceDto> redisInstanceDtoList =  redisInstanceService.getRedisInstanceByType(type);
        return new ProvisioningDto<List<RedisInstanceDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, redisInstanceDtoList);
    }

    /**
     * 根据 类型 查询 db_schema 实例 列表
     * @param type
     * @return
     */
    @ApiOperation(value = "根据 类型 查询 db_schema 实例 列表", httpMethod = "GET")
    @ApiImplicitParam(name = "type", value = "dbSchema 实例类型 0/1", paramType="path",required = true, dataType = "integer", example = "0")
    @RequestMapping(value = "dbSchema/{type}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getDbSchemaByType(@PathVariable Integer type){
        List<DbSchemaDto> dbSchemaDtoList =  dbSchemaService.getDbSchemaByType(type);
        return new ProvisioningDto<List<DbSchemaDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, dbSchemaDtoList);
    }
}
