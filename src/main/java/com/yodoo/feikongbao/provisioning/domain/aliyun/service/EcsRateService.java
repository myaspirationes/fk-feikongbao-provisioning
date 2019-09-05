package com.yodoo.feikongbao.provisioning.domain.aliyun.service;

import com.aliyuncs.cms.model.v20190101.DescribeMetricMetaListRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricMetaListResponse;
import com.aliyuncs.cms.model.v20190101.DescribeMetricTopRequest;
import com.aliyuncs.cms.model.v20190101.DescribeMetricTopResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodoo.feikongbao.provisioning.domain.paas.dto.EcsInstanceDto;
import com.yodoo.feikongbao.provisioning.domain.aliyun.dto.EcsRateDto;
import com.yodoo.feikongbao.provisioning.domain.aliyun.enums.RateEnum;
import com.yodoo.feikongbao.provisioning.domain.aliyun.dto.Datapoints;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description 使用率服务
 * @Author jinjun_luo
 * @Date 2019/6/25 17:08
 **/
@Service
public class EcsRateService {

    private static Logger logger = LoggerFactory.getLogger(EcsRateService.class);

    @Autowired
    private ECSCallService ecsCallService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EcsInstanceService ecsInstanceService;

    /**
     * 查询用户下监控指标使用率
     * 1、根据条件查询数据库实例:
     *      1、实例类型不为空，安实例类型查询。
     *      2、实例id不为空，安实例id查询
     *      3、1和2都不为空，安两个条件查询
     *      4、都不设置条件，用户下所有的实例
     * 2、判断用户是否指定 监控指标项目，
     *      1、是：通过实例 id 和监控指标名称查询
     *      2、否：先查询阿里获取所有监控指标项目名称，再通过实例 id 和监控指标项目名称查询
     * @param rateRequest 必传参数 namespace
     * @return
     */
    public List<EcsRateDto> getEcsRateList(EcsRateDto rateRequest) {
        if (rateRequest == null || StringUtils.isBlank(rateRequest.getNamespace())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询数据库
        List<EcsInstanceDto> ecsInstanceDtoList = selectEcsInstance(rateRequest);
        // 指定监控指标项目
        if (StringUtils.isNoneBlank(rateRequest.getMetricName())){
            return getEcsRateByInstanceIdAndMetricName(ecsInstanceDtoList, rateRequest);
        }else {
            // 没指定监控指标项目
            return getEcsRateByInstanceId(ecsInstanceDtoList, rateRequest);
        }
    }

    /**
     * 指定查询指标名称
     * @param ecsInstanceDtoList
     * @param rateRequest
     * @return
     */
    private List<EcsRateDto> getEcsRateByInstanceIdAndMetricName(List<EcsInstanceDto> ecsInstanceDtoList, EcsRateDto rateRequest) {
        return ecsInstanceDtoList.stream()
                .filter(Objects::nonNull)
                .map(instanceDto -> {
                    rateRequest.setInstanceId(instanceDto.getInstanceId());
                    return getDescribeMetricTop(rateRequest);
                })
                .filter(Objects::nonNull)
                .filter(ecsRateDto -> !CollectionUtils.isEmpty(ecsRateDto.getDatapoints()))
                .collect(Collectors.toList());
    }

    /**
     * 没有指定查询监控指标名称
     *  1、先查询所有监控指标项目名称
     *  2、再通过监控指标项目名称和实例id查询使用率
     * @param ecsInstanceDtoList
     * @param rateRequest
     * @return
     */
    private List<EcsRateDto> getEcsRateByInstanceId(List<EcsInstanceDto> ecsInstanceDtoList, EcsRateDto rateRequest) {
        // 查询所有的监控指标名称
        DescribeMetricMetaListResponse describeMetricMetaList = getDescribeMetricMetaList(rateRequest);
        // 查询使用率
        List<List<EcsRateDto>> ecsRateDto = ecsInstanceDtoList.stream()
                .filter(Objects::nonNull)
                .map(ecsInstanceDto -> {
                    rateRequest.setInstanceId(ecsInstanceDto.getInstanceId());
                    // 每个实例查询所有的监控指标
                    return describeMetricMetaList.getResources().stream()
                            .filter(Objects::nonNull)
                            .filter(describeMetricMetaListResponse -> !describeMetricMetaListResponse.getMetricName().contains("."))
                            .map(describeMetricMetaListResponse -> {
                                rateRequest.setMetricName(describeMetricMetaListResponse.getMetricName());
                                ecsInstanceService.sleepSomeTime(50);
                                return getDescribeMetricTop(rateRequest);
                            })
                            .filter(Objects::nonNull)
                            .filter(vmRateResponse -> !CollectionUtils.isEmpty(vmRateResponse.getDatapoints()))
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .filter(vmRateResponse -> !CollectionUtils.isEmpty(vmRateResponse))
                .collect(Collectors.toList());
        List<EcsRateDto> vmRateResponseList = new ArrayList<>();
        for (List<EcsRateDto> vmRateResponse: ecsRateDto) {
            vmRateResponseList.addAll(vmRateResponse);
        }
        return vmRateResponseList;
    }

    /**
     * 查询指定多个实例id 的使用率
     * 必传参数
     * 1、namespace
     * 2、实例 id
     * 3、监控项目名称
     * @param ecsRateDtoList
     * 使用 getEcsRateList
     */
    @Deprecated
    public List<EcsRateDto> getRateListByInstancesAndMetricName(List<EcsRateDto> ecsRateDtoList){
        // 验证参数
        requiredParameterCheckInstanceIdAndMetricName(ecsRateDtoList);
        return ecsRateDtoList.stream()
                .filter(Objects::nonNull)
                .map(ecsRateDto -> {
                    ecsInstanceService.sleepSomeTime(30);
                    return getDescribeMetricTop(ecsRateDto);
                })
                .filter(Objects::nonNull)
                .filter(ecsRateDto -> !CollectionUtils.isEmpty(ecsRateDto.getDatapoints()))
                .collect(Collectors.toList());
    }

    /**
     * 查询指定多个实例id 的使用率
     * 必传参数
     * 1、namespace
     * 2、实例 id
     * @param rateRequests
     * 使用 getEcsRateList
     */
    @Deprecated
    public List<EcsRateDto>  getRateListByInstances(List<EcsRateDto> rateRequests){
        // 验证参数
        requiredParameterCheckInstanceId(rateRequests);
        DescribeMetricMetaListResponse describeMetricMetaList = getDescribeMetricMetaList(rateRequests.get(0));
        List<List<EcsRateDto>> collect = rateRequests.stream()
                .filter(Objects::nonNull)
                .filter(rateRequest -> StringUtils.isBlank(rateRequest.getMetricName()))
                .map(rateRequest -> {
                    return describeMetricMetaList.getResources().stream()
                            .filter(Objects::nonNull)
                            .filter(describeMetricMetaListResponse -> !describeMetricMetaListResponse.getMetricName().contains("."))
                            .map(describeMetricMetaListResponse -> {
                                rateRequest.setMetricName(describeMetricMetaListResponse.getMetricName());
                                ecsInstanceService.sleepSomeTime(50);
                                return getDescribeMetricTop(rateRequest);
                            })
                            .filter(Objects::nonNull)
                            .filter(vmRateResponse -> !CollectionUtils.isEmpty(vmRateResponse.getDatapoints()))
                            .collect(Collectors.toList());
                })
                .filter(Objects::nonNull)
                .filter(vmRateResponses -> !CollectionUtils.isEmpty(vmRateResponses))
                .collect(Collectors.toList());

        List<EcsRateDto> vmRateResponseList = new ArrayList<>();
        for (List<EcsRateDto> vmRateResponse: collect) {
            vmRateResponseList.addAll(vmRateResponse);
        }
        return vmRateResponseList;
    }

    /**
     * 查询 在 namespace 下 指定 实例 下的 监控项目 的使用率
     * 必传参数
     * 1、namespace
     * 1、实例 id
     * 2、监控项目名称
     * @param rateRequest
     * @return
     */
    private EcsRateDto getDescribeMetricTop(EcsRateDto rateRequest) {
        if (rateRequest == null || StringUtils.isBlank(rateRequest.getNamespace()) || StringUtils.isBlank(rateRequest.getMetricName())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DescribeMetricTopRequest request = new DescribeMetricTopRequest();
        request.setNamespace(rateRequest.getNamespace());
        request.setMetricName(rateRequest.getMetricName());
        if (StringUtils.isNoneBlank(rateRequest.getInstanceId())){
            request.setDimensions("[{\"instanceId\": \"" + rateRequest.getInstanceId() + "\"}]");
        }
        request.setOrderby(rateRequest.getOrderby() == null ? RateEnum.average.name() : rateRequest.getOrderby());
        request.setStartTime(rateRequest.getStartTime());
        request.setEndTime(rateRequest.getEndTime());
        request.setLength(rateRequest.getPageSize() <= 0 ? null : String.valueOf(rateRequest.getPageSize()));
        request.setPeriod(rateRequest.getPeriod());
        return buildEcsRateDto(ecsCallService.callOpenApi(request), rateRequest);
    }

    /**
     * 参数转化
     * 1、Json 转 list
     * 2、每个对象设置 监控项目名称
     * @param describeMetricTopResponse
     * @return
     */
    private EcsRateDto buildEcsRateDto(DescribeMetricTopResponse describeMetricTopResponse, EcsRateDto ecsRateDto){
        if (describeMetricTopResponse != null){
            ecsRateDto.setCode(describeMetricTopResponse.getCode());
            ecsRateDto.setPeriod(describeMetricTopResponse.getPeriod());
        }
        if (StringUtils.isNotBlank(describeMetricTopResponse.getDatapoints())){
            try {
                List<Datapoints> responses = objectMapper.readValue(describeMetricTopResponse.getDatapoints(),
                        objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Datapoints.class));

                List<Datapoints> collect = responses.stream()
                        .filter(Objects::nonNull)
                        .map(datapoints -> {
                            datapoints.setMetricName(datapoints.getMetricName());
                            return datapoints;
                        }).filter(Objects::nonNull)
                        .collect(Collectors.toList());

                ecsRateDto.setDatapoints(collect);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("json to object list error ======");
            }
        }
        return ecsRateDto;
    }

    /**
     * 查询云监控项
     * 通常配合查询监控数据接口DescribeMetricList、DescribeMetricLast 等一起使用
     *
     * @param rateRequest
     * @return
     */
    private DescribeMetricMetaListResponse getDescribeMetricMetaList(EcsRateDto rateRequest){
        if (rateRequest == null || StringUtils.isBlank(rateRequest.getNamespace())){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        DescribeMetricMetaListRequest request = new DescribeMetricMetaListRequest();
        request.setNamespace(rateRequest.getNamespace());
        request.setMetricName(rateRequest.getMetricName());
        request.setPageNumber(rateRequest.getPageNum());
        request.setPageSize(rateRequest.getPageSize());
        DescribeMetricMetaListResponse describeMetricMetaListResponse = ecsCallService.callOpenApi(request);
        if (describeMetricMetaListResponse == null || CollectionUtils.isEmpty(describeMetricMetaListResponse.getResources())){
            throw new ProvisioningException(BundleKey.METRIC_NAME_NO_EXIST_ERROR, BundleKey.METRIC_NAME_NO_EXIST_ERROR_MSG);
        }
        return describeMetricMetaListResponse;
    }

    /**
     * 查询数据库中的虚拟机实例
     * @param rateRequest
     * @return
     */
    private List<EcsInstanceDto> selectEcsInstance(EcsRateDto rateRequest) {
        List<EcsInstanceDto> ecsInstanceDtoList = new ArrayList<>();
        // ecs 类型不为空或 ecs id 不为空
        if (StringUtils.isNoneBlank(rateRequest.getEcsType()) || !CollectionUtils.isEmpty(rateRequest.getInstanceIds())){
            // 如果 ecs id 不为空
            if (!CollectionUtils.isEmpty(rateRequest.getInstanceIds())){
                rateRequest.getInstanceIds().stream()
                        .filter(Objects::nonNull)
                        .forEach(instanceId -> {
                            List<EcsInstanceDto> ecsInstanceDtos = ecsInstanceService.getEcsInstancesByInstanceIdAneEcsType(instanceId, rateRequest.getEcsType());
                            if (!CollectionUtils.isEmpty(ecsInstanceDtos)){
                                ecsInstanceDtoList.addAll(ecsInstanceDtoList);
                            }
                        });
            }else {
                // 只指定类型
                List<EcsInstanceDto> ecsInstanceDtoByEcsType = ecsInstanceService.getEcsInstancesByInstanceIdAneEcsType(null, rateRequest.getEcsType());
                if (!CollectionUtils.isEmpty(ecsInstanceDtoByEcsType)){
                    ecsInstanceDtoList.addAll(ecsInstanceDtoByEcsType);
                }
            }
        }else {
            List<EcsInstanceDto> allEcsInstance = ecsInstanceService.getAllEcsInstance();
            if (!CollectionUtils.isEmpty(allEcsInstance)){
                ecsInstanceDtoList.addAll(allEcsInstance);
            }
        }
        if (CollectionUtils.isEmpty(ecsInstanceDtoList)){
            throw new ProvisioningException(BundleKey.NO_ECS_TYPE_INSTANCE_ERROR, BundleKey.NO_ECS_TYPE_INSTANCE_ERROR_MSG);
        }
        return ecsInstanceDtoList;
    }

    /**
     * 验证 实例 id 不能为空
     * @param rateRequests
     */
    private void requiredParameterCheckInstanceId(List<EcsRateDto> rateRequests){
        requiredParameterCheckList(rateRequests);
        rateRequests.stream().forEach(rateRequest -> {
            if (StringUtils.isBlank(rateRequest.getInstanceId())){
                throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
            }
        });
    }

    /**
     * 验证 实例 id 监控项目名称 不能为空
     * @param rateRequests
     */
    private void requiredParameterCheckInstanceIdAndMetricName(List<EcsRateDto> rateRequests){
        requiredParameterCheckList(rateRequests);
        rateRequests.stream().forEach(rateRequest -> {
            if (StringUtils.isBlank(rateRequest.getInstanceId()) || StringUtils.isBlank(rateRequest.getMetricName())){
                throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
            }
        });
    }

    /**
     * 参数校验
     * @param rateRequests
     */
    private void requiredParameterCheckList(List<EcsRateDto> rateRequests) {
        if (CollectionUtils.isEmpty(rateRequests)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
    }
}