package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.common.dto.ProvisioningDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.DictionaryDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Dictionary;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.DictionaryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @Date ： 2019/7/26 0026
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    /**
     * 分页条件查询
     * @param dictionaryDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dictionary')")
    public PageInfoDto<DictionaryDto> queryDictionaryList(DictionaryDto dictionaryDto) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDto, dictionary);
        Page<?> pages = PageHelper.startPage(dictionaryDto.getPageNum(), dictionaryDto.getPageSize());
        List<Dictionary> select = dictionaryMapper.select(dictionary);
        List<DictionaryDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(select)){
            collect = select.stream()
                    .filter(Objects::nonNull)
                    .map(dictionary1 -> {
                        DictionaryDto dto = new DictionaryDto();
                        BeanUtils.copyProperties(dictionary1, dto);
                        dto.setTid(dictionary1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<DictionaryDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    @PreAuthorize("hasAnyAuthority('dictionary')")
    public ProvisioningDto<?> addDictionary(DictionaryDto dictionaryDto) {
        ProvisioningDto provisioningDto = addDictionaryParameterCheck(dictionaryDto);
        return null;
    }

    private ProvisioningDto addDictionaryParameterCheck(DictionaryDto dictionaryDto) {
        return null;
    }
}
