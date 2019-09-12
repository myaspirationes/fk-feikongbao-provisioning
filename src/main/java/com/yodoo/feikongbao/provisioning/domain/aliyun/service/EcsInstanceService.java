package com.yodoo.feikongbao.provisioning.domain.aliyun.service;

import com.aliyuncs.ecs.model.v20140526.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yodoo.feikongbao.provisioning.common.dto.PageInfoDto;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.aliyun.contract.ECSConstants;
import com.yodoo.feikongbao.provisioning.domain.aliyun.dto.SnapshotRequest;
import com.yodoo.feikongbao.provisioning.domain.aliyun.enums.ECSAutoRenew;
import com.yodoo.feikongbao.provisioning.domain.aliyun.enums.ECSStatus;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDetailDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsTemplateDto;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.EcsInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.EcsInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.paas.service.EcsTemplateService;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.BeanCopyUtils;
import com.yodoo.feikongbao.provisioning.util.JsonUtils;
import com.yodoo.megalodon.datasource.config.AliYunConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2019/6/11 19:05
 * @Created by houzhen
 */
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class EcsInstanceService {

    private static Logger logger = LoggerFactory.getLogger(EcsInstanceService.class);

    @Autowired
    private EcsInstanceMapper ecsInstanceMapper;

    @Autowired
    private EcsTemplateService ecsTemplateService;

    @Autowired
    private EcsSnapshotService ecsSnapshotService;

    @Autowired
    private ECSCallService ecsCallService;

    @Autowired
    private AliYunConfig aliYunConfig;

    /**
     * 创建虚拟机实例（目前只支持每次创建一个）
     * @Author houzhen
     * @Date 15:29 2019/6/4
     **/
    public void createRunInstance(String ecsType) throws ProvisioningException {
        // 验证参数
        if (StringUtils.isBlank(ecsType)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        EcsInstanceDto ecsInstanceDto = new EcsInstanceDto();
        ecsInstanceDto.setEcsType(ecsType);
        // ecs模板参数信息
        Map<String, Object> templateMap = getTemplateMap(ecsInstanceDto);
        // ecs创建request实体
        RunInstancesRequest runInstancesRequest = composeRunInstancesRequest(templateMap);
        // 请求aliyun，创建ecs实例
        RunInstancesResponse response = ecsCallService.callOpenApi(runInstancesRequest);
        // ecs实例Id
        String instanceId = response.getInstanceIdSets().get(0);
        logger.info("Success. Instance creation succeed. InstanceId:{}", instanceId);
        // 将实例记录到数据库，并记录当前状态
        insertRunInstances(runInstancesRequest, instanceId, templateMap);
        // 检查虚拟机创建状态
        checkRunningStatusToInstance(instanceId);
        // 设置自动快策略
        addAutoSnapshotPolicy(instanceId, templateMap);
    }

    /**
     * 释放虚拟机（只支持虚拟机状态为Running和Stopped的实例释放，目前只支持释放一个虚拟机，只支持强制释放，
     * 强制释放相当于断电，实例内存以及存储中的临时数据都会被擦除，无法恢复）
     * @Author houzhen
     * @Date 15:30 2019/6/5
     **/
    public void deleteRunInstance(String ecsInstanceId) throws ProvisioningException {
        if (StringUtils.isBlank(ecsInstanceId)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 1.查询虚拟机实例是否存在，并判断状态
        DescribeInstancesResponse describeInstancesResponse = getDescribeInstance(ecsInstanceId);
        if (describeInstancesResponse == null || CollectionUtils.isEmpty(describeInstancesResponse.getInstances())) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        for(DescribeInstancesResponse.Instance instance : describeInstancesResponse.getInstances()) {
            // 强制释放虚拟机实例
            DeleteInstanceResponse response = deleteRunInstance(instance.getInstanceId(), true);
            if (response == null) {
                throw new ProvisioningException(BundleKey.VIRTUAL_DELETE_ERROR, BundleKey.VIRTUAL_DELETE_ERROR_MSG);
            }
            // 更新数据库实例-释放
            updateRunInstanceStatus(instance.getInstanceId(), ECSStatus.DELETED.getCode());
        }
    }

    /**
     * 查询ecs实例（数据库）
     * @Author houzhen
     * @Date 10:39 2019/6/14
    **/
    public List<EcsInstanceDto> getEcsInstancesByInstanceIdAneEcsType(String instanceId, String ecsType) {
        Example example = getExample(instanceId, ecsType);
        List<EcsInstance> ecsInstanceList = ecsInstanceMapper.selectByExample(example);
        return copyProperties(ecsInstanceList);
    }

    /**
     * 查询所有ecs 实例
     * @return
     */
    public List<EcsInstanceDto> getAllEcsInstance(){
        List<EcsInstance> ecsInstanceList = ecsInstanceMapper.selectAll();
        return copyProperties(ecsInstanceList);
    }

    /**
     * 根据ecs实例id查询数据库中记录
     * @Author houzhen
     * @Date 16:17 2019/6/13
     **/
    public EcsInstanceDto getByInstanceId(String instanceId) {
        Example example = getExample(instanceId, null);
        EcsInstance ecsInstance = ecsInstanceMapper.selectOneByExample(example);
        return copyProperties(ecsInstance);
    }

    /**
     * 休眠
     * @Author houzhen
     * @Date 15:32 2019/6/4
     **/
    public void sleepSomeTime(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新虚拟机状态
     * @Author houzhen
     * @Date 14:53 2019/6/5
     **/
    public void updateRunInstanceStatus(List<String> instanceIds, Integer status) {
        if (!CollectionUtils.isEmpty(instanceIds) || status == null || status < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        instanceIds.stream()
                .filter(Objects::nonNull)
                .forEach(instanceId -> {
                    updateRunInstanceStatus(instanceId,status);
                });
    }

    /**
     * 根据 ecs 实例更新
     * @param instanceId
     * @param status
     */
    public void updateRunInstanceStatus(String instanceId, Integer status){
        if (StringUtils.isBlank(instanceId) || status == null || status < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = new Example(EcsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        EcsInstance ecsInstance = ecsInstanceMapper.selectOneByExample(example);
        if (ecsInstance != null){
            ecsInstance.setStatus(status);
            ecsInstanceMapper.updateByPrimaryKeySelective(ecsInstance);
        }
    }

    /**
     * 更新磁盘大小
     * @param instanceId
     * @param newSize
     */
    public void updateRunInstanceSystemDiskSize(String instanceId, Integer newSize, Integer status){
        if (StringUtils.isBlank(instanceId) || newSize == null || newSize < 0){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        Example example = getExample(instanceId, null);
        EcsInstance ecsInstance = ecsInstanceMapper.selectOneByExample(example);
        if (ecsInstance != null){
            ecsInstance.setSystemDiskSize(String.valueOf(newSize));
            ecsInstance.setStatus(status);
            ecsInstanceMapper.updateByPrimaryKeySelective(ecsInstance);
        }
    }

    /**
     * 停止一台虚拟机
     * @param instanceId : 实例id
     * @param forceStop : 停止实例时的是否强制关机策略,true：强制关机 , false（默认）：正常关机流程
     * @return
     */
    public StopInstanceResponse stopRunInstance(String instanceId, Boolean forceStop){
        StopInstanceRequest stopInstanceRequest = new StopInstanceRequest();
        stopInstanceRequest.setInstanceId(instanceId);
        stopInstanceRequest.setForceStop(forceStop);
        return ecsCallService.callOpenApi(stopInstanceRequest);
    }

    /**
     * 启动一台虚拟机
     * @param instanceId
     */
    public StartInstanceResponse startRunInstance(String instanceId){
        StartInstanceRequest startInstanceRequest = new StartInstanceRequest();
        startInstanceRequest.setInstanceId(instanceId);
        return ecsCallService.callOpenApi(startInstanceRequest);
    }

    /**
     * 根据ecs实例获取内网ip
     * @Author houzhen
     * @Date 9:53 2019/7/5
     **/
    public String getPrivateIpAddress(String instanceId) {
        // 验证参数
        if (StringUtils.isEmpty(instanceId)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 2.查询阿里ecs状态
        DescribeInstancesResponse describeInstance = this.getDescribeInstance(instanceId);
        // 虚拟机不存在返回
        if (describeInstance == null || CollectionUtils.isEmpty(describeInstance.getInstances())){
            throw new ProvisioningException(BundleKey.NO_INSTANCE_ERROR, BundleKey.NO_INSTANCE_ERROR_MSG);
        }
        DescribeInstancesResponse.Instance instance = describeInstance.getInstances().get(0);
        // 判断状态是否正确
        if (instance.getStatus().equals(ECSStatus.RUNNING.getStatus()) || instance.getStatus().equals(ECSStatus.STOPPED.getStatus())){
            // 获取内网ip
            if (instance.getVpcAttributes() == null || CollectionUtils.isEmpty(instance.getVpcAttributes().getPrivateIpAddress())) {
                logger.error("privateIpAddress not exist");
                throw new ProvisioningException(BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR, BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR_MSG  + "(privateIpAddress not exist)");
            }
            return instance.getVpcAttributes().getPrivateIpAddress().get(0);
        }
        return null;
    }

    /**
     * 条件分页查询
     * @param ecsInstanceDto
     * @return
     */
    public PageInfoDto<EcsInstanceDto> queryEcsInstanceList(EcsInstanceDto ecsInstanceDto) {
        Example example = new Example(EcsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        Page<?> pages = PageHelper.startPage(ecsInstanceDto.getPageNum(), ecsInstanceDto.getPageSize());
        List<EcsInstance> ecsInstanceList = ecsInstanceMapper.selectByExample(example);
        List<EcsInstanceDto> collect = copyProperties(ecsInstanceList);
        return new PageInfoDto<EcsInstanceDto>(pages.getPageNum(), pages.getPageSize(), pages.getTotal(), pages.getPages(), collect);
    }

    /**
     * 获取 example
     * @param instanceId
     * @param ecsType
     * @return
     */
    private Example getExample(String instanceId, String ecsType){
        Example example = new Example(EcsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(instanceId)){
            criteria.andEqualTo("instanceId", instanceId);
        }
        if (StringUtils.isNotBlank(ecsType)){
            criteria.andEqualTo("ecsType", ecsType);
        }
        return example;
    }

    /**
     * 模板信息
     * @Author houzhen
     * @Date 14:40 2019/6/13
    **/
    private Map<String, Object> getTemplateMap(EcsInstanceDto ecsInstanceDto) {
        // 1.根据ecsType查询虚拟机模板信息
        EcsTemplateDto ecsTemplateByEcsType = ecsTemplateService.getEcsTemplateByEcsType(ecsInstanceDto.getEcsType());
        if (ecsTemplateByEcsType == null || CollectionUtils.isEmpty(ecsTemplateByEcsType.getEcsTemplateDetailDtoList())) {
            logger.error("escType error：{}", ecsInstanceDto.getEcsType());
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 2.模板数据组成key，方便后续获取
        Map<String, Object> templateMap = new HashMap<>();
        for (EcsTemplateDetailDto detailResponse : ecsTemplateByEcsType.getEcsTemplateDetailDtoList()) {
            templateMap.put(detailResponse.getRequestCode(), detailResponse.getRequestValue());
        }
        // 3.将ecs实例类型加入到map中
        templateMap.put(ECSConstants.instanceType, ecsInstanceDto.getEcsType());
        return templateMap;
    }

    /**
     * 组建创建实例请求参数
     * @Author houzhen
     * @Date 13:34 2019/6/4
     **/
    private RunInstancesRequest composeRunInstancesRequest(Map<String, Object> templateMap) {
        // 组建请求参数
        RunInstancesRequest runInstancesRequest = BeanCopyUtils.convertMapToBean(templateMap, RunInstancesRequest.class);
        // 是否只预检此次请求
        runInstancesRequest.setDryRun(ECSConstants.dryRun);
        // 随机选取交换机ID
        runInstancesRequest.setVSwitchId(randomVSwitchId());
        return runInstancesRequest;
    }

    /**
     * 查询单个虚拟机状态
     * @Author houzhen
     * @Date 15:52 2019/6/5
     **/
    private DescribeInstancesResponse getDescribeInstance(String instanceId) {
        return getDescribeInstance(Arrays.asList(instanceId));
    }

    /**
     * 查询虚拟机状态
     * @Author houzhen
     * @Date 15:52 2019/6/5
     **/
    private DescribeInstancesResponse getDescribeInstance(List<String> instanceIds) {
        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
        describeInstancesRequest.setInstanceIds(JsonUtils.obj2json(instanceIds));
        return ecsCallService.callOpenApi(describeInstancesRequest);
    }

    /**
     * 随机选取一个交换机ID
     * @Author houzhen
     * @Date 16:02 2019/6/11
     **/
    private String randomVSwitchId() {
        String vSwitchId = null;
        List<DescribeVSwitchesResponse.VSwitch> vSwitchList = getVSwitches();
        if (!CollectionUtils.isEmpty(vSwitchList)) {
            Random random = new Random();
            DescribeVSwitchesResponse.VSwitch vSwitch =  vSwitchList.get(random.nextInt(vSwitchList.size()));
            vSwitchId = vSwitch.getVSwitchId();
        }
        return vSwitchId;
    }

    /**
     * 查询全部交换机
     * @Author houzhen
     * @Date 15:49 2019/6/11
     **/
    private List<DescribeVSwitchesResponse.VSwitch> getVSwitches() {
        List<DescribeVSwitchesResponse.VSwitch> vSwitchList = null;
        DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
        DescribeVSwitchesResponse describeVSwitchesResponse = ecsCallService.callOpenApi(request);
        if (describeVSwitchesResponse != null) {
            vSwitchList = describeVSwitchesResponse.getVSwitches();
        }
        return vSwitchList;
    }

    /**
     * 数据库中插入虚拟机实例
     * @Author houzhen
     * @Date 14:16 2019/6/5
     **/
    private void insertRunInstances(RunInstancesRequest runInstancesRequest, String instanceId, Map<String, Object> templateMap) {
        // 虚拟机实例pojo
        EcsInstance ecsInstance = composeVmInstance(runInstancesRequest, instanceId, templateMap);
        if (ecsInstance.getId() != null && ecsInstance.getId() > 0){
            ecsInstanceMapper.updateByPrimaryKeySelective(ecsInstance);
        }else {
            ecsInstanceMapper.insertSelective(ecsInstance);
        }
    }

    /**
     * 组织虚拟机参数（数据库）
     * @Author houzhen
     * @Date 9:38 2019/6/10
     **/
    private EcsInstance composeVmInstance(RunInstancesRequest runInstancesRequest, String instanceId, Map<String, Object> templateMap) {
        EcsInstance vmInstance = ecsInstanceMapper.selectOneByExample(getExample(instanceId, null));
        if (vmInstance == null){
            vmInstance = new EcsInstance();
        }
        vmInstance.setInstanceId(instanceId);
        vmInstance.setEcsType(String.valueOf(templateMap.get(ECSConstants.instanceType)));
        vmInstance.setRegionId(aliYunConfig.regionId);
        vmInstance.setImageId(runInstancesRequest.getImageId());
        vmInstance.setSecurityGroupIds(runInstancesRequest.getSecurityGroupId());
        vmInstance.setvSwitchId(runInstancesRequest.getVSwitchId());
        vmInstance.setInstanceType(runInstancesRequest.getInstanceType());
        vmInstance.setInternetMaxBandwidthIn(runInstancesRequest.getInternetMaxBandwidthIn());
        vmInstance.setInternetMaxBandwidthOut(runInstancesRequest.getInternetMaxBandwidthOut());
        vmInstance.setInstanceName(runInstancesRequest.getInstanceName());
        vmInstance.setHostName(runInstancesRequest.getHostName());
        vmInstance.setUsername(String.valueOf(templateMap.get(ECSConstants.instanceAccount)));
        vmInstance.setPassword(runInstancesRequest.getPassword());
        vmInstance.setPort(Integer.valueOf(String.valueOf(templateMap.get(ECSConstants.instancePort))));
        vmInstance.setDescription(runInstancesRequest.getDescription());
        vmInstance.setSystemDiskSize(runInstancesRequest.getSystemDiskSize());
        vmInstance.setSystemDiskCategory(runInstancesRequest.getSystemDiskCategory());
        vmInstance.setAutoReleaseTime(runInstancesRequest.getAutoReleaseTime());
        vmInstance.setInternetChargeType(runInstancesRequest.getInternetChargeType());
        vmInstance.setPeriod(runInstancesRequest.getPeriod());
        vmInstance.setAutoRenew(runInstancesRequest.getAutoRenew() ? ECSAutoRenew.YES.getCode() : ECSAutoRenew.NO.getCode());
        vmInstance.setPeriodUnit(runInstancesRequest.getPeriodUnit());
        vmInstance.setInstanceChargeType(runInstancesRequest.getInstanceChargeType());
        vmInstance.setStatus(ECSStatus.PENDING.getCode());
        return vmInstance;
    }

    /**
     * 检查实例状态
     * @Author houzhen
     * @Date 15:30 2019/6/4
     **/
    private void checkRunningStatusToInstance(String instanceId) {
        if (StringUtils.isEmpty(instanceId)) {
            return;
        }
        // 开始时间
        Long startTime = System.currentTimeMillis();
        for(;;) {
            sleepSomeTime(aliYunConfig.ecsStatusCheckIntervalTime);
            DescribeInstancesResponse describeInstancesResponse = getDescribeInstance(instanceId);
            Long timeStamp = System.currentTimeMillis();
            if(describeInstancesResponse == null) {
                continue;
            } else {
                DescribeInstancesResponse.Instance instance = describeInstancesResponse.getInstances().get(0);
                if (ECSStatus.RUNNING.getStatus().equalsIgnoreCase(instance.getStatus())) {
                    // 更改数据库中实例状态
                    updateRunInstanceStatus(instance.getInstanceId(), ECSStatus.RUNNING.getCode());
                    logger.info(String.format("Instance boot successfully: %s", instance.getInstanceId()));
                    return;
                }
            }
            if(timeStamp - startTime > aliYunConfig.ecsStatusTotalCheckTime) {
                updateRunInstanceStatus(instanceId, ECSStatus.TIMEOUT.getCode());
                logger.error(String.format("Instances boot failed within %s mins: %s", aliYunConfig.ecsStatusTotalCheckTime /60000, instanceId));
                throw new ProvisioningException(BundleKey.VIRTUAL_CREATE_ERROR, BundleKey.VIRTUAL_CREATE_ERROR_MSG);
            }
        }
    }

    /**
     * 添加自动快照策略
     * @Author houzhen
     * @Date 14:59 2019/6/13
    **/
    private void addAutoSnapshotPolicy(String instanceId, Map<String, Object> templateMap) {
        if (StringUtils.isBlank(instanceId)) {
            return;
        }
        Object autoSnapshotPolicyId = templateMap.get(ECSConstants.autoSnapshotPolicy);
        if (autoSnapshotPolicyId == null) {
            return;
        }
        SnapshotRequest snapshotRequest = new SnapshotRequest();
        snapshotRequest.setInstanceId(instanceId);
        snapshotRequest.setAutoSnapshotPolicyId(String.valueOf(autoSnapshotPolicyId));
        ecsSnapshotService.applyAutoSnapshotPolicy(snapshotRequest);
    }

    /**
     * 释放虚拟机实例
     * @Author houzhen
     * @Date 16:22 2019/6/5
     **/
    private DeleteInstanceResponse deleteRunInstance(String instanceId, boolean force) {
        DeleteInstanceRequest request = new DeleteInstanceRequest();
        request.setInstanceId(instanceId);
        request.setForce(force);
        return ecsCallService.callOpenApi(request);
    }

    /**
     * 复制
     * @param ecsInstanceList
     * @return
     */
    private List<EcsInstanceDto> copyProperties(List<EcsInstance> ecsInstanceList){
        if (!CollectionUtils.isEmpty(ecsInstanceList)){
            return ecsInstanceList.stream()
                    .filter(Objects::nonNull)
                    .map(ecsInstance -> {
                        return copyProperties(ecsInstance);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 复制
     * @param ecsInstance
     * @return
     */
    private EcsInstanceDto copyProperties(EcsInstance ecsInstance){
        if (ecsInstance != null){
            EcsInstanceDto dbGroupDto = new EcsInstanceDto();
            BeanUtils.copyProperties(ecsInstance, dbGroupDto);
            dbGroupDto.setTid(ecsInstance.getId());
            return dbGroupDto;
        }
        return null;
    }
}
