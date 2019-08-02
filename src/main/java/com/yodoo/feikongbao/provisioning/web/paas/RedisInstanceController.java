package com.yodoo.feikongbao.provisioning.web.paas;

import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.service.RedisInstanceService;
import com.yodoo.feikongbao.provisioning.enums.OperateCode;
import com.yodoo.feikongbao.provisioning.enums.SystemStatus;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.util.LinkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Description ：Redis 实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@RestController
@RequestMapping(value = "/redisInstance")
public class RedisInstanceController {

    @Autowired
    private RedisInstanceService redisInstanceService;

    /**
     * 条件分页查询
     *
     * @param redisInstanceDto
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> queryRedisInstanceList(RedisInstanceDto redisInstanceDto) {
        PageInfoDto<RedisInstanceDto> pageInfoDto = redisInstanceService.queryRedisInstanceList(redisInstanceDto);
        // 列表item导向
        LinkUtils.setItemListLink(pageInfoDto.getList(), RedisInstanceController.class);
        // 操作资源导向
        LinkUtils.setResourceLink(pageInfoDto, RedisInstanceController.class, Arrays.asList("company_manage"),
                OperateCode.ADD.getCode(), OperateCode.EDIT.getCode(), OperateCode.DELETE.getCode(), OperateCode.ITEM.getCode());
        return new ProvisioningDto<PageInfoDto<RedisInstanceDto>>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, pageInfoDto);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "item/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> getRedisInstanceDetails(@PathVariable Integer id) {
        RedisInstanceDto redisInstanceDto = redisInstanceService.getRedisInstanceDetails(id);
        return new ProvisioningDto<RedisInstanceDto>(SystemStatus.SUCCESS.getStatus(), BundleKey.SUCCESS, BundleKey.SUCCESS_MSG, redisInstanceDto);
    }

    /**
     * 使用缓存
     *
     * @param redisInstanceDto
     * @return
     */
    @RequestMapping(value = "/useRedisInstance", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('company_manage')")
    public ProvisioningDto<?> useRedisInstance(@RequestBody RedisInstanceDto redisInstanceDto) {
        return redisInstanceService.useRedisInstance(redisInstanceDto);
    }

}
