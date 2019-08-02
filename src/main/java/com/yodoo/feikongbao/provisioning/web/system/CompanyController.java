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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Description ：公司管理
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/company")
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
     *
     * @param companyDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryCompanyList(CompanyDto companyDto) {
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
    @RequestMapping(value = "/addCompany", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> addCompany(@RequestBody CompanyDto companyDto) {
        return companyService.addCompany(companyDto);
    }

    /**
     * 第二步：数据库
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
     * 第三步：缓存
     *
     * @param redisInstanceDto
     * @return
     */
    @RequestMapping(value = "/useRedisInstance", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useRedisInstance(@RequestBody RedisInstanceDto redisInstanceDto) {
        return redisInstanceService.useRedisInstance(redisInstanceDto);
    }

    /**
     * 第四步：创建存储对象
     *
     * @param swiftProjectDto
     * @return
     */
    @RequestMapping(value = "/useSwiftProject", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useSwiftProject(@RequestBody SwiftProjectDto swiftProjectDto) {
        return swiftProjectService.useSwiftProject(swiftProjectDto);
    }

    /**
     * 第五步：消息队列
     *
     * @param mqVhostDto
     * @return
     */
    @RequestMapping(value = "/useMqVhost", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useMqVhost(@RequestBody MqVhostDto mqVhostDto) {
        return mqVhostService.useMqVhost(mqVhostDto);
    }

    /**
     * 第六步：创建 eno4j
     *
     * @param neo4jInstanceDto
     * @return
     */
    @RequestMapping(value = "/useNeo4jInstance", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useNeo4jInstance(@RequestBody Neo4jInstanceDto neo4jInstanceDto) {
        return neo4jInstanceService.useNeo4jInstance(neo4jInstanceDto);
    }

    /**
     * 第七步：部署的项目
     *
     * @param projectList
     * @return
     */
    @RequestMapping(value = "/createPublishProjects", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> createPublishProjects(List<PublishProjectDto> projectList) {
        publishProjectService.createPublishProjects(projectList);
        return new ProvisioningDto<>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }

    /**
     * 第八步：创建超级用户
     *
     * @param companyId
     * @return
     */
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
    @RequestMapping(value = "/publishProjects/{companyId}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> publishProjectsForCompanyCreateFinish(@PathVariable Integer companyId) {
        publishProjectService.publishProjectsForCompanyCreateFinish(companyId);
        return new ProvisioningDto<>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }


}
