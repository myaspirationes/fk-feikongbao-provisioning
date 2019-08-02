package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.MqVhostDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.MqVhostService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
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
public class MqVhostController {

    @Autowired
    private MqVhostService mqVhostService;

    /**
     * 条件分页查询
     *
     * @param mqVhostDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryMqVhostList(MqVhostDto mqVhostDto) {
        PageInfoDto<MqVhostDto> pageInfoDto = mqVhostService.queryMqVhostList(mqVhostDto);
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
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getMqVhostDetails(@PathVariable Integer id) {
        MqVhostDto mqVhostDto = mqVhostService.getMqVhostDetails(id);
        return new ProvisioningDto<MqVhostDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, mqVhostDto);
    }

    /**
     * 创建公司后 创建 消息队列
     *
     * @param mqVhostDto
     * @return
     */
    @RequestMapping(value = "/useMqVhost", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useMqVhost(@RequestBody MqVhostDto mqVhostDto) {
        return mqVhostService.useMqVhost(mqVhostDto);
    }
}
