package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.RedisGroupService;
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
 * @Description ：redis组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@RestController
@RequestMapping(value = "/redisGroup")
public class RedisGroupController {

    @Autowired
    private RedisGroupService redisGroupService;

    /**
     * 条件分页查询
     *
     * @param redisGroupDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryRedisGroupList(RedisGroupDto redisGroupDto) {
        PageInfoDto<RedisGroupDto> pageInfoDto = redisGroupService.queryRedisGroupList(redisGroupDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), RedisGroupController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, RedisGroupController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<RedisGroupDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getRedisGroupDetails(@PathVariable Integer id) {
        RedisGroupDto redisGroupDto = redisGroupService.getRedisGroupDetails(id);
        return new ProvisioningDto<RedisGroupDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, redisGroupDto);
    }

}
