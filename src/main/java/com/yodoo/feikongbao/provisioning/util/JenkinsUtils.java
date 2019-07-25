package com.yodoo.feikongbao.provisioning.util;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Date 2019/6/28 17:02
 * @Created by houzhen
 */
@Component
public class JenkinsUtils {

    @Autowired
    private JenkinsServer jenkins;

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
        int LastSuccessfulNumber = 0;
        int LastUnsuccessfulNumber = 0;
        JobWithDetails job = getJobWithDetails(jobName);
        LastSuccessfulNumber = job.getLastSuccessfulBuild().getNumber();
        LastUnsuccessfulNumber = job.getLastUnsuccessfulBuild().getNumber();
        boolean flag = false;
        if (LastSuccessfulNumber == number) {
            flag = true;
        }
        if (LastUnsuccessfulNumber == number) {
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
    public boolean delJob(String jobName) {
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
    public boolean updateJob(String jobName, String config) {
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

    public static void main(String[] args) {
//        Job job = JenkinsUtils.getJob("fat-md-swift-storage-client");
//        System.out.println(JsonUtils.obj2json(job));
    }
}
