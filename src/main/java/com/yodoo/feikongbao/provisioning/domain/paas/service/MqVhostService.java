package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.MqVhostDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.MqVhost;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.MqVhostMapper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.CompanyDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.Company;
import com.yodoo.feikongbao.provisioning.domain.system.service.ApolloService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.MqResponseEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.megalodon.datasource.config.RabbitMqConfig;
import io.swagger.annotations.ApiImplicitParams;
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
 * @Description ：mq vhost
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class MqVhostService {

    @Autowired
    private MqVhostMapper mqVhostMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RabbitMqVirtualHostService rabbitMqVirtualHostService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    @Autowired
    private ApolloService apolloService;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    /**
     * 条件查询
     *
     * @param mqVhostDto
     * @return
     */
    public PageInfoDto<MqVhostDto> queryMqVHostList(MqVhostDto mqVhostDto) {
        Example example = new Example(MqVhost.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(mqVhostDto.getVhostName())){
            criteria.andEqualTo("vhostName", mqVhostDto.getVhostName());
        }
        Page<?> pages = PageHelper.startPage(mqVhostDto.getPageNum(), mqVhostDto.getPageSize());
        List<MqVhost> mqVhosts = mqVhostMapper.selectByExample(example);
        List<MqVhostDto> collect = copyProperties(mqVhosts);
        return new PageInfoDto<MqVhostDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public MqVhostDto getMqVHostDetails(Integer id) {
        return copyProperties(selectByPrimaryKey(id));
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public MqVhost selectByPrimaryKey(Integer id) {
        return mqVhostMapper.selectByPrimaryKey(id);
    }

    /**
     * 创建公司时，创建消息队列
     *
     * @param companyId
     * @return
     */
    public void useMqVHost(Integer companyId) {
        // 参数校验
        Company company = useMqVHostParameterCheck(companyId);

        // 创建vhost
        MqResponseEnum mqResponseEnum = rabbitMqVirtualHostService.createVirtualHost(company.getCompanyCode());
        if (mqResponseEnum.code == MqResponseEnum.EXIST.code) {
            throw new ProvisioningException(BundleKey.RABBITMQ_VHOST_NAME_EXIST_ERROR, BundleKey.RABBITMQ_VHOST_NAME_EXIST_ERROR_MSG);
        }

        // 添加mqvhost表数据
        MqVhost mqVhost = mqVhostMapper.selectOne(new MqVhost(company.getCompanyCode()));
        if (mqVhost != null){
            mqVhost.setIp(rabbitMqConfig.rabbitmqUrlHost);
            mqVhost.setPort(rabbitMqConfig.rabbitmqUrlServicePort);
            mqVhostMapper.updateByPrimaryKeySelective(mqVhost);
        }else {
            mqVhost = new MqVhost();
            mqVhost.setVhostName(company.getCompanyCode());
            mqVhost.setIp(rabbitMqConfig.rabbitmqUrlHost);
            mqVhost.setPort(rabbitMqConfig.rabbitmqUrlServicePort);
            mqVhostMapper.insertSelective(mqVhost);
        }

        // 更新公司表数据
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(companyId);
        companyDto.setMqVhostId(mqVhost.getId());
        companyService.updateCompany(companyDto);

        // 添加公司创建过程记录表
        companyCreateProcessService.insertCompanyCreateProcess(companyId,
                CompanyCreationStepsEnum.RABBITMQ_STEP.getOrder(), CompanyCreationStepsEnum.RABBITMQ_STEP.getCode());

        // apollo 配置
        apolloService.createVirtualHostItem(company.getCompanyCode());
    }

    /**
     * 删除
     * @param companyCode
     * @return
     */
    public Integer deleteMqVHostByCompanyCode(String companyCode){
        if (StringUtils.isBlank(companyCode)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(MqVhost.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("vhostName", companyCode);
       return mqVhostMapper.deleteByExample(example);
    }

    /**
     * 创建 vhost 参数校验
     *
     * @param companyId
     */
    private Company useMqVHostParameterCheck(Integer companyId) {
        if (companyId == null || companyId < 0) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(companyId);
        if (company == null) {
            throw new ProvisioningException(BundleKey.COMPANY_NOT_EXIST, BundleKey.COMPANY_NOT_EXIST_MSG);
        }
        return company;
    }

    /**
     * 复制
     * @param mqVhostList
     * @return
     */
    private List<MqVhostDto> copyProperties(List<MqVhost> mqVhostList){
        if (!CollectionUtils.isEmpty(mqVhostList)){
            return mqVhostList.stream()
                    .filter(Objects::nonNull)
                    .map(mqVhost -> {
                        return copyProperties(mqVhost);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param mqVhost
     * @return
     */
    private MqVhostDto copyProperties(MqVhost mqVhost){
        if (mqVhost != null){
            MqVhostDto mqVhostDto = new MqVhostDto();
            BeanUtils.copyProperties(mqVhost, mqVhostDto);
            mqVhostDto.setTid(mqVhost.getId());
            return mqVhostDto;
        }
        return null;
    }
}
