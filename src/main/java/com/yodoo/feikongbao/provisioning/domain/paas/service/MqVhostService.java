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
import com.yodoo.feikongbao.provisioning.domain.system.entity.CompanyCreateProcess;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyCreateProcessService;
import com.yodoo.feikongbao.provisioning.domain.system.service.CompanyService;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.MqResponseEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
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
 * @Description ：mq vhost
 * @Author ：jinjun_luo
 * @Date ： 2019/7/29 0029
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.TRANSACTION_MANAGER_BEAN_NAME)
public class MqVhostService {

    @Autowired
    private MqVhostMapper mqVhostMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RabbitMqVirtualHostService rabbitMqVirtualHostService;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    /**
     * 条件查询
     * @param mqVhostDto
     * @return
     */
    public PageInfoDto<MqVhostDto> queryMqVhostList(MqVhostDto mqVhostDto) {
        // 设置查询条件
        MqVhost mqVhost = new MqVhost();
        if (mqVhostDto != null) {
            BeanUtils.copyProperties(mqVhostDto, mqVhost);
        }
        Page<?> pages = PageHelper.startPage(mqVhostDto.getPageNum(), mqVhostDto.getPageSize());
        List<MqVhost> list = mqVhostMapper.select(mqVhost);
        List<MqVhostDto> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            collect = list.stream()
                    .filter(Objects::nonNull)
                    .map(mqVhost1 -> {
                        MqVhostDto dto = new MqVhostDto();
                        BeanUtils.copyProperties(mqVhost1, dto);
                        dto.setTid(mqVhost1.getId());
                        return dto;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new PageInfoDto<MqVhostDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    public MqVhostDto getMqVhostDetails(Integer id) {
        MqVhost mqVhost = selectByPrimaryKey(id);
        MqVhostDto mqVhostDto = new MqVhostDto();
        if (mqVhost != null){
            BeanUtils.copyProperties(mqVhost, mqVhostDto);
            mqVhostDto.setTid(mqVhost.getId());
        }
        return mqVhostDto;
    }

    /**
     * 通过主键查询
     * @param id
     * @return
     */
    public MqVhost selectByPrimaryKey(Integer id){
        return mqVhostMapper.selectByPrimaryKey(id);
    }

    /**
     * 创建公司时，创建消息队列
     * @param mqVhostDto
     * @return
     */
    public MqVhostDto useMqVhost(MqVhostDto mqVhostDto) {
        // 参数校验
        useMqVhostParameterCheck(mqVhostDto);
        // 创建vhost
        MqResponseEnum mqResponseEnum = rabbitMqVirtualHostService.createVirtualHost(mqVhostDto.getVhostName());
        if (mqResponseEnum.code != MqResponseEnum.CREATE_SUCCESS.code){
            throw new ProvisioningException(BundleKey.UNDEFINED, BundleKey.UNDEFINED_MSG);
        }
        // 添加mqvhost表数据
        MqVhost mqVhost = new MqVhost();
        BeanUtils.copyProperties(mqVhostDto, mqVhost);
        mqVhostMapper.insertSelective(mqVhost);

        // 更新公司表数据
        CompanyDto companyDto = new CompanyDto();
        companyDto.setTid(mqVhostDto.getCompanyId());
        companyDto.setMqVhostId(mqVhost.getId());
        companyService.updateCompany(companyDto);

        // 添加公司创建过程记录表
        companyCreateProcessService.insertCompanyCreateProcess(new CompanyCreateProcess(mqVhostDto.getCompanyId(),
                CompanyCreationStepsEnum.RABBITMQ_STEP.getOrder(), CompanyCreationStepsEnum.RABBITMQ_STEP.getCode()));

        return mqVhostDto;
    }

    /**
     * 创建 vhost 参数校验
     * @param mqVhostDto
     */
    private void useMqVhostParameterCheck(MqVhostDto mqVhostDto) {
        if (mqVhostDto == null || mqVhostDto.getCompanyId() == null || mqVhostDto.getCompanyId() < 0
                || StringUtils.isBlank(mqVhostDto.getVhostName()) || StringUtils.isBlank(mqVhostDto.getIp())
                || mqVhostDto.getPort() != null || mqVhostDto.getPort() < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        MqVhost mqVhost = new MqVhost();
        mqVhost.setVhostName(mqVhostDto.getVhostName());
        MqVhost mqVhost1 = mqVhostMapper.selectOne(mqVhost);
        if (mqVhost1 != null){
            throw new ProvisioningException(BundleKey.EXIST, BundleKey.EXIST_MEG);
        }
        // 查询公司是否存在，不存在不操作
        Company company = companyService.selectByPrimaryKey(mqVhostDto.getCompanyId());
        if (company == null){
            throw new ProvisioningException(BundleKey.ON_EXIST, BundleKey.ON_EXIST_MEG);
        }
    }
}
