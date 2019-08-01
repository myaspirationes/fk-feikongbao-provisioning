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
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyCreateProcess;
import com.yodoo.feikongbao.provisioning.domain.system.service.ApolloService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
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
 * @Description ：redis 实例
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
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
     * @param redisInstanceDto
     * @return
     */
    public PageInfoDto<RedisInstanceDto> queryRedisInstanceList(RedisInstanceDto redisInstanceDto) {
        RedisInstance redisInstance = new RedisInstance();
        if (redisInstanceDto != null) {
            BeanUtils.copyProperties(redisInstanceDto, redisInstance);
        }
        Page<?> pages = PageHelper.startPage(redisInstanceDto.getPageNum(), redisInstanceDto.getPageSize());
        List<RedisInstanceDto> collect = selectRedisInstance(redisInstance);
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
     * 使用缓存
     * @param redisInstanceDto
     * @return
     */
    public RedisInstanceDto useRedisInstance(RedisInstanceDto redisInstanceDto) {
        RedisInstance redisInstance = useRedisInstanceParameterCheck(redisInstanceDto);
        // 公司表
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(redisInstanceDto.getTid());
        companyDto.setRedisGroupId(redisInstance.getRedisGroupId());
        companyService.updateCompany(companyDto);

        // 添加apollo配置
        apolloService.createRedisItems(redisInstanceDto.getCompanyCode());

        // 添加创建公司流程记录表
        companyCreateProcessService.insertCompanyCreateProcess(new CompanyCreateProcess(redisInstanceDto.getCompanyId(),
                CompanyCreationStepsEnum.REDIS_STEP.getOrder(), CompanyCreationStepsEnum.REDIS_STEP.getCode()));
        return redisInstanceDto;
    }

    /**
     * 通过 redisGroupId 查询
     * @param redisGroupId
     * @return
     */
    public List<RedisInstanceDto> selectRedisInstanceListByRedisGroupId(Integer redisGroupId) {
        RedisInstance redisInstance = new RedisInstance();
        redisInstance.setRedisGroupId(redisGroupId);
        return selectRedisInstance(redisInstance);
    }

    /**
     * 使用缓存校验
     * @param redisInstanceDto
     */
    private RedisInstance useRedisInstanceParameterCheck(RedisInstanceDto redisInstanceDto) {
        if (redisInstanceDto == null || redisInstanceDto.getCompanyId() == null || redisInstanceDto.getCompanyId() < 0
                || redisInstanceDto.getTid() == null || redisInstanceDto.getTid() < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询 redis实例 是否存在，不存在不操作
        RedisInstance redisInstance = selectByPrimaryKey(redisInstanceDto.getTid());
        if (redisInstance == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // 查询redis 组是否存在，不存在不操作
        RedisGroup redisGroup = redisGroupService.selectByPrimaryKey(redisInstance.getRedisGroupId());
        if (redisGroup == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(redisInstanceDto.getCompanyId());
        if (company == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
        redisInstanceDto.setCompanyCode(company.getCompanyCode());
        return redisInstance;
    }

    /**
     * 条件查询
     * @param redisInstance
     * @return
     */
    private List<RedisInstanceDto> selectRedisInstance(RedisInstance redisInstance) {
        List<RedisInstance> select = redisInstanceMapper.select(redisInstance);
        List<RedisInstanceDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(select)){
            collect = select.stream()
                    .filter(Objects::nonNull)
                    .map(redisInstance1 -> {
                        RedisInstanceDto redisInstanceDto = new RedisInstanceDto();
                        BeanUtils.copyProperties(redisInstance1, redisInstanceDto);
                        redisInstanceDto.setTid(redisInstance1.getId());
                        return redisInstanceDto;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return collect;
    }
}
