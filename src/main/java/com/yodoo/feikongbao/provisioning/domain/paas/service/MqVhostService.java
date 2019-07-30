package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.MqVhostDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.MqVhost;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.MqVhostMapper;
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
 * @Description ：mq vhost
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class MqVhostService {

    @Autowired
    private MqVhostMapper mqVhostMapper;

    /**
     * 条件查询
     * @param mqVhostDto
     * @return
     */
    public PageInfoDto<MqVhostDto> queryMqVhostList(MqVhostDto mqVhostDto) {
        // 设置查询条件
        MqVhost mqVhost = new MqVhost();
        if (mqVhostDto != null) {
            BeanUtils.copyProperties(mqVhostDto, mqVhost);
        }
        Page<?> pages = PageHelper.startPage(mqVhostDto.getPageNum(), mqVhostDto.getPageSize());
        List<MqVhost> list = mqVhostMapper.select(mqVhost);
        List<MqVhostDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(mqVhost1 -> {
                        MqVhostDto dto = new MqVhostDto();
                        BeanUtils.copyProperties(mqVhost1, dto);
                        dto.setTid(mqVhost1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<MqVhostDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public MqVhostDto getMqVhostDetails(Integer id) {
        MqVhost mqVhost = selectByPrimaryKey(id);
        MqVhostDto mqVhostDto = new MqVhostDto();
        if (mqVhost != null){
            BeanUtils.copyProperties(mqVhost, mqVhostDto);
            mqVhostDto.setTid(mqVhost.getId());
        }
        return mqVhostDto;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public MqVhost selectByPrimaryKey(Integer id){
        return mqVhostMapper.selectByPrimaryKey(id);
    }
}
