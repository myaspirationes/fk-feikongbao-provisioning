package com.yodoo.feikongbao.provisioning.domain.paas.service;

import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.View;
import com.yodoo.feikongbao.provisioning.enums.JenkinsEnum;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.feikongbao.provisioning.util.JenkinsUtils;
import com.yodoo.feikongbao.provisioning.util.RequestPrecondition;
import com.yodoo.feikongbao.provisioning.util.StringUtils;
import com.yodoo.megalodon.datasource.config.JenkinsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.misc.BASE64Encoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description ：jenkins 管理
 * @Author ：jinjun_luo
 * @Date ： 2019/7/3 0003
 */
@Service
public class JenkinsManagerService {

    private static Logger logger = LoggerFactory.getLogger(JenkinsManagerService.class);

    @Autowired
    private JenkinsUtils jenkinsUtils;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    /**
     * 查询指定环境下所有的 job
     * @param evnName 环境名，如 dev ,fat ....
     * @return
     */
    public Map<String, Job> getJobsByEvnName(String evnName){
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(evnName));
        return jenkinsUtils.getJobs(evnName);
    }

    /**
     * 获取指定环境下详细信息包括 job 列表
     * @param evnName 环境名，如 dev ,fat ....
     * @return
     */
    public View getViewByEvnName(String evnName){
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(evnName));
        return jenkinsUtils.getView(evnName);
    }

    /**
     * 查询指定单个 job 信息
     * @param jobName 项目名
     * @return
     */
    public Job getJobByJobName(String jobName){
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(jobName));
        return jenkinsUtils.getJob(jobName);
    }

    /**
     * 构建项目
     * @param jobName 项目名
     * @return 项目地址
     */
    public String buildJob(String jobName){
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(jobName));
        Job job = jenkinsUtils.getJob(jobName);
        if (job == null){
            logger.info("jenkins上的 job 不存在");
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        return jenkinsUtils.buildJob(jobName);
    }

    /**
     * 数据脚本迁移，json 串不能用双引或单引号，可以用其它符号，目前用#号
     * json : {"instanceId":"dev_mysql","schemas":[{"schema":"migrate_database"}]}
     * @param instanceId : 实例名
     * @param action ：执行的动作 JenkinsEnum
     * @param version ： 版本
     * @param schemaList ：schemas 可以传多个
     * @return
     */
    public String buildScriptMigrationData(String instanceId, String action, String version, List<String> schemaList){
        // 校验参数
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(instanceId, action, version));
        if (CollectionUtils.isEmpty(schemaList)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 封装参数
        Map<String, String> parameters = encapsulatingRequestParameters(instanceId, action, version, schemaList);
        return jenkinsUtils.buildJobWithParameters(jenkinsConfig.jenkinsScriptMigrationDataJobName, parameters);
    }

    /**
     * build 项目，调用倪伟接口
     * @param targetServerIP : 目标实例 ip 地址
     * @param parametersMap ： 传其它参数
     * @return
     */
    public String buildJobWithParameters(String targetServerIP, Map<String, String> parametersMap){
        // 校验参数
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(targetServerIP));
        // 封闭参数
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Authorization", "Basic "+ buildBase64Encoder( jenkinsConfig.jenkinsUsername +":"+ jenkinsConfig.jenkinsPassword));
        parameters.put("IP",targetServerIP);
        if (!CollectionUtils.isEmpty(parametersMap)){
            parameters.putAll(parametersMap);
        }
        return jenkinsUtils.buildJobWithParameters(jenkinsConfig.jenkinsBuildJobName, parameters);
    }

    /****
     * 查询 build 项目是否成功
     * @param jobName ： job 名称
     * @return boolean
     */
    public Boolean checkRunningStatusToJenkins(String jobName){
        // 校验参数
        RequestPrecondition.checkArguments(!StringUtils.isContainEmpty(jobName));
        // 开始时间
        Long startTime = System.currentTimeMillis();
        for(;;) {
            // 要睡时间长点，要不然获取到的是上一次 build 的数据
            jenkinsUtils.sleepSomeTime(jenkinsConfig.jenkinsCheckRunWaitTime);
            String jobRunResult = jenkinsUtils.getJobRunResult(jobName);
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(jobRunResult) && jobRunResult.equalsIgnoreCase(JenkinsEnum.SUCCESS.getAction())){
                return true;
            }
            // 超过一点时间，认为 build 失败
            if(System.currentTimeMillis() - startTime > jenkinsConfig.jenkinsCheckRunWaitTimeTotal) {
                logger.error(String.format("build jenkins failed within %s jobName: %s", jenkinsConfig.jenkinsCheckRunWaitTimeTotal /60000, jobName));
                return false;
            }
        }
    }

    /**
     * 用 户名 密码 冒号分隔 转 base64
     * @param str
     * @return
     */
    private String buildBase64Encoder(String str) {
        try {
            return new BASE64Encoder().encode(str.getBytes("UTF-8"));
        }catch (Exception e){
            logger.error("username ,password  转 BASE64Encoder 失败 ： {}", str, e);
        }
        return null;
    }

    /**
     * 封装参数
     * @param instanceId : 实例名
     * @param action ：执行的动作 JenkinsEnum
     * @param version ： 版本
     * @param schemaList ：schemas 可以传多个
     * @return
     */
    private Map<String, String> encapsulatingRequestParameters(String instanceId, String action, String version, List<String> schemaList) {
        // 把 schema拼接成json串
        StringBuilder schemaStringBuilder = new StringBuilder();
        schemaList.stream()
                .filter(Objects::nonNull)
                .forEach(schemaName -> {
                    schemaStringBuilder.append(",{#schema#:#"+ schemaName +"#}");
                });
        // 去掉第一个豆号
        String schemas = schemaStringBuilder.toString().substring(1, schemaStringBuilder.toString().length());
        // 封装 map 参数
        Map<String, String> parameters = new HashMap<>();
        parameters.put("instanceId","{#instanceId#:#"+ instanceId +"#,#schemas#:["+ schemas +"]}");
        parameters.put("action", action);
        parameters.put("version",version);
        return parameters;
    }
}