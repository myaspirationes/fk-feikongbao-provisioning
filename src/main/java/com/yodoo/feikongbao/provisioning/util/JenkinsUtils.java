package com.yodoo.feikongbao.provisioning.util;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import com.yodoo.feikongbao.provisioning.exception.BundleKey;
import com.yodoo.feikongbao.provisioning.exception.ProvisioningException;
import com.yodoo.megalodon.datasource.config.JenkinsConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Date 2019/6/28 17:02
 * @Author by houzhen
 */
@Component
public class JenkinsUtils {

    private static Logger logger = LoggerFactory.getLogger(JenkinsUtils.class);

    @Autowired
    private JenkinsServer jenkins;

    @Autowired
    private JenkinsConfig jenkinsConfig;

    /**
     * 获取所有任务
     *
     * @Author houzhen
     * @Date 17:11 2019/6/28
     **/
    public Map<String, Job> getJobs() {
        Map<String, Job> jobs = null;
        try {
            jobs = jenkins.getJobs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    /**
     * 获取指定环境下所有的job 列表
     *
     * @param viewName 环境
     * @return
     */
    public Map<String, Job> getJobs(String viewName) {
        try {
            return jenkins.getJobs(viewName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定环境下详情信息
     *
     * @param name 环境名
     * @return
     */
    public View getView(String name) {
        try {
            return jenkins.getView(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取某个任务
     *
     * @Author houzhen
     * @Date 17:31 2019/6/28
     **/
    public Job getJob(String jobName) {
        Job job = null;
        try {
            job = jenkins.getJob(jobName);
            if (null != job) {
                final JobWithDetails details = job.details();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return job;
    }

    /**
     * 任务详情
     *
     * @Author houzhen
     * @Date 17:41 2019/6/28
     **/
    public JobWithDetails getJobWithDetails(String jobName) {
        Job job = getJob(jobName);
        JobWithDetails jobWithDetails = null;
        try {
            if (null != job) {
                jobWithDetails = job.details();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobWithDetails;
    }

    /**
     * 创建job
     *
     * @Author houzhen
     * @Date 17:33 2019/6/28
     **/
    public Boolean createJob(String jobName, String config) {
        boolean flag = false;
        try {
            jenkins.createJob(jobName, config);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * job日志
     *
     * @Author houzhen
     * @Date 17:42 2019/6/28
     **/
    public String getJobLog(String jobName, int number) {
        JobWithDetails job = getJobWithDetails(jobName);
        Build buildByNumber = job.getBuildByNumber(number);
        String outputText = null;
        BuildWithDetails details = null;
        try {
            details = buildByNumber.details();
            outputText = details.getConsoleOutputText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputText;
    }

    /**
     * 查询 build 是否成功
     *
     * @param jobName
     * @return
     */
    public String getJobRunResult(String jobName) {
        JobWithDetails job = getJobWithDetails(jobName);
        String result = null;
        if (null != job) {
            try {
                Build build = job.getBuildByNumber(job.getLastBuild().getNumber());
                if (null != build) {
                    BuildWithDetails buildWithDetails = build.details();
                    if (null != buildWithDetails) {
                        BuildResult buildResult = buildWithDetails.getResult();
                        if (null != buildResult) {
                            return buildResult.name();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 创建的job是否成功
     *
     * @Author houzhen
     * @Date 17:43 2019/6/28
     **/
    public boolean isSuccess(String jobName, int number) {
        int lastSuccessfulNumber = 0;
        int lastUnsuccessfulNumber = 0;
        JobWithDetails job = getJobWithDetails(jobName);
        lastSuccessfulNumber = job.getLastSuccessfulBuild().getNumber();
        lastUnsuccessfulNumber = job.getLastUnsuccessfulBuild().getNumber();
        boolean flag = false;
        if (lastSuccessfulNumber == number) {
            flag = true;
        }
        if (lastUnsuccessfulNumber == number) {
            flag = false;
        }
        return flag;
    }

    /**
     * 删除job
     *
     * @Author houzhen
     * @Date 17:45 2019/6/28
     **/
    public Boolean delJob(String jobName) {
        Boolean flag = false;
        try {
            jenkins.deleteJob(jobName);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 更新job
     *
     * @Author houzhen
     * @Date 17:45 2019/6/28
     **/
    public Boolean updateJob(String jobName, String config) {
        Boolean flag = false;
        try {
            jenkins.updateJob(jobName, config);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 构建任务
     *
     * @Author houzhen
     * @Date 17:48 2019/6/28
     **/
    public String buildJobWithParameters(String jobName, Map<String, String> parameters) {
        try {
            Job job = jenkins.getJob(jobName);
            if (null != job) {
                job.build(parameters);
                return job.getUrl();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 构建任务
     *
     * @Author houzhen
     * @Date 17:48 2019/6/28
     **/
    public String buildJob(String jobName) {
        try {
            Job job = jenkins.getJob(jobName);
            if (null != job) {
                job.build();
            }
            return job.getUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 查询任务是否完成
     *
     * @Author houzhen
     * @Date 17:47 2019/6/28
     **/
    public boolean isFinished(String jobName, int number) {
        boolean flag = false;
        if (number <= 0) {
            return false;
        }
        try {
            JobWithDetails job = getJobWithDetails(jobName);
            Build buildByNumber = job.getBuildByNumber(number);
            if (null != buildByNumber) {
                BuildWithDetails details = buildByNumber.details();
                if (null != details) {
                    flag = details.isBuilding();
                } else {
                    flag = true;
                }
            } else {
                flag = true;
            }
            return !flag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 休眠
     *
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

    /****
     * 查询 build 项目是否成功
     * @param jobName ： job 名称
     * @return boolean
     */
    public Boolean checkRunningStatusToJenkins(String jobName) {
        // 校验参数
        if (StringUtils.isBlank(jobName)){
            throw new ProvisioningException(BundleKey.PARAMS_ERROR, BundleKey.PARAMS_ERROR_MSG);
        }
        // 开始时间
        Long startTime = System.currentTimeMillis();
        for (; ; ) {
            // 要睡时间长点，要不然获取到的是上一次 build 的数据
            sleepSomeTime(jenkinsConfig.jenkinsCheckRunWaitTime);
            String jobRunResult = getJobRunResult(jobName);
            if (StringUtils.isNotBlank(jobRunResult) && jobRunResult.equalsIgnoreCase(BuildResult.SUCCESS.name())) {
                return true;
            }/* else if (BuildResult.FAILURE.name().equalsIgnoreCase(jobRunResult)
                    || BuildResult.ABORTED.name().equalsIgnoreCase(jobRunResult)
                    || BuildResult.CANCELLED.name().equalsIgnoreCase(jobRunResult)) {
                return false;
            }*/
            // 超过一点时间，认为 build 失败
            if (System.currentTimeMillis() - startTime > jenkinsConfig.jenkinsCheckRunWaitTimeTotal) {
                logger.error(String.format("build jenkins failed within %s jobName: %s", jenkinsConfig.jenkinsCheckRunWaitTimeTotal / 60000, jobName));
                return false;
            }
        }
    }
}
