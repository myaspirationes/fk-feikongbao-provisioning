package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.RedisGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.RedisInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.RedisInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.service.ApolloService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.InstanceStatusEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description ：redis 实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class RedisInstanceService {

    @Autowired
    private RedisInstanceMapper redisInstanceMapper;

    @Autowired
    private RedisGroupService redisGroupService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    @Autowired
    private ApolloService apolloService;

    /**
     * 条件分页查询
     *
     * @param redisInstanceDto
     * @return
     */
    public PageInfoDto<RedisInstanceDto> queryRedisInstanceList(RedisInstanceDto redisInstanceDto) {
        Example example = new Example(RedisInstance.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(redisInstanceDto.getPageNum(), redisInstanceDto.getPageSize());
        List<RedisInstance> redisInstanceList = redisInstanceMapper.selectByExample(example);
        List<RedisInstanceDto> collect = copyProperties(redisInstanceList);
        return new PageInfoDto<RedisInstanceDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public RedisInstanceDto getRedisInstanceDetails(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public RedisInstance selectByPrimaryKey(Integer id) {
        return redisInstanceMapper.selectByPrimaryKey(id);
    }

    /**
     * 使用缓存
     *
     * @param redisInstanceDto
     * @return
     */
    public RedisInstanceDto useRedisInstance(RedisInstanceDto redisInstanceDto) {
        // 校验
        List<RedisInstance> redisInstanceList = useRedisInstanceParameterCheck(redisInstanceDto);

        // 公司表
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(redisInstanceDto.getCompanyId());
        companyDto.setRedisGroupId(redisInstanceDto.getRedisGroupId());
        companyService.updateCompany(companyDto);

        // 添加apollo配置
        apolloService.createRedisItems(redisInstanceDto.getCompanyCode());

        // 添加创建公司流程记录表
        companyCreateProcessService.insertCompanyCreateProcess(redisInstanceDto.getCompanyId(),
                CompanyCreationStepsEnum.REDIS_STEP.getOrder(), CompanyCreationStepsEnum.REDIS_STEP.getCode());

        // 更新 RedisInstance使用状态
        redisInstanceList.stream().filter(Objects::nonNull).forEach(redisInstance -> {
            redisInstance.setStatus(InstanceStatusEnum.USED.getCode());
            redisInstanceMapper.updateByPrimaryKeySelective(redisInstance);
        });

        return redisInstanceDto;
    }

    /**
     * 通过 redisGroupId 查询
     *
     * @param redisGroupId
     * @return
     */
    public List<RedisInstanceDto> selectRedisInstanceListByRedisGroupId(Integer redisGroupId) {
        Example example = new Example(RedisInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("redisGroupId", redisGroupId);
        return copyProperties(redisInstanceMapper.selectByExample(example));
    }

    /**
     * 根据 类型 查询 redis 实例 列表
     * @param type
     * @return
     */
    public List<RedisInstanceDto> getRedisInstanceByType(Integer type) {
        Example example = new Example(RedisInstance.class);
        example.setOrderByClause("ORDER BY create_time ASC");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", type == null ? 0 : type);
        return copyProperties(redisInstanceMapper.selectByExample(example));
    }

    /**
     * 添加
     * @param redisInstanceDto
     * @return
     */
    public Integer addRedisInstance(RedisInstanceDto redisInstanceDto) {
        addRedisInstanceParameterCheck(redisInstanceDto);
        RedisInstance redisInstance = new RedisInstance();
        BeanUtils.copyProperties(redisInstanceDto, redisInstance);
        redisInstance.setStatus(InstanceStatusEnum.UNUSED.getCode());
        return redisInstanceMapper.insertSelective(redisInstance);
    }

    /**
     *  更新
     * @param redisInstanceDto
     * @return
     */
    public Integer editRedisInstance(RedisInstanceDto redisInstanceDto) {
        RedisInstance redisInstance = editRedisInstanceParameterCheck(redisInstanceDto);
        BeanUtils.copyProperties(redisInstanceDto, redisInstance);
        return redisInstanceMapper.updateByPrimaryKeySelective(redisInstance);
    }

    /**
     *  删除
     * @param id
     * @return
     */
    public Integer deleteRedisInstance(Integer id) {
        deleteRedisInstanceParameterCheck(id);
        return redisInstanceMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除
     * @param id
     */
    private void deleteRedisInstanceParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        RedisInstance redisInstance = selectByPrimaryKey(id);
        if (redisInstance == null){
            throw new ProvisioningException(BundleKey.REDIS_INSTANCE_NOT_EXIST, BundleKey.REDIS_INSTANCE_NOT_EXIST_MSG);
        }
        RedisGroup redisGroup = redisGroupService.selectByPrimaryKey(redisInstance.getRedisGroupId());
        if (redisGroup != null){
          List<Company> companyList = companyService.getCompanyByRedisGroupId(redisGroup.getId());
          if (!CollectionUtils.isEmpty(companyList)){
              throw new ProvisioningException(BundleKey.REDIS_INSTANCE_USED, BundleKey.REDIS_INSTANCE_USED_MSG);
          }
        }
    }

    /**
     * 更新参数校验
     * @param redisInstanceDto
     * @return
     */
    private RedisInstance editRedisInstanceParameterCheck(RedisInstanceDto redisInstanceDto) {
        if (redisInstanceDto == null || redisInstanceDto.getTid() == null || redisInstanceDto.getTid() < 0 || redisInstanceDto.getRedisGroupId() == null
                || redisInstanceDto.getRedisGroupId() < 0 || StringUtils.isBlank(redisInstanceDto.getIp()) || redisInstanceDto.getPort() == null
                || redisInstanceDto.getPort() < 0 || StringUtils.isBlank(redisInstanceDto.getUsername()) || StringUtils.isBlank(redisInstanceDto.getPassword())
                || !Arrays.asList(0,1).contains(redisInstanceDto.getType())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        RedisInstance redisInstance = selectByPrimaryKey(redisInstanceDto.getTid());
        if (redisInstance == null){
            throw new ProvisioningException(BundleKey.REDIS_INSTANCE_NOT_EXIST, BundleKey.REDIS_INSTANCE_NOT_EXIST_MSG);
        }
        RedisGroup redisGroup = redisGroupService.selectByPrimaryKey(redisInstanceDto.getRedisGroupId());
        if (redisGroup == null){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_NOT_EXIST, BundleKey.REDIS_GROUP_NOT_EXIST_MSG);
        }
        Example example = new Example(RedisInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", redisInstanceDto.getTid());
        criteria.andEqualTo("redisGroupId", redisInstanceDto.getRedisGroupId());
        criteria.andEqualTo("ip", redisInstanceDto.getIp());
        criteria.andEqualTo("port", redisInstanceDto.getPort());
        criteria.andEqualTo("type", redisInstanceDto.getType());
        RedisInstance redisInstanceNot = redisInstanceMapper.selectOneByExample(example);
        if (redisInstanceNot != null){
            throw new ProvisioningException(BundleKey.REDIS_INSTANCE_ALREADY_EXIST, BundleKey.REDIS_INSTANCE_ALREADY_EXIST_MSG);
        }
        return redisInstance;
    }


    /**
     * 添加参数校验
     * @param redisInstanceDto
     */
    private void addRedisInstanceParameterCheck(RedisInstanceDto redisInstanceDto) {
        if (redisInstanceDto == null || redisInstanceDto.getRedisGroupId() == null || redisInstanceDto.getRedisGroupId() < 0
                || StringUtils.isBlank(redisInstanceDto.getIp()) || redisInstanceDto.getPort() == null || redisInstanceDto.getPort() < 0
                || StringUtils.isBlank(redisInstanceDto.getUsername()) || StringUtils.isBlank(redisInstanceDto.getPassword())
                || !Arrays.asList(0,1).contains(redisInstanceDto.getType())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        RedisGroup redisGroup = redisGroupService.selectByPrimaryKey(redisInstanceDto.getRedisGroupId());
        if (redisGroup == null){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_NOT_EXIST, BundleKey.REDIS_GROUP_NOT_EXIST_MSG);
        }
        Example example = new Example(RedisInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("redisGroupId", redisInstanceDto.getRedisGroupId());
        criteria.andEqualTo("ip", redisInstanceDto.getIp());
        criteria.andEqualTo("port", redisInstanceDto.getPort());
        criteria.andEqualTo("type", redisInstanceDto.getType());
        RedisInstance redisInstance = redisInstanceMapper.selectOneByExample(example);
        if (redisInstance != null){
            throw new ProvisioningException(BundleKey.REDIS_INSTANCE_ALREADY_EXIST, BundleKey.REDIS_INSTANCE_ALREADY_EXIST_MSG);
        }
    }


    /**
     * 使用缓存校验
     *
     * @param redisInstanceDto
     */
    private List<RedisInstance> useRedisInstanceParameterCheck(RedisInstanceDto redisInstanceDto) {
        if (redisInstanceDto == null || redisInstanceDto.getCompanyId() == null || redisInstanceDto.getCompanyId() < 0
                || redisInstanceDto.getRedisGroupId() == null || redisInstanceDto.getRedisGroupId() < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询redis 组是否存在，不存在不操作
        RedisGroup redisGroup = redisGroupService.selectByPrimaryKey(redisInstanceDto.getRedisGroupId());
        if (redisGroup == null){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_NOT_EXIST, BundleKey.REDIS_GROUP_NOT_EXIST_MSG);
        }
        // 查询 redis实例 是否存在，不存在不操作。或是否被使用
        List<RedisInstance> redisInstanceList = getRedisInstanceListByRedisGroupId(redisGroup.getId());
        if (CollectionUtils.isEmpty(redisInstanceList)){
            throw new ProvisioningException(BundleKey.REDIS_INSTANCE_NOT_EXIST, BundleKey.REDIS_INSTANCE_NOT_EXIST_MSG);
        }
        Long count = redisInstanceList.stream().filter(Objects::nonNull).filter(redisInstance -> redisInstance.getType().equals(InstanceStatusEnum.USED.getCode())).count();
        if (count != null && count > 0){
            throw new ProvisioningException(BundleKey.REDIS_INSTANCE_USED, BundleKey.REDIS_INSTANCE_USED_MSG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(redisInstanceDto.getCompanyId());
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        redisInstanceDto.setCompanyCode(company.getCompanyCode());
        return redisInstanceList;
    }

    /**
     * 通过redis 组 id 查询
     * @param redisGroupId
     * @return
     */
    private List<RedisInstance> getRedisInstanceListByRedisGroupId(Integer redisGroupId) {
        Example example = new Example(RedisInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("redisGroupId", redisGroupId);
        return redisInstanceMapper.selectByExample(example);
    }

    /**
     * 复制
     * @param redisInstanceList
     * @return
     */
    private List<RedisInstanceDto> copyProperties(List<RedisInstance> redisInstanceList){
        if (!CollectionUtils.isEmpty(redisInstanceList)){
            return redisInstanceList.stream()
                    .filter(Objects::nonNull)
                    .map(redisInstance -> {
                        return copyProperties(redisInstance);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param redisInstance
     * @return
     */
    private RedisInstanceDto copyProperties(RedisInstance redisInstance){
        if (redisInstance != null){
            RedisInstanceDto redisInstanceDto = new RedisInstanceDto();
            BeanUtils.copyProperties(redisInstance, redisInstanceDto);
            redisInstanceDto.setTid(redisInstance.getId());
            return redisInstanceDto;
        }
        return null;
    }
}
