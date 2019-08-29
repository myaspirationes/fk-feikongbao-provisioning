package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisGroupDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.RedisInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.RedisGroup;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.RedisInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.RedisGroupMapper;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
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
 * @Description ：redis 组
 * @Author ：jinjun_luo
 * @Date ： 2019/7/30 0030
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class RedisGroupService {

    @Autowired
    private RedisGroupMapper redisGroupMapper;

    @Autowired
    private RedisInstanceService redisInstanceService;

    @Autowired
    private CompanyService companyService;

    /**
     * 条件分页查询
     *
     * @param redisGroupDto
     * @return
     */
    public PageInfoDto<RedisGroupDto> queryRedisGroupList(RedisGroupDto redisGroupDto) {
        Example example = new Example(RedisGroup.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(redisGroupDto.getGroupCode())){
            criteria.andEqualTo("groupCode", redisGroupDto.getGroupCode());
        }
        if (StringUtils.isNotBlank(redisGroupDto.getGroupName())){
            criteria.andEqualTo("groupName", redisGroupDto.getGroupName());
        }
        Page<?> pages = PageHelper.startPage(redisGroupDto.getPageNum(), redisGroupDto.getPageSize());
        List<RedisGroup> list = redisGroupMapper.selectByExample(example);
        List<RedisGroupDto> collect = copyProperties(list);
        return new PageInfoDto<RedisGroupDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public RedisGroupDto getRedisGroupDetails(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public RedisGroup selectByPrimaryKey(Integer id) {
        return redisGroupMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过缓存组 id 查询
     *
     * @param id
     * @return
     */
    public RedisGroupDto selectRedisGroupById(Integer id) {
        RedisGroup redisGroup = selectByPrimaryKey(id);
        RedisGroupDto redisGroupDto = new RedisGroupDto();
        if (redisGroup != null) {
            redisGroupDto = copyProperties(redisGroup);
            List<RedisInstanceDto> redisInstances = redisInstanceService.selectRedisInstanceListByRedisGroupId(redisGroup.getId());
            if (!CollectionUtils.isEmpty(redisInstances)) {
                redisGroupDto.setRedisInstanceDtoList(redisInstances);
            }
        }
        return redisGroupDto;
    }

    /**
     * 添加参数据校验
     * @param redisGroupDto
     */
    public Integer addRedisGroup(RedisGroupDto redisGroupDto) {
        addRedisGroupParameterCheck(redisGroupDto);
        return redisGroupMapper.insertSelective(new RedisGroup(redisGroupDto.getGroupCode(), redisGroupDto.getGroupName()));
    }

    /**
     * 更新
     * @param redisGroupDto
     */
    public Integer editRedisGroup(RedisGroupDto redisGroupDto) {
        RedisGroup redisGroup = editRedisGroupParameterCheck(redisGroupDto);
        redisGroup.setGroupCode(redisGroupDto.getGroupCode());
        redisGroup.setGroupName(redisGroupDto.getGroupName());
        return redisGroupMapper.updateByPrimaryKeySelective(redisGroup);
    }

    /**
     * 删除
     * @param id
     */
    public Integer deleteRedisGroup(Integer id) {
        deleteRedisGroupParameterCheck(id);
        return redisGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除校验
     * @param id
     */
    private void deleteRedisGroupParameterCheck(Integer id) {
        if (id == null || id < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        RedisGroup redisGroup = selectByPrimaryKey(id);
        if (redisGroup == null){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_NOT_EXIST, BundleKey.REDIS_GROUP_NOT_EXIST_MSG);
        }
        List<RedisInstanceDto> redisInstancesList = redisInstanceService.selectRedisInstanceListByRedisGroupId(id);
        if (!CollectionUtils.isEmpty(redisInstancesList)){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_IS_USE, BundleKey.REDIS_GROUP_IS_USE_MSG);
        }
        List<Company> companyList = companyService.getCompanyByRedisGroupId(id);
        if (!CollectionUtils.isEmpty(companyList)){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_IS_USE, BundleKey.REDIS_GROUP_IS_USE_MSG);
        }
    }

    /**
     * 更新参数校验
     * @param redisGroupDto
     * @return
     */
    private RedisGroup editRedisGroupParameterCheck(RedisGroupDto redisGroupDto) {
        if (redisGroupDto == null || redisGroupDto.getTid() == null || redisGroupDto.getTid() < 0
                || StringUtils.isBlank(redisGroupDto.getGroupCode()) || StringUtils.isBlank(redisGroupDto.getGroupName())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        RedisGroup redisGroup = selectByPrimaryKey(redisGroupDto.getTid());
        if (redisGroup == null){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_NOT_EXIST, BundleKey.REDIS_GROUP_NOT_EXIST_MSG);
        }
        Example example = new Example(RedisGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("id", redisGroupDto.getTid());
        criteria.andEqualTo("groupCode", redisGroupDto.getGroupCode());
        RedisGroup redisGroupNot = redisGroupMapper.selectOneByExample(example);
        if (redisGroupNot != null){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_ALREADY_EXIST, BundleKey.REDIS_GROUP_ALREADY_EXIST_MSG);
        }
        return redisGroup;
    }

    /**
     * 添加参数校验
     * @param redisGroupDto
     */
    private void addRedisGroupParameterCheck(RedisGroupDto redisGroupDto) {
        if (redisGroupDto == null || StringUtils.isBlank(redisGroupDto.getGroupCode()) || StringUtils.isBlank(redisGroupDto.getGroupName())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(RedisGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupCode", redisGroupDto.getGroupCode());
        RedisGroup redisGroup = redisGroupMapper.selectOneByExample(example);
        if (redisGroup != null){
            throw new ProvisioningException(BundleKey.REDIS_GROUP_ALREADY_EXIST, BundleKey.REDIS_GROUP_ALREADY_EXIST_MSG);
        }
    }

    /**
     * 复制
     * @param redisGroupList
     * @return
     */
    private List<RedisGroupDto> copyProperties(List<RedisGroup> redisGroupList){
        if (!CollectionUtils.isEmpty(redisGroupList)){
            return redisGroupList.stream()
                    .filter(Objects::nonNull)
                    .map(redisGroup -> {
                        return copyProperties(redisGroup);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param redisGroup
     * @return
     */
    private RedisGroupDto copyProperties(RedisGroup redisGroup){
        if (redisGroup != null){
            RedisGroupDto redisGroupDto = new RedisGroupDto();
            BeanUtils.copyProperties(redisGroup, redisGroupDto);
            redisGroupDto.setTid(redisGroup.getId());
            return redisGroupDto;
        }
        return null;
    }
}
