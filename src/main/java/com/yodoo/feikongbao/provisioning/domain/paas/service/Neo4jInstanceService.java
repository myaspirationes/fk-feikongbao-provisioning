package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.Neo4jInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.Neo4jInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.Neo4jInstanceMapper;
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
 * @Description ：TODO
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class Neo4jInstanceService {

    @Autowired
    private Neo4jInstanceMapper neo4jInstanceMapper;

    /**
     * 条件分页查询
     * @param neo4jInstanceDto
     * @return
     */
    public PageInfoDto<Neo4jInstanceDto> queryNeo4jInstanceList(Neo4jInstanceDto neo4jInstanceDto) {
        Neo4jInstance neo4jInstance = new Neo4jInstance();
        if (neo4jInstanceDto != null) {
            BeanUtils.copyProperties(neo4jInstanceDto, neo4jInstance);
        }
        Page<?> pages = PageHelper.startPage(neo4jInstanceDto.getPageNum(), neo4jInstanceDto.getPageSize());
        List<Neo4jInstance> list = neo4jInstanceMapper.select(neo4jInstance);
        List<Neo4jInstanceDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(neo4jInstance1 -> {
                        Neo4jInstanceDto dto = new Neo4jInstanceDto();
                        BeanUtils.copyProperties(neo4jInstance1, dto);
                        dto.setTid(neo4jInstance1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<Neo4jInstanceDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public Neo4jInstanceDto getNeo4jInstanceDetails(Integer id) {
        Neo4jInstance neo4jInstance = selectByPrimaryKey(id);
        Neo4jInstanceDto neo4jInstanceDto = new Neo4jInstanceDto();
        if (neo4jInstance != null){
            BeanUtils.copyProperties(neo4jInstance, neo4jInstanceDto);
            neo4jInstanceDto.setTid(neo4jInstance.getId());
        }
        return neo4jInstanceDto;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public Neo4jInstance selectByPrimaryKey(Integer id){
        return neo4jInstanceMapper.selectByPrimaryKey(id);
    }
}