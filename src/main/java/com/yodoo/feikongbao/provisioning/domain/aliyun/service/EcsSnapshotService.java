package com.yodoo.feikongbao.provisioning.domain.aliyun.service;

import com.aliyuncs.ecs.model.v20140526.*;
import com.yodoo.feikongbao.provisioning.domain.aliyun.dto.SnapshotRequest;
import com.yodoo.feikongbao.provisioning.domain.aliyun.dto.VmSnapshotResponse;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.BeanCopyUtils;
import com.yodoo.feikongbao.provisioning.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Date 2019/6/13 10:26
 * @Created by houzhen
 */
@Service
public class EcsSnapshotService {

    private static Logger logger = LoggerFactory.getLogger(EcsSnapshotService.class);

    @Autowired
    private ECSCallService ecsCallService;

    @Autowired
    private EcsDiskService ecsDiskService;

    /**
     * 查询快照列表
     * @Author houzhen
     * @Date 20:02 2019/6/13
    **/
    public List<VmSnapshotResponse> describeSnapshots(SnapshotRequest request) {
        DescribeSnapshotsRequest describeSnapshotsRequest = new DescribeSnapshotsRequest();
        if (!StringUtils.isEmpty(request.getInstanceId())) {
            describeSnapshotsRequest.setInstanceId(request.getInstanceId());
        }
        if (!StringUtils.isEmpty(request.getDiskId())) {
            describeSnapshotsRequest.setDiskId(request.getDiskId());
        }
        List<VmSnapshotResponse> responseList = null;
        DescribeSnapshotsResponse aliResponse = ecsCallService.callOpenApi(describeSnapshotsRequest);
        if (aliResponse != null && !CollectionUtils.isEmpty(aliResponse.getSnapshots())) {
            List<DescribeSnapshotsResponse.Snapshot> aliSnapshotList =   aliResponse.getSnapshots();
            responseList = BeanCopyUtils.copyList(aliSnapshotList, VmSnapshotResponse.class);
        }
        return responseList;
    }

    /**
     * 手动创建快照
     * @Author houzhen
     * @Date 11:09 2019/6/13
    **/
    public String createSnapshot(SnapshotRequest request) {
        // 验证参数
        if (request == null || (StringUtils.isBlank(request.getDiskId()) && StringUtils.isBlank(request.getInstanceId()))) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 确定磁盘Id
        String diskId = getDiskIdByInstanceId(request.getInstanceId(), request.getDiskId());
        CreateSnapshotRequest createSnapshotRequest = new CreateSnapshotRequest();
        createSnapshotRequest.setDiskId(diskId);
        createSnapshotRequest.setSnapshotName("handle-" + System.currentTimeMillis());
        CreateSnapshotResponse response = ecsCallService.callOpenApi(createSnapshotRequest);
        // 返回快照id
        return response.getSnapshotId();
    }

    /**
     * 为某个磁盘设置自动快照策略
     * @Author houzhen
     * @Date 10:52 2019/6/13
    **/
    public void applyAutoSnapshotPolicy(SnapshotRequest request) {
        // 参数校验
        if (request == null || StringUtils.isEmpty(request.getAutoSnapshotPolicyId())
                || (StringUtils.isEmpty(request.getDiskId()) && StringUtils.isEmpty(request.getInstanceId()))) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 确定磁盘Id
        String diskId = getDiskIdByInstanceId(request.getInstanceId(), request.getDiskId());
        // 请求设置参数
        ApplyAutoSnapshotPolicyRequest autoSnapshotPolicyRequest = new ApplyAutoSnapshotPolicyRequest();
        autoSnapshotPolicyRequest.setDiskIds(JsonUtils.obj2json(diskId.split(",")));
        autoSnapshotPolicyRequest.setAutoSnapshotPolicyId(request.getAutoSnapshotPolicyId());
        ecsCallService.callOpenApi(autoSnapshotPolicyRequest);
    }

    /**
     * 取消某个磁盘的自动快照策略
     * @Author houzhen
     * @Date 11:00 2019/6/13
    **/
    public void cancelAutoSnapshotPolicy(SnapshotRequest request) {
        // 验证参数
        if (request == null || (StringUtils.isEmpty(request.getDiskId()) && StringUtils.isEmpty(request.getInstanceId()))) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 确定磁盘Id
        String diskId = getDiskIdByInstanceId(request.getInstanceId(), request.getDiskId());
        CancelAutoSnapshotPolicyRequest autoSnapshotPolicyRequest = new CancelAutoSnapshotPolicyRequest();
        autoSnapshotPolicyRequest.setDiskIds(diskId);
        ecsCallService.callOpenApi(autoSnapshotPolicyRequest);
    }

    /**
     * 根据ecs实例id查询系统盘Id
     * @Author houzhen
     * @Date 15:42 2019/6/13
    **/
    private String getDiskIdByInstanceId(String instanceId, String diskId) {
        // 判断磁盘id是否传递
        if (StringUtils.isEmpty(diskId)) {
            // 根据ecs实例查询
            DescribeDisksResponse.Disk systemDisk = ecsDiskService.getSystemDiskByInstanceId(instanceId);
            diskId = systemDisk.getDiskId();
        }
        return diskId;
    }
}
