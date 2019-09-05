package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDetailDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.EcsTemplate;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.EcsTemplateDetail;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.EcsTemplateDetailMapper;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：ecs 模板详情
 * @Author ：jinjun_luo
 * @Date ： 2019/9/3 0003
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class EcsTemplateDetailService {

    @Autowired
    private EcsTemplateDetailMapper ecsTemplateDetailMapper;

    @Autowired
    private EcsTemplateService ecsTemplateService;

    /**
     * 条件分页查询
     * @param ecsTemplateDetailDto
     * @return
     */
    public PageInfoDto<EcsTemplateDetailDto> queryEcsTemplateDetailList(EcsTemplateDetailDto ecsTemplateDetailDto) {
        Example example = new Example(EcsTemplateDetail.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(ecsTemplateDetailDto.getPageNum(), ecsTemplateDetailDto.getPageSize());
        List<EcsTemplateDetail> ecsInstanceList = ecsTemplateDetailMapper.selectByExample(example);
        List<EcsTemplateDetailDto> collect = copyProperties(ecsInstanceList);
        return new PageInfoDto<EcsTemplateDetailDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 添加
     * @param ecsTemplateDetailDto
     */
    public Integer addEcsTemplateDetail(EcsTemplateDetailDto ecsTemplateDetailDto) {
        addEcsTemplateDetailParameterCheck(ecsTemplateDetailDto);
        EcsTemplateDetail ecsTemplateDetail = new EcsTemplateDetail();
        BeanUtils.copyProperties(ecsTemplateDetailDto, ecsTemplateDetail);
        return ecsTemplateDetailMapper.insertSelective(ecsTemplateDetail);
    }

    /**
     * 更新
     * @param ecsTemplateDetailDto
     */
    public Integer editEcsTemplateDetail(EcsTemplateDetailDto ecsTemplateDetailDto) {
        EcsTemplateDetail ecsTemplateDetail = editEcsTemplateDetailParameterCheck(ecsTemplateDetailDto);
        BeanUtils.copyProperties(ecsTemplateDetailDto, ecsTemplateDetail);
        return ecsTemplateDetailMapper.updateByPrimaryKeySelective(ecsTemplateDetail);
    }

    /**
     * 删除
     * @param id
     */
    public Integer deleteEcsTemplateDetail(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        return ecsTemplateDetailMapper.deleteByPrimaryKey(id);
    }

    /**
     * 通过模板id 查询
     * @param ecsTemplateId
     * @return
     */
    public List<EcsTemplateDetailDto> selectEcsTemplateDetailByEcsTemplateId(Integer ecsTemplateId){
        if (ecsTemplateId == null || ecsTemplateId < 0){
            return new ArrayList<>();
        }
        Example example = new Example(EcsTemplateDetail.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", ecsTemplateId);
        return copyProperties(ecsTemplateDetailMapper.selectByExample(example));
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public EcsTemplateDetail selectByPrimaryKey(Integer id){
        return ecsTemplateDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加参数校验
     * @param ecsTemplateDetailDto
     */
    private void addEcsTemplateDetailParameterCheck(EcsTemplateDetailDto ecsTemplateDetailDto) {
        if (ecsTemplateDetailDto == null || ecsTemplateDetailDto.getTemplateId() == null || ecsTemplateDetailDto.getTemplateId() < 0
                || StringUtils.isBlank(ecsTemplateDetailDto.getRequestCode()) || StringUtils.isBlank(ecsTemplateDetailDto.getRequestName())
                || StringUtils.isBlank(ecsTemplateDetailDto.getRequestValue())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        EcsTemplate ecsTemplate = ecsTemplateService.selectByPrimaryKey(ecsTemplateDetailDto.getTemplateId());
        if (ecsTemplate == null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_NO_EXIST, BundleKey.ECS_TEMPLATE_NO_EXIST_MSG);
        }
        Example example = new Example(EcsTemplateDetail.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId", ecsTemplateDetailDto.getTemplateId());
        criteria.andEqualTo("requestCode", ecsTemplateDetailDto.getRequestCode());
        criteria.andEqualTo("requestValue", ecsTemplateDetailDto.getRequestValue());
        EcsTemplateDetail ecsTemplateDetail = ecsTemplateDetailMapper.selectOneByExample(example);
        if (ecsTemplateDetail != null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_ALREADY_EXIST, BundleKey.ECS_TEMPLATE_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 修改参数校验
     * @param ecsTemplateDetailDto
     * @return
     */
    private EcsTemplateDetail editEcsTemplateDetailParameterCheck(EcsTemplateDetailDto ecsTemplateDetailDto) {
        if (ecsTemplateDetailDto == null || ecsTemplateDetailDto.getTid() == null || ecsTemplateDetailDto.getTid() < 0
                || ecsTemplateDetailDto.getTemplateId() == null || ecsTemplateDetailDto.getTemplateId() < 0
                || StringUtils.isBlank(ecsTemplateDetailDto.getRequestCode()) || StringUtils.isBlank(ecsTemplateDetailDto.getRequestName())
                || StringUtils.isBlank(ecsTemplateDetailDto.getRequestValue())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        EcsTemplateDetail ecsTemplateDetail = selectByPrimaryKey(ecsTemplateDetailDto.getTid());
        if (ecsTemplateDetail == null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_NO_EXIST, BundleKey.ECS_TEMPLATE_NO_EXIST_MSG);
        }
        EcsTemplate ecsTemplate = ecsTemplateService.selectByPrimaryKey(ecsTemplateDetailDto.getTemplateId());
        if (ecsTemplate == null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_NO_EXIST, BundleKey.ECS_TEMPLATE_NO_EXIST_MSG);
        }
        Example example = new Example(EcsTemplateDetail.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", ecsTemplateDetailDto.getTid());
        criteria.andEqualTo("templateId", ecsTemplateDetailDto.getTemplateId());
        criteria.andEqualTo("requestCode", ecsTemplateDetailDto.getRequestCode());
        criteria.andEqualTo("requestValue", ecsTemplateDetailDto.getRequestValue());
        EcsTemplateDetail ecsTemplateDetail1 = ecsTemplateDetailMapper.selectOneByExample(example);
        if (ecsTemplateDetail1 != null){
            throw new ProvisioningException(BundleKey.ECS_TEMPLATE_ALREADY_EXIST, BundleKey.ECS_TEMPLATE_ALREADY_EXIST_MSG);
        }
        return ecsTemplateDetail;
    }

    /**
     * 复制
     * @param ecsTemplateDetailList
     * @return
     */
    private List<EcsTemplateDetailDto> copyProperties(List<EcsTemplateDetail> ecsTemplateDetailList){
        if (!CollectionUtils.isEmpty(ecsTemplateDetailList)){
            return ecsTemplateDetailList.stream()
                    .filter(Objects::nonNull)
                    .map(ecsTemplateDetail -> {
                        return copyProperties(ecsTemplateDetail);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param ecsTemplateDetail
     * @return
     */
    private EcsTemplateDetailDto copyProperties(EcsTemplateDetail ecsTemplateDetail){
        if (ecsTemplateDetail != null){
            EcsTemplateDetailDto ecsTemplateDetailDto = new EcsTemplateDetailDto();
            BeanUtils.copyProperties(ecsTemplateDetail, ecsTemplateDetailDto);
            ecsTemplateDetailDto.setTid(ecsTemplateDetail.getId());
            return ecsTemplateDetailDto;
        }
        return null;
    }
}
