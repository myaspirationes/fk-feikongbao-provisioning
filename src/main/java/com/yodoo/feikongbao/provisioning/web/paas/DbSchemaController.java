package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbSchemaDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.DbSchemaService;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 创建公司，选用数据库
     * @param dbSchemaDto
     * @return
     */
    @RequestMapping(value = "/useDbSchema", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useDbSchema(@RequestBody DbSchemaDto dbSchemaDto){
        DbSchemaDto dbSchemaDto1 = dbSchemaService.useDbSchema(dbSchemaDto);
        return new ProvisioningDto<DbSchemaDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, dbSchemaDto1);
    }
}
