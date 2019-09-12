package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.Neo4jInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.Neo4jInstanceService;
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
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/neojInstance")
@Api(tags = "Neo4jInstanceController | Neo4j 实例")
public class Neo4jInstanceController {

    @Autowired
    private Neo4jInstanceService neo4jInstanceService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param neo4jName
     * @param status
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "neo4jName", value = "neo4j 名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "neo4j 状态", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryNeo4jInstanceList(int pageNum, int pageSize, String neo4jName, Integer status) {
        Neo4jInstanceDto neo4jInstanceDto = new Neo4jInstanceDto();
        neo4jInstanceDto.setPageNum(pageNum);
        neo4jInstanceDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(neo4jName)){
            neo4jInstanceDto.setNeo4jName(neo4jName);
        }
        if (status != null){
            neo4jInstanceDto.setStatus(status);
        }
        PageInfoDto<Neo4jInstanceDto> pageInfoDto = neo4jInstanceService.queryNeo4jInstanceList(neo4jInstanceDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), Neo4jInstanceController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, Neo4jInstanceController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<Neo4jInstanceDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 添加
     * @param neo4jInstanceDto
     * @return
     */
    @ApiOperation(value = "添加 neo4j ecs", httpMethod = "POST")
    @ApiImplicitParam(name = "neo4jInstanceDto", value = "neo4j ecs neo4jInstanceDto", required = true, dataType = "Neo4jInstanceDto")
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> addNeo4jInstance(@RequestBody Neo4jInstanceDto neo4jInstanceDto){
        neo4jInstanceService.addNeo4jInstance(neo4jInstanceDto);
        return new ProvisioningDto<Neo4jInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 修改
     * @param neo4jInstanceDto
     * @return
     */
    @ApiOperation(value = "修改 neo4j ecs", httpMethod = "PUT")
    @ApiImplicitParam(name = "neo4jInstanceDto", value = "neo4j ecs neo4jInstanceDto", required = true, dataType = "Neo4jInstanceDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> editNeo4jInstance(@RequestBody Neo4jInstanceDto neo4jInstanceDto){
        neo4jInstanceService.editNeo4jInstance(neo4jInstanceDto);
        return new ProvisioningDto<Neo4jInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除 neo4j ecs", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "neo4j 数据库自增id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> deleteNeo4jInstance(@PathVariable Integer id){
        neo4jInstanceService.deleteNeo4jInstance(id);
        return new ProvisioningDto<Neo4jInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }



    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询详情 neo4j ecs", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "neo4j 数据库自增id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getNeo4jInstanceDetails(@PathVariable Integer id) {
        Neo4jInstanceDto neo4jInstanceDto = neo4jInstanceService.getNeo4jInstanceDetails(id);
        return new ProvisioningDto<Neo4jInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, neo4jInstanceDto);
    }

    /**
     * 创建公司后 创建 eno4j
     *
     * @param companyId
     * @param neo4jInstanceId
     * @return
     */
    @ApiOperation(value = "创建公司后 创建 eno4j", hidden = true)
    @RequestMapping(value = "/useNeo4jInstance", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useNeo4jInstance(@RequestParam Integer companyId, @RequestParam Integer neo4jInstanceId) {
        Neo4jInstanceDto neo4jInstanceDtoResponse = neo4jInstanceService.useNeo4jInstance(companyId, neo4jInstanceId);
        return new ProvisioningDto<Neo4jInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, neo4jInstanceDtoResponse);
    }
}
