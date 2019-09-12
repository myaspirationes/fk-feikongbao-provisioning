package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.DbInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.DbInstanceService;
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
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/8/28 0028
 */
@RestController
@RequestMapping(value = "/dbInstance")
@Api(tags = "DbInstanceController | db 实例")
public class DbInstanceController {

    @Autowired
    private DbInstanceService dbInstanceService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param ip
     * @param port
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "ip", value = "db实例ip地址", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "port", value = "db实例端口号", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> queryDbInstanceList(int pageNum, int pageSize, String ip, String port) {
        DbInstanceDto dbInstanceDto =  new DbInstanceDto();
        dbInstanceDto.setPageNum(pageNum);
        dbInstanceDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(ip)){
            dbInstanceDto.setIp(ip);
        }
        if (StringUtils.isNotBlank(port)){
            dbInstanceDto.setIp(port);
        }
        PageInfoDto<DbInstanceDto> pageInfoDto = dbInstanceService.queryDbInstanceList(dbInstanceDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), DbInstanceController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, DbInstanceController.class, Arrays.asList("db_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<DbInstanceDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @param dbInstanceDto
     * @return
     */
    @ApiOperation(value = "添加db实例", httpMethod = "POST")
    @ApiImplicitParam(name = "dbInstanceDto", value = "db实例 dbInstanceDto", required = true, dataType = "DbInstanceDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> addDbInstance(@RequestBody DbInstanceDto dbInstanceDto){
        dbInstanceService.addDbInstance(dbInstanceDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改
     * @param dbInstanceDto
     * @return
     */
    @ApiOperation(value = "修改db实例", httpMethod = "PUT")
    @ApiImplicitParam(name = "dbInstanceDto", value = "db实例 dbInstanceDto", required = true, dataType = "DbInstanceDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> editDbInstance(@RequestBody DbInstanceDto dbInstanceDto){
        dbInstanceService.editDbInstance(dbInstanceDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除db实例", httpMethod = "PUT")
    @ApiImplicitParam(name = "id", value = "数据库自增 id", paramType="path",required = true, dataType = "integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('db_manage')")
    public ProvisioningDto<?> deleteDbInstance(@PathVariable Integer id){
        dbInstanceService.deleteDbInstance(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
