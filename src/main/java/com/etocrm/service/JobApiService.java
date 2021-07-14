package com.etocrm.service;

import com.etocrm.pojo.JobBriefInfo;
import com.etocrm.pojo.Result;
import com.etocrm.vo.ShardingVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface JobApiService {

    int getJobsTotalCount(HttpServletRequest request) throws Exception;

    Collection<JobBriefInfo> jobs(HttpServletRequest request) throws Exception;

    Collection<ShardingVo> jobDetail(String jobName) throws Exception;

    Result triggerJob(String jobName);

    Result disableJob(String jobName);

    Result shutdownJob(String jobName);

    Result validJob(String jobName);

	Result disableOrEnableJobs(String jobName, String item, boolean disabled);
}
