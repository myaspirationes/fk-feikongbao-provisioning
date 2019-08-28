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
public class DbSchemaController {

    @Autowired
    private DbSchemaService dbSchemaService;

    @Autowired
    private RedisInstanceService redisInstanceService;

    /**
     * 条件分页查询
     *
     * @param dbSchemaDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> queryDbSchemaList(DbSchemaDto dbSchemaDto) {
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
    @RequestMapping(value = "dbSchema/{type}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getDbSchemaByType(@PathVariable Integer type){
        List<DbSchemaDto> dbSchemaDtoList =  dbSchemaService.getDbSchemaByType(type);
        return new ProvisioningDto<List<DbSchemaDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, dbSchemaDtoList);
    }
}
