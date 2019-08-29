package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.system.dto.DictionaryDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Dictionary;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.DictionaryMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：字典表
 * @Author ：jinjun_luo
 * @Date ： 2019/7/26 0026
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    /**
     * 分页条件查询
     *
     * @param dictionaryDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public PageInfoDto<DictionaryDto> queryDictionaryList(DictionaryDto dictionaryDto) {
        Example example = new Example(Dictionary.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(dictionaryDto.getPageNum(), dictionaryDto.getPageSize());
        List<Dictionary> select = dictionaryMapper.selectByExample(example);
        List<DictionaryDto> collect = copyProperties(select);
        return new PageInfoDto<DictionaryDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加
     *
     * @param dictionaryDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public Integer addDictionary(DictionaryDto dictionaryDto) {
        addDictionaryParameterCheck(dictionaryDto);

        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDto, dictionary);
        return dictionaryMapper.insertSelective(dictionary);
    }

    /**
     * 更新
     *
     * @param dictionaryDto
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public Integer editDictionary(DictionaryDto dictionaryDto) {
        Dictionary dictionary = editDictionaryParameterCheck(dictionaryDto);
        return dictionaryMapper.updateByPrimaryKeySelective(dictionary);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public Integer deleteDictionary(Integer id) {
        deleteDictionaryParameterCheck(id);
        return dictionaryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dictionary_manage')")
    public DictionaryDto getDictionaryDetails(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 删除参数校验
     *
     * @param id
     * @return
     */
    private void deleteDictionaryParameterCheck(Integer id) {
        if (id == null || id < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Dictionary dictionary = selectByPrimaryKey(id);
        if (dictionary == null) {
            throw new ProvisioningException(BundleKey.DICTIONARY_NOT_EXIST, BundleKey.DICTIONARY_NOT_EXIST_MSG);
        }
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    private Dictionary selectByPrimaryKey(Integer id) {
        return dictionaryMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新参数校验
     *
     * @param dictionaryDto
     * @return
     */
    private Dictionary editDictionaryParameterCheck(DictionaryDto dictionaryDto) {
        if (dictionaryDto == null || dictionaryDto.getTid() == null || dictionaryDto.getTid() < 0
                || StringUtils.isBlank(dictionaryDto.getType()) || StringUtils.isBlank(dictionaryDto.getCode())
                || StringUtils.isBlank(dictionaryDto.getName()) || StringUtils.isBlank(dictionaryDto.getValue())
                || StringUtils.isBlank(dictionaryDto.getRemark())) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Dictionary dictionary = selectByPrimaryKey(dictionaryDto.getTid());
        if (dictionary == null) {
            throw new ProvisioningException(BundleKey.DICTIONARY_NOT_EXIST, BundleKey.DICTIONARY_NOT_EXIST_MSG);
        }
        Dictionary dictionarySelf = dictionaryMapper.selectDictionaryInAdditionToItself(dictionaryDto.getTid(), dictionaryDto.getType(), dictionaryDto.getCode(), dictionaryDto.getName(), dictionaryDto.getValue());
        if (dictionarySelf != null) {
            throw new ProvisioningException(BundleKey.DICTIONARY_ALREADY_EXIST, BundleKey.DICTIONARY_ALREADY_EXIST_MSG);
        }
        if (StringUtils.isNoneBlank(dictionaryDto.getType())) {
            dictionary.setType(dictionaryDto.getType());
        }
        if (StringUtils.isNoneBlank(dictionaryDto.getCode())) {
            dictionary.setCode(dictionaryDto.getCode());
        }
        if (StringUtils.isNoneBlank(dictionaryDto.getName())) {
            dictionary.setName(dictionaryDto.getName());
        }
        if (StringUtils.isNoneBlank(dictionaryDto.getValue())) {
            dictionary.setValue(dictionaryDto.getValue());
        }
        if (StringUtils.isNoneBlank(dictionaryDto.getRemark())) {
            dictionary.setRemark(dictionaryDto.getRemark());
        }
        return dictionary;
    }

    /**
     * 添加参数校验
     *
     * @param dictionaryDto
     * @return
     */
    private void addDictionaryParameterCheck(DictionaryDto dictionaryDto) {
        if (dictionaryDto == null || StringUtils.isBlank(dictionaryDto.getType()) || StringUtils.isBlank(dictionaryDto.getCode())
                || StringUtils.isBlank(dictionaryDto.getName()) || StringUtils.isBlank(dictionaryDto.getValue())
                || StringUtils.isBlank(dictionaryDto.getRemark())) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询是否有相同的数据，有不添加
        Example example = new Example(Dictionary.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", dictionaryDto.getType());
        criteria.andEqualTo("code", dictionaryDto.getCode());
        criteria.andEqualTo("name", dictionaryDto.getName());
        criteria.andEqualTo("value", dictionaryDto.getValue());
        Dictionary dictionaryResponse = dictionaryMapper.selectOneByExample(example);
        if (dictionaryResponse != null) {
            throw new ProvisioningException(BundleKey.DICTIONARY_ALREADY_EXIST, BundleKey.DICTIONARY_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 复制
     * @param dictionaryList
     * @return
     */
    private List<DictionaryDto> copyProperties(List<Dictionary> dictionaryList){
        if (!CollectionUtils.isEmpty(dictionaryList)){
            return dictionaryList.stream()
                    .filter(Objects::nonNull)
                    .map(dictionary -> {
                        return copyProperties(dictionary);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param dictionary
     * @return
     */
    private DictionaryDto copyProperties(Dictionary dictionary){
        if (dictionary != null){
            DictionaryDto dictionaryDto = new DictionaryDto();
            BeanUtils.copyProperties(dictionary, dictionaryDto);
            dictionaryDto.setTid(dictionary.getId());
            return dictionaryDto;
        }
        return null;
    }
}
