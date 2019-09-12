package com.yodoo.feikongbao.provisioning.web.system;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.*;
import com.yodoo.feikongbao.provisioning.domain.paas.service.*;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.dto.PublishProjectDto;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanySuperUserService;
import com.yodoo.feikongbao.provisioning.domain.system.service.PublishProjectService;
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
 * @Description ：公司管理
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/company")
@Api(tags = "CompanyController | 公司管理")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DbSchemaService dbSchemaService;

    @Autowired
    private RedisInstanceService redisInstanceService;

    @Autowired
    private SwiftProjectService swiftProjectService;

    @Autowired
    private MqVhostService mqVhostService;

    @Autowired
    private Neo4jInstanceService neo4jInstanceService;

    @Autowired
    private PublishProjectService publishProjectService;

    @Autowired
    private CompanySuperUserService companySuperUserService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param companyName
     * @param companyCode
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "companyName", value = "公司名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "companyCode", value = "公司 code", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryCompanyList(int pageNum, int pageSize, String companyName, String companyCode) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setPageNum(pageNum);
        companyDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(companyName)){
            companyDto.setCompanyName(companyName);
        }
        if (StringUtils.isNotBlank(companyCode)){
            companyDto.setCompanyCode(companyCode);
        }
        PageInfoDto<CompanyDto> pageInfoDto = companyService.queryCompanyList(companyDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), CompanyController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, CompanyController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode(), "companyCode");
        return new ProvisioningDto<PageInfoDto<CompanyDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 校验公司code是否存在
     *
     * @return
     */
    @ApiOperation(value = "校验公司code是否存在", httpMethod = "POST")
    @ApiImplicitParam(name = "companyCode", value = "公司代码 companyCode", required = true, dataType = "String", example = "yodoo123")
    @RequestMapping(value = "companyCode/{companyCode}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getCompanyByCompanyCode(@PathVariable String companyCode) {
        CompanyDto companyDto = companyService.getCompanyByCompanyCode(companyCode);
        return new ProvisioningDto<CompanyDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, companyDto);
    }

    /**
     * 更新
     *
     * @param companyDto
     * @return
     */
    @ApiOperation(value = "更新", httpMethod = "PUT")
    @ApiImplicitParam(name = "companyDto", value = "公司代码 companyDto", required = true, dataType = "CompanyDto")
    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> editCompany(@RequestBody CompanyDto companyDto) {
        companyService.editCompany(companyDto);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 删除 TODO
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "公司表自增 id", required = true, dataType = "Integer", example = "0")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return new ProvisioningDto<String>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询详情", httpMethod = "POST")
    @ApiImplicitParam(name = "id", value = "公司表自增 id", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('permission_manage')")
    public ProvisioningDto<?> getCompanyDetails(@PathVariable Integer id) {
        CompanyDto companyDto = companyService.getCompanyDetails(id);
        return new ProvisioningDto<CompanyDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, companyDto);
    }

    /**
     * 第一步：创建公司 TODO
     *
     * @param companyDto
     * @return
     */
    @ApiOperation(value = "第一步：创建公司", httpMethod = "POST")
    @ApiImplicitParam(name = "companyDto", value = "公司 companyDto", required = true, dataType = "CompanyDto")
    @RequestMapping(value = "/addCompany", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> addCompany(@RequestBody CompanyDto companyDto) {
        CompanyDto companyDtoResponse = companyService.addCompany(companyDto);
        return new ProvisioningDto<CompanyDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, companyDtoResponse);
    }

    /**
     * 第二步：数据库
     * @param companyId
     * @param dbGroupId
     * @param targetVersion
     * @return
     */
    @ApiOperation(value = "第二步：数据库", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司表自增 id", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "dbGroupId", value = "db 数据库表自增 id", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "targetVersion", value = "数据库版本号", required = true, dataType = "String", paramType = "query", example = "R1907")
    })
    @RequestMapping(value = "/useDbSchema", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useDbSchema(@RequestParam Integer companyId, @RequestParam Integer dbGroupId, @RequestParam(required = true) String targetVersion) {
        DbSchemaDto dbSchemaDto = new DbSchemaDto();
        dbSchemaDto.setCompanyId(companyId);
        dbSchemaDto.setDbGroupId(dbGroupId);
        dbSchemaDto.setTargetVersion(targetVersion);
        DbSchemaDto dbSchemaDtoResponse = dbSchemaService.useDbSchema(dbSchemaDto);
        return new ProvisioningDto<DbSchemaDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, dbSchemaDtoResponse);
    }

    /**
     * 第三步：缓存
     * @param companyId
     * @param redisGroupId
     * @return
     */
    @ApiOperation(value = "第三步：缓存", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司表自增 id", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "redisGroupId", value = "redisGroup 数据库表自增 id", required = true, dataType = "Integer", paramType = "query", example = "1"),
    })
    @RequestMapping(value = "/useRedisInstance", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useRedisInstance(@RequestParam Integer companyId, @RequestParam Integer redisGroupId) {
        RedisInstanceDto redisInstanceDto = new RedisInstanceDto();
        redisInstanceDto.setCompanyId(companyId);
        redisInstanceDto.setRedisGroupId(redisGroupId);
        RedisInstanceDto redisInstanceDtoResponse = redisInstanceService.useRedisInstance(redisInstanceDto);
        return new ProvisioningDto<RedisInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, redisInstanceDtoResponse);
    }

    /**
     * 第四步：swift 创建存储对象
     *
     * @param swiftProjectDto
     * @return
     */
    @ApiOperation(value = "第四步：swift 创建存储对象", httpMethod = "POST")
    @ApiImplicitParam(name = "swiftProjectDto", value = "swift swiftProjectDto", required = true, dataType = "SwiftProjectDto")
    @RequestMapping(value = "/useSwiftProject", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useSwiftProject(@RequestBody SwiftProjectDto swiftProjectDto) {
        SwiftProjectDto swiftProjectDtoResponse = swiftProjectService.useSwiftProject(swiftProjectDto);
        return new ProvisioningDto<SwiftProjectDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, swiftProjectDtoResponse);
    }

    /**
     * 第五步：消息队列
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "第五步：消息队列", httpMethod = "GET")
    @ApiImplicitParam(name = "companyId", value = "公司表自增 id", required = true, dataType = "Integer", paramType = "query", example = "1")
    @RequestMapping(value = "/useMqVhost", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useMqVhost(@RequestParam Integer companyId) {
        mqVhostService.useMqVHost(companyId);
        return new ProvisioningDto<MqVhostDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 第六步：创建 eno4j
     *
     * @param companyId
     * @param neo4jInstanceId
     * @return
     */
    @ApiOperation(value = "第六步：创建 eno4j", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司表自增 id", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "neo4jInstanceId", value = "neo4jInstance数据库表自增 id", required = true, dataType = "Integer", paramType = "query", example = "1"),
    })
    @RequestMapping(value = "/useNeo4jInstance", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useNeo4jInstance(@RequestParam Integer companyId, @RequestParam Integer neo4jInstanceId) {
        Neo4jInstanceDto neo4jInstanceDtoResponse = neo4jInstanceService.useNeo4jInstance(companyId, neo4jInstanceId);
        return new ProvisioningDto<Neo4jInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, neo4jInstanceDtoResponse);
    }

    /**
     * 第七步：部署的项目
     *
     * @param publishProjectDto
     * @return
     */
    @ApiOperation(value = "第七步：部署的项目", httpMethod = "POST")
    @ApiImplicitParam(name = "publishProjectDto", value = "项目 publishProjectDto", required = true, dataType = "PublishProjectDto")
    @RequestMapping(value = "/createPublishProjects", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> createPublishProjects(@RequestBody PublishProjectDto publishProjectDto) {
        publishProjectService.createPublishProjects(publishProjectDto);
        return new ProvisioningDto<>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 第八步：创建超级用户
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "第八步：创建超级用户", httpMethod = "POST")
    @ApiImplicitParam(name = "companyId", value = "项目 companyId", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "/createSuperUser/{companyId}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> createSuperUser(@PathVariable Integer companyId) {
        companySuperUserService.createSuperUser(companyId);
        return new ProvisioningDto<>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 第九步：发布项目
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "第九步：发布项目", httpMethod = "POST")
    @ApiImplicitParam(name = "companyId", value = "项目 companyId", required = true, dataType = "Integer", example = "1")
    @RequestMapping(value = "/publishProjects/{companyId}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> publishProjectsForCompanyCreateFinish(@PathVariable Integer companyId) {
        publishProjectService.publishProjectsForCompanyCreateFinish(companyId);
        return new ProvisioningDto<>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
