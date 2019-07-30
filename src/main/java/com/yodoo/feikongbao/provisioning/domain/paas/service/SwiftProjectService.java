package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.SwiftProjectDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.SwiftProject;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.SwiftProjectMapper;
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
 * @Description ：存储
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class SwiftProjectService {

    @Autowired
    private SwiftProjectMapper swiftProjectMapper;

    /**
     * 条件分页查询
     * @param swiftProjectDto
     * @return
     */
    public PageInfoDto<SwiftProjectDto> querySwiftProjectList(SwiftProjectDto swiftProjectDto) {
        SwiftProject swiftProject = new SwiftProject();
        if (swiftProjectDto != null) {
            BeanUtils.copyProperties(swiftProjectDto, swiftProject);
        }
        Page<?> pages = PageHelper.startPage(swiftProjectDto.getPageNum(), swiftProjectDto.getPageSize());
        List<SwiftProject> list = swiftProjectMapper.select(swiftProject);
        List<SwiftProjectDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(swiftProject1 -> {
                        SwiftProjectDto dto = new SwiftProjectDto();
                        BeanUtils.copyProperties(swiftProject1, dto);
                        dto.setTid(swiftProject1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<SwiftProjectDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public SwiftProjectDto getSwiftProjectDetails(Integer id) {
        SwiftProject swiftProject = selectByPrimaryKey(id);
        SwiftProjectDto swiftProjectDto = new SwiftProjectDto();
        if (swiftProject != null){
            BeanUtils.copyProperties(swiftProject, swiftProjectDto);
            swiftProjectDto.setTid(swiftProject.getId());
        }
        return swiftProjectDto;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public SwiftProject selectByPrimaryKey(Integer id){
        return swiftProjectMapper.selectByPrimaryKey(id);
    }
}
