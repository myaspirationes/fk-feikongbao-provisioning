package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.Neo4jInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.Neo4jInstanceService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/neojInstance")
public class Neo4jInstanceController {

    @Autowired
    private Neo4jInstanceService neo4jInstanceService;

    /**
     * 条件分页查询
     * @param neo4jInstanceDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryNeo4jInstanceList(Neo4jInstanceDto neo4jInstanceDto){
        PageInfoDto<Neo4jInstanceDto> pageInfoDto = neo4jInstanceService.queryNeo4jInstanceList(neo4jInstanceDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), Neo4jInstanceController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, Neo4jInstanceController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<Neo4jInstanceDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getNeo4jInstanceDetails(@PathVariable Integer id){
        Neo4jInstanceDto neo4jInstanceDto = neo4jInstanceService.getNeo4jInstanceDetails(id);
        return new ProvisioningDto<Neo4jInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, neo4jInstanceDto);
    }
}
