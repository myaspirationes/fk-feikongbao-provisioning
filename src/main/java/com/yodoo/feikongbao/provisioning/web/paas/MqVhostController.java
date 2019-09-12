package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.MqVhostDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.MqVhostService;
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
 * @Description ：mq vhost
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@RestController
@RequestMapping(value = "/mqVhost")
@Api(tags = "MqVhostController | rabbitmq 创建 VHost")
public class MqVhostController {

    @Autowired
    private MqVhostService mqVhostService;

    /**
     * 条件分页查询
     * @param pageNum
     * @param pageSize
     * @param vhostName
     * @return
     */
    @ApiOperation(value = "条件分页查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = false, dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "多少行", required = false, dataType = "int", paramType = "query", example = "10"),
            @ApiImplicitParam(name = "vhostName", value = "vhost 名称", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryMqVhostList(int pageNum, int pageSize, String vhostName) {
        MqVhostDto mqVhostDto = new MqVhostDto();
        mqVhostDto.setPageNum(pageNum);
        mqVhostDto.setPageSize(pageSize);
        if (StringUtils.isNotBlank(vhostName)){
            mqVhostDto.setVhostName(vhostName);
        }
        PageInfoDto<MqVhostDto> pageInfoDto = mqVhostService.queryMqVHostList(mqVhostDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), MqVhostController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, MqVhostController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<MqVhostDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "vhost 详情", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "vhost数据库自增 id", paramType="path",required = true, dataType = "integer", example = "1")
    @RequestMapping(value = "item/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getMqVhostDetails(@PathVariable Integer id) {
        MqVhostDto mqVhostDto = mqVhostService.getMqVHostDetails(id);
        return new ProvisioningDto<MqVhostDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, mqVhostDto);
    }

    /**
     * 创建公司后 创建 消息队列
     *
     * @param companyId
     * @return
     */
    @ApiOperation(value = "创建公司后 创建 消息队列", hidden = true)
    @RequestMapping(value = "/useMqVhost", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useMqVhost(@RequestParam Integer companyId) {
        mqVhostService.useMqVHost(companyId);
        return new ProvisioningDto<MqVhostDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG);
    }
}
