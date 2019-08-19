package com.yodoo.feikongbao.provisioning.domain.system.service;

import com.offbytwo.jenkins.model.BuildResult;
import com.yodoo.feikongbao.provisioning.config.ProvisioningConfig;
import com.yodoo.feikongbao.provisioning.domain.paas.entity.EcsInstance;
import com.yodoo.feikongbao.provisioning.domain.paas.mapper.EcsInstanceMapper;
import com.yodoo.feikongbao.provisioning.domain.system.dto.PublishProjectDto;
import com.yodoo.feikongbao.provisioning.domain.system.entity.PublishProject;
import com.yodoo.feikongbao.provisioning.domain.system.mapper.PublishProjectMapper;
import com.yodoo.feikongbao.provisioning.enums.CompanyCreationStepsEnum;
import com.yodoo.feikongbao.provisioning.enums.ProjectStatusEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.Base64Util;
import com.yodoo.feikongbao.provisioning.util.JenkinsUtils;
import com.yodoo.megalodon.datasource.config.JenkinsConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @Author houzhen
 * @Date 10:30 2019/7/31
 **/
@Service
@Transactional(rollbackFor = Exception.class, transactionManager = ProvisioningConfig.PROVISIONING_TRANSACTION_MANAGER_BEAN_NAME)
public class PublishProjectService {

    private static Logger logger = LoggerFactory.getLogger(PublishProjectService.class);
    @Autowired
    private EcsInstanceMapper instanceMapper;

    @Autowired
    private PublishProjectMapper publishProjectMapper;

    @Autowired
    private CompanyCreateProcessService companyCreateProcessService;

    @Autowired
    private JenkinsUtils jenkinsUtils;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    /**
     * 创建所需部署的项目
     *
     * @Author houzhen
     * @Date 10:38 2019/7/31
     **/
    public void createPublishProjects(List<PublishProjectDto> projectList) {
        if (CollectionUtils.isEmpty(projectList)) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        List<PublishProject> insertList = new ArrayList<>();
        projectList.forEach(publishProjectDto -> {
            // 校验参数
            this.checkArgs(publishProjectDto);
            // 查询ecs实例
            EcsInstance ecsInstance = instanceMapper.selectByPrimaryKey(publishProjectDto.getVmInstanceId());
            if (ecsInstance == null) {
                throw new ProvisioningException(BundleKey.ECS_NOT_EXIST, BundleKey.ECS_NOT_EXIST_MSG);
            }
            PublishProject publishProject = new PublishProject();
            publishProject.setCompanyId(publishProjectDto.getCompanyId());
            publishProject.setVmInstanceId(publishProjectDto.getVmInstanceId());
            publishProject.setProjectName(publishProjectDto.getProjectName());
            publishProject.setProjectType(publishProjectDto.getProjectType());
            publishProject.setVersion(publishProjectDto.getVersion());
            publishProject.setIp(ecsInstance.getInnerIp());
            publishProject.setPort(ecsInstance.getPort());
            publishProject.setStatus(ProjectStatusEnum.TOPUBLISH.getCode());
            insertList.add(publishProject);

        });
        // 根据公司名称查询发布的信息
        List<PublishProject> oldPublishProjectList = this.getPublishProjectByCompanyId(projectList.get(0).getCompanyId());
        if (!CollectionUtils.isEmpty(oldPublishProjectList)) {
            Example example = new Example(PublishProject.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("companyId", projectList.get(0).getCompanyId());
            example.and(criteria);
            publishProjectMapper.deleteByExample(example);
        }
        // 插入发布项目
        publishProjectMapper.insertList(insertList);
        // 插入步骤信息
        companyCreateProcessService.insertCompanyCreateProcess(projectList.get(0).getCompanyId(),
                CompanyCreationStepsEnum.PUBLISH_STEP.getOrder(), CompanyCreationStepsEnum.PUBLISH_STEP.getCode());

    }

    /**
     * 校验参数
     *
     * @param dto
     */
    private void checkArgs(PublishProjectDto dto) {
        if (dto == null || dto.getCompanyId() == null || dto.getVmInstanceId() == null
                || StringUtils.isBlank(dto.getProjectName()) || StringUtils.isBlank(dto.getProjectType())
                || StringUtils.isBlank(dto.getVersion())) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
    }

    /**
     * 公司创建完成后，发布项目
     *
     * @Author houzhen
     * @Date 11:25 2019/7/31
     **/
    public void publishProjectsForCompanyCreateFinish(Integer companyId) {
        if (companyId == null) {
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 查询需要发布的项目
        List<PublishProject> publishProjectList = this.getPublishProjectByCompanyId(companyId);
        if (!CollectionUtils.isEmpty(publishProjectList)) {
            Map<String, PublishProject> jobNameMap = new HashMap<>();
            publishProjectList.forEach(entity -> {
                this.buildJob(entity.getIp());
                // TODO jobName此处需要修改
                jobNameMap.put(jenkinsConfig.jenkinsBuildJobName, entity);
            });
            // 查询项目是否发布完成
            this.checkJobStatus(jobNameMap);
        }
    }

    /**
     * 根据公司id查询公司发布的项目信息
     *
     * @param companyId
     * @return
     */
    private List<PublishProject> getPublishProjectByCompanyId(Integer companyId) {
        Example example = new Example(PublishProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("companyId", companyId);
        example.and(criteria);
        return publishProjectMapper.selectByExample(example);
    }

    /**
     * TODO 此发布方法需要修改
     *
     * @param targetServerIp
     * @return
     */
    private String buildJob(String targetServerIp) {
        Map<String, String> parameters = new HashMap<>(2);
        parameters.put("Authorization", "Basic " + Base64Util.base64Encoder(jenkinsConfig.jenkinsUsername + ":" + jenkinsConfig.jenkinsPassword));
        // TODO 参数需要填写
        parameters.put("IP", targetServerIp);
        // TODO 不同的项目，需要不同的job
        return jenkinsUtils.buildJobWithParameters(jenkinsConfig.jenkinsBuildJobName, parameters);
    }

    /**
     * 检查job状态
     *
     * @param jobNameMap
     */
    public void checkJobStatus(Map<String, PublishProject> jobNameMap) {
        Long startTime = System.currentTimeMillis();
        for (; ; ) {
            sleepSomeTime(jenkinsConfig.jenkinsCheckRunWaitTime);
            Iterator<Map.Entry<String, PublishProject>> iterator = jobNameMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, PublishProject> entry = iterator.next();
                String rs = jenkinsUtils.getJobRunResult(entry.getKey());
                if (BuildResult.SUCCESS.name().equalsIgnoreCase(rs)) {
                    // 更新项目成功
                    this.updateProjectStatus(entry.getValue(), ProjectStatusEnum.RUNNING.getCode());
                    // 移除
                    iterator.remove();
                } else if (BuildResult.FAILURE.name().equalsIgnoreCase(rs)
                        || BuildResult.ABORTED.name().equalsIgnoreCase(rs)
                        || BuildResult.CANCELLED.name().equalsIgnoreCase(rs)) {
                    // 更新失败
                    this.updateProjectStatus(entry.getValue(), ProjectStatusEnum.FAIL.getCode());
                    // 移除
                    iterator.remove();
                }
            }
            Long timeStamp = System.currentTimeMillis();
            if (jobNameMap.size() == 0) {
                return;
            }
            if (timeStamp - startTime > jenkinsConfig.jenkinsCheckRunWaitTimeTotal) {
                if (jobNameMap.size() > 0) {
                    logger.error(String.format("build jenkins failed within %s jobName: %s", jenkinsConfig.jenkinsCheckRunWaitTimeTotal / 60000, jobNameMap));
                    jobNameMap.forEach((key, value) -> {
                        this.updateProjectStatus(value, ProjectStatusEnum.FAIL.getCode());
                    });
                }
                return;
            }
        }
    }

    /**
     * 休眠
     *
     * @param sleepTime
     */
    public void sleepSomeTime(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新项目状态
     *
     * @param publishProject
     * @param status
     */
    private void updateProjectStatus(PublishProject publishProject, Integer status) {
        publishProject.setStatus(status);
        publishProjectMapper.updateByPrimaryKey(publishProject);
    }
}
