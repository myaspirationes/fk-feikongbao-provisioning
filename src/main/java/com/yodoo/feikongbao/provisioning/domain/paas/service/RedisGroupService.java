package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.RedisGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.RedisGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：redis 组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class,transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class RedisGroupService {

    @Autowired
    private RedisGroupMapper redisGroupMapper;

    /**
     * 条件分页查询
     * @param redisGroupDto
     * @return
     */
    public PageInfoDto<RedisGroupDto> queryRedisGroupList(RedisGroupDto redisGroupDto) {
        RedisGroup redisGroup = new RedisGroup();
        if (redisGroupDto != null) {
            BeanUtils.copyProperties(redisGroupDto, redisGroup);
        }
        Page<?> pages = PageHelper.startPage(redisGroupDto.getPageNum(), redisGroupDto.getPageSize());
        List<RedisGroup> list = redisGroupMapper.select(redisGroup);
        List<RedisGroupDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(redisGroup1 -> {
                        RedisGroupDto dto = new RedisGroupDto();
                        BeanUtils.copyProperties(redisGroup1, dto);
                        dto.setTid(redisGroup1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<RedisGroupDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public RedisGroupDto getRedisGroupDetails(Integer id) {
        RedisGroup redisGroup = selectByPrimaryKey(id);
        RedisGroupDto redisGroupDto = new RedisGroupDto();
        if (redisGroup != null){
            BeanUtils.copyProperties(redisGroup, redisGroupDto);
            redisGroupDto.setTid(redisGroup.getId());
        }
        return redisGroupDto;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public RedisGroup selectByPrimaryKey(Integer id){
        return redisGroupMapper.selectByPrimaryKey(id);
    }
}