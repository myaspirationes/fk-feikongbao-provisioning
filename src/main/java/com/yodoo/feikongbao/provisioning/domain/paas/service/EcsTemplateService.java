package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDetailDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.EcsTemplate;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.EcsTemplateMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：ecs 模板
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class EcsTemplateService {

    @Autowired
    private EcsTemplateMapper ecsTemplateMapper;

    @Autowired
    private EcsTemplateDetailService ecsTemplateDetailService;

    /**
     * 条件分页查询
     * @param ecsInstanceDto
     * @return
     */
    public PageInfoDto<EcsTemplateDto> queryEcsTemplateList(EcsTemplateDto ecsInstanceDto) {
        Example example = new Example(EcsTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(ecsInstanceDto.getPageNum(), ecsInstanceDto.getPageSize());
        List<EcsTemplate> ecsInstanceList = ecsTemplateMapper.selectByExample(example);
        List<EcsTemplateDto> collect = copyProperties(ecsInstanceList);
        return new PageInfoDto<EcsTemplateDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加
     * @param ecsInstanceDto
     */
    public Integer addEcsTemplate(EcsTemplateDto ecsInstanceDto) {
        addEcsTemplateParameterCheck(ecsInstanceDto);
        EcsTemplate ecsTemplate = new EcsTemplate();
        BeanUtils.copyProperties(ecsInstanceDto, ecsTemplate);
        return ecsTemplateMapper.insertSelective(ecsTemplate);
    }

    /**
     * 修改
     * @param ecsInstanceDto
     * @return
     */
    public Integer editEcsTemplate(EcsTemplateDto ecsInstanceDto) {
        EcsTemplate ecsTemplate = editEcsTemplateParameterCheck(ecsInstanceDto);
        BeanUtils.copyProperties(ecsInstanceDto, ecsTemplate);
        return ecsTemplateMapper.updateByPrimaryKeySelective(ecsTemplate);
    }

    /**
     * 删除
     * @param id
     */
    public Integer deleteEcsTemplate(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        List<EcsTemplateDetailDto> ecsTemplateDetailDtoList = ecsTemplateDetailService.selectEcsTemplateDetailByEcsTemplateId(id);
        if (!CollectionUtils.isEmpty(ecsTemplateDetailDtoList)){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_IS_USE, BundleKey.ECS_TEMPLATE_IS_USE_MSG);
        }
       return ecsTemplateMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public EcsTemplate selectByPrimaryKey(Integer id){
        return ecsTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据code查询模板信息
     * @Date 9:53 2019/6/11
     **/
    public EcsTemplateDto getEcsTemplateByEcsType(String ecsType) {
        if (StringUtils.isBlank(ecsType)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = getExampleByEcsType(ecsType);
        EcsTemplate ecsTemplate = ecsTemplateMapper.selectOneByExample(example);
        if (ecsTemplate != null) {
            EcsTemplateDto ecsTemplateDto = copyProperties(ecsTemplate);
            List<EcsTemplateDetailDto> ecsTemplateDetailDtoList = ecsTemplateDetailService.selectEcsTemplateDetailByEcsTemplateId(ecsTemplate.getId());
            if (!CollectionUtils.isEmpty(ecsTemplateDetailDtoList)){
                ecsTemplateDto.setEcsTemplateDetailDtoList(ecsTemplateDetailDtoList);
            }
            return ecsTemplateDto;
        }
        return null;
    }

    /**
     * 更新参数校验
     * @param ecsInstanceDto
     * @return
     */
    private EcsTemplate editEcsTemplateParameterCheck(EcsTemplateDto ecsInstanceDto) {
        if (ecsInstanceDto == null || ecsInstanceDto.getTid() == null || ecsInstanceDto.getTid() < 0 || StringUtils.isBlank(ecsInstanceDto.getEcsType())
                || StringUtils.isBlank(ecsInstanceDto.getName()) || StringUtils.isBlank(ecsInstanceDto.getRemark())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        EcsTemplate ecsTemplate = selectByPrimaryKey(ecsInstanceDto.getTid());
        if (ecsTemplate == null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_NO_EXIST, BundleKey.ECS_TEMPLATE_NO_EXIST_MSG);
        }
        Example example = new Example(EcsTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", ecsInstanceDto.getTid());
        criteria.andEqualTo("ecsType", ecsInstanceDto.getEcsType());
        EcsTemplate ecsTemplateNot = ecsTemplateMapper.selectOneByExample(example);
        if (ecsTemplateNot != null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_ALREADY_EXIST, BundleKey.ECS_TEMPLATE_ALREADY_EXIST_MSG);
        }
        return ecsTemplate;
    }

    /**
     * 添加参数校验
     * @param ecsInstanceDto
     */
    private void addEcsTemplateParameterCheck(EcsTemplateDto ecsInstanceDto) {
        if (ecsInstanceDto == null || StringUtils.isBlank(ecsInstanceDto.getEcsType()) || StringUtils.isBlank(ecsInstanceDto.getName())
                || StringUtils.isBlank(ecsInstanceDto.getRemark())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = getExampleByEcsType(ecsInstanceDto.getEcsType());
        EcsTemplate ecsTemplate = ecsTemplateMapper.selectOneByExample(example);
        if (ecsTemplate != null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_ALREADY_EXIST, BundleKey.ECS_TEMPLATE_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 获取 example
     * @param ecsType
     * @return
     */
    private Example getExampleByEcsType(String ecsType){
        Example example = new Example(EcsTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ecsType", ecsType);
        return example;
    }

    /**
     * 复制
     * @param ecsTemplateDetailList
     * @return
     */
    private List<EcsTemplateDto> copyProperties(List<EcsTemplate> ecsTemplateDetailList){
        if (!CollectionUtils.isEmpty(ecsTemplateDetailList)){
            return ecsTemplateDetailList.stream()
                    .filter(Objects::nonNull)
                    .map(ecsTemplate -> {
                        return copyProperties(ecsTemplate);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param ecsTemplate
     * @return
     */
    private EcsTemplateDto copyProperties(EcsTemplate ecsTemplate){
        if (ecsTemplate != null){
            EcsTemplateDto ecsTemplateDto = new EcsTemplateDto();
            BeanUtils.copyProperties(ecsTemplate, ecsTemplateDto);
            ecsTemplateDto.setTid(ecsTemplate.getId());
            return ecsTemplateDto;
        }
        return null;
    }
}
