package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.RedisInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.RedisInstanceMapper;
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
 * @Description ：redis 实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class RedisInstanceService {

    @Autowired
    private RedisInstanceMapper redisInstanceMapper;

    /**
     * 条件分页查询
     * @param redisInstanceDto
     * @return
     */
    public PageInfoDto<RedisInstanceDto> queryRedisInstanceList(RedisInstanceDto redisInstanceDto) {
        RedisInstance redisInstance = new RedisInstance();
        if (redisInstanceDto != null) {
            BeanUtils.copyProperties(redisInstanceDto, redisInstance);
        }
        Page<?> pages = PageHelper.startPage(redisInstanceDto.getPageNum(), redisInstanceDto.getPageSize());
        List<RedisInstance> list = redisInstanceMapper.select(redisInstance);
        List<RedisInstanceDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(redisInstance1 -> {
                        RedisInstanceDto dto = new RedisInstanceDto();
                        BeanUtils.copyProperties(redisInstance1, dto);
                        dto.setTid(redisInstance1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<RedisInstanceDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public RedisInstanceDto getRedisInstanceDetails(Integer id) {
        RedisInstance redisInstance = selectByPrimaryKey(id);
        RedisInstanceDto redisInstanceDto = new RedisInstanceDto();
        if (redisInstance != null){
            BeanUtils.copyProperties(redisInstance, redisInstanceDto);
            redisInstanceDto.setTid(redisInstance.getId());
        }
        return redisInstanceDto;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public RedisInstance selectByPrimaryKey(Integer id){
        return redisInstanceMapper.selectByPrimaryKey(id);
    }

    /**
     * 使用缓存 TODO
     * @param redisInstanceDto
     * @return
     */
    public RedisInstanceDto useRedisInstance(RedisInstanceDto redisInstanceDto) {
        useRedisInstanceParameterCheck(redisInstanceDto);
        return null;
    }

    /**
     * 使用缓存校验 TODO
     * @param redisInstanceDto
     */
    private void useRedisInstanceParameterCheck(RedisInstanceDto redisInstanceDto) {

    }
}
