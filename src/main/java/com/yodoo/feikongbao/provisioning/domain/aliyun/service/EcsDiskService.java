package com.yodoo.feikongbao.provisioning.domain.aliyun.service;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.ResizeDiskRequest;
import com.aliyuncs.ecs.model.v20140526.ResizeDiskResponse;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.yodoo.feikongbao.provisioning.domain.aliyun.contract.ECSConstants;
import com.yodoo.feikongbao.provisioning.domain.aliyun.dto.DiskDto;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.aliyun.enums.DiskType;
import com.yodoo.feikongbao.provisioning.domain.aliyun.enums.ECSStatus;
import com.yodoo.feikongbao.provisioning.domain.aliyun.enums.ResizeDiskType;
import com.yodoo.feikongbao.provisioning.domain.aliyun.dto.SnapshotRequest;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.JsonUtils;
import com.yodoo.feikongbao.provisioning.util.SshUtils;
import com.yodoo.megalodon.datasource.config.AliYunConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * @Description 升级系统盘
 * @Author jinjun_luo
 * @Date 2019/6/12 16:29
 **/
@Service
public class EcsDiskService {

    private static Logger logger = LoggerFactory.getLogger(EcsDiskService.class);

    @Autowired
    private EcsInstanceService ecsInstanceService;

    @Autowired
    private ECSCallService ecsCallService;

    @Autowired
    private EcsSnapshotService ecsSnapshotService;

    @Autowired
    private AliYunConfig aliYunConfig;

    /**
     * 扩容系统盘：
     * 1、检查实例是否存在、实例状态(Running,Stopped)是否复合系统盘扩容
     * 2、检查系统是否存在和请求扩容大小是否正确：只能加不能减
     * 3、系统盘备份
     * 4、扩容
     * 5、通过命令使扩容的系统盘生效
     * 6、更新实例数据库中的数据
     * @param diskDto instanceId不为能空，newSize不能小于0
     * @return
     */
    public EcsInstanceDto updateSystemDiskSize(DiskDto diskDto) throws ProvisioningException {
        if (diskDto == null || StringUtils.isBlank(diskDto.getInstanceId())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询实例状态,同是获取实例的内网地址
        EcsInstanceDto ecsInstanceDto = checkInstanceExistAndStatus(diskDto);
        // 系统盘备份
        systemDiskSnapshot(diskDto);
        // 阿里扩容方法调用
        executeResizeDisk(diskDto);
        // 通过命令使扩容系统盘生效、更新数据库
        systemDiskEffect(ecsInstanceDto);
        return ecsInstanceDto;
    }

    /**
     * 系统盘份
     * @param diskDto
     */
    private void systemDiskSnapshot(DiskDto diskDto) {
        SnapshotRequest snapshotRequest = new SnapshotRequest();
        snapshotRequest.setDiskId(diskDto.getDiskId());
        snapshotRequest.setInstanceId(diskDto.getInstanceId());
        ecsSnapshotService.createSnapshot(snapshotRequest);
    }

    /**
     * 根据实例id查询系统盘信息
     * @param instanceId
     * @return
     */
    public DescribeDisksResponse.Disk getSystemDiskByInstanceId(String instanceId){
        return getDiskListByInstanceId(instanceId, DiskType.SYSTEM.getType()).get(0);
    }

    /**
     * 根据实例id查询系统盘信息
     * @param instanceId
     * @return
     */
    public List<DescribeDisksResponse.Disk> getDiskListByInstanceId(String instanceId, String diskType){
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
        describeDisksRequest.setInstanceId(instanceId);
        describeDisksRequest.setDiskType(diskType);
        DescribeDisksResponse describeDisksResponse = ecsCallService.callOpenApi(describeDisksRequest);
        // 云盘是否存在
        if (describeDisksResponse == null || CollectionUtils.isEmpty(describeDisksResponse.getDisks())){
            logger.error("system disk is not exist");
            throw new ProvisioningException(BundleKey.SYSTEM_DISK_NO_EXIST_ERROR, BundleKey.SYSTEM_DISK_NO_EXIST_ERROR_MSG);
        }
        return describeDisksResponse.getDisks();
    }

    /**
     * 通过命令使扩容的系统生效
     * @param ecsInstanceDto
     * @return
     * @throws JSchException
     * @throws IOException
     */
    private void systemDiskEffect(EcsInstanceDto ecsInstanceDto) {
        // ecs实例状态
        Integer status = ECSStatus.RUNNING.getCode();
        // ssh登录服务器执行扩容指令
        Session shellSession = null;
        try {
            SshUtils.DestHost host = new SshUtils.DestHost(ecsInstanceDto.getPrivateIpAddress(), ecsInstanceDto.getPort(), ecsInstanceDto.getUsername(), ecsInstanceDto.getPassword());
            shellSession = SshUtils.getJschSession(host);
            // 提交执行命令
            SshUtils.execCommandByJsch(shellSession, ECSConstants.system_Disk_growpart + ";" + ECSConstants.system_Disk_resize2fs);
        } catch (JSchException e) {
            logger.error("DiskService.systemDiskEffect JSchException:{}", JsonUtils.obj2json(e));
            // 更新实例状态
            status = ECSStatus.RESIZE_EXCEPTION.getCode();
            throw new ProvisioningException(BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR, BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR_MSG);
        } catch (IOException e) {
            logger.error("DiskService.systemDiskEffect IOException:{}", JsonUtils.obj2json(e));
            // 更新实例状态
            status = ECSStatus.RESIZE_EXCEPTION.getCode();
            throw new ProvisioningException(BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR, BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR_MSG);
        } finally {
            SshUtils.closeJsChSession(shellSession);
            // 更新实例数据库中的数据
            ecsInstanceService.updateRunInstanceStatus(ecsInstanceDto.getInstanceId(), status);
        }
    }

    /**
     * 执行扩容
     * @param diskDto
     * @return
     */
    private ResizeDiskResponse executeResizeDisk(DiskDto diskDto) {
        ResizeDiskRequest resizeDiskRequest = new ResizeDiskRequest();
        // 系统盘id
        resizeDiskRequest.setDiskId(diskDto.getDiskId());
        // offline（默认）：离线扩容，您需要重启实例以完成扩容。online：在线扩容，无需重启实例即可完成扩容。仅支持高效云盘与SSD云盘。
        resizeDiskRequest.setType(ResizeDiskType.ON_LINE.getValue());
        // 容量大小 20 G 默认
        resizeDiskRequest.setNewSize(diskDto.getNewSize());
        resizeDiskRequest.setSysReadTimeout(ECSConstants.readTimeoutMillis);
        ResizeDiskResponse resizeDiskResponse = ecsCallService.callOpenApi(resizeDiskRequest);
        if (resizeDiskResponse == null){
            throw new ProvisioningException(BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR, BundleKey.SYSTEM_REDIZE_DISK_SIZE_ERROR_MSG);
        }
        // 更新数据库磁盘大小及ecs实例状态
        ecsInstanceService.updateRunInstanceSystemDiskSize(diskDto.getInstanceId(), diskDto.getNewSize(), ECSStatus.RESIZING.getCode());
        return resizeDiskResponse;
    }

    /**
     * 1、检查数据库中的虚拟机是否存在
     * 2、判断如果虚拟机状态是否复合扩容条件
     * 3、判断虚拟机内网地址是否存在
     * 4、判断虚拟机系统盘是否存在
     * 5、扩容大小是否复合条件，只能加，不能减
     * @param diskDto
     */
    private EcsInstanceDto checkInstanceExistAndStatus(DiskDto diskDto) {
        // 1.检查数据库中ecs实例是否存在
        EcsInstanceDto ecsInstanceDto = ecsInstanceService.getByInstanceId(diskDto.getInstanceId());
        if (ecsInstanceDto == null) {
            throw new ProvisioningException(BundleKey.NO_INSTANCE_ERROR, BundleKey.NO_INSTANCE_ERROR_MSG);
        }
        // 2.检查阿里ecs状态，获取内外网ip
        String privateIpAddress = ecsInstanceService.getPrivateIpAddress(diskDto.getInstanceId());
        if (StringUtils.isEmpty(privateIpAddress)) {
            throw new ProvisioningException(BundleKey.INSTANCE_STATUS_NO_RUNNING_AND_STOPPED_ERROR, BundleKey.INSTANCE_STATUS_NO_RUNNING_AND_STOPPED_ERROR_MSG);
        }
        ecsInstanceDto.setPrivateIpAddress(privateIpAddress);
        // 3.检查系统盘大小及设置系统盘id
        DescribeDisksResponse.Disk sysDisk = getSystemDiskByInstanceId(diskDto.getInstanceId());
        // 云盘是否存在
        if (sysDisk == null){
            throw new ProvisioningException(BundleKey.SYSTEM_DISK_NO_EXIST_ERROR, BundleKey.SYSTEM_DISK_NO_EXIST_ERROR_MSG);
        }
        // 扩容大小是否复合条件，每次20G，最大500 只能加，不能减。
        if (aliYunConfig.systemDiskResizeSize +  sysDisk.getSize() > aliYunConfig.systemDiskMaximumSize){
            throw new ProvisioningException(BundleKey.SYSTEM_DISK_SIZE_ERROR, BundleKey.SYSTEM_DISK_SIZE_ERROR_MSG);
        }
        diskDto.setNewSize(aliYunConfig.systemDiskResizeSize +  sysDisk.getSize());
        diskDto.setDiskId(sysDisk.getDiskId());
        ecsInstanceDto.setSystemDiskSize(String.valueOf(aliYunConfig.systemDiskResizeSize +  sysDisk.getSize()));
        ecsInstanceDto.setSystemDiskId(sysDisk.getDiskId());
        return ecsInstanceDto;
    }
}