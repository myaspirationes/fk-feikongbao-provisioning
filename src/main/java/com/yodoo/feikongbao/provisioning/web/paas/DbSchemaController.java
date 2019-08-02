package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbSchemaDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.DbSchemaService;
import com.yodoo.feikongbao.provisioning.domain.paas.service.RedisInstanceService;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * 创建公司，选用数据库
     *
     * @param dbSchemaDto
     * @return
     */
    @RequestMapping(value = "/useDbSchema", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useDbSchema(@RequestBody DbSchemaDto dbSchemaDto) {
        return dbSchemaService.useDbSchema(dbSchemaDto);
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
