package com.etocrm.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etocrm.pojo.JobBriefInfo;
import com.etocrm.pojo.Result;
import com.etocrm.pojo.UserEntity;
import com.etocrm.service.JobApiService;
import com.etocrm.vo.ShardingVo;

@RestController
@RequestMapping(value = "/api/jobs")
public class JobApiController {

    @Autowired
    private JobApiService jobApiService;

     /**
     * 获取作业总数.
     *
     * @return 作业总数
     */
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public int getJobsTotalCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return jobApiService.getJobsTotalCount(request);
    }

     /**
     * 查询所有作业
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Collection<JobBriefInfo> jobs(HttpServletRequest request) throws Exception {
        return jobApiService.jobs(request);
    }


     /**
     * job detail
     * @param jobName
     * @return
     */
    @RequestMapping(value = "/{jobName}/sharding",method = RequestMethod.GET)
    public Collection<ShardingVo> jobDetail(@PathVariable("jobName") final String jobName) throws Exception {
        return jobApiService.jobDetail(jobName);
    }

     /**
     * trigger Job
     * @param jobName
     * @return
     */
    @RequestMapping(value = "/{jobName}/trigger",method = RequestMethod.POST)
    public Result triggerJob(@PathVariable("jobName") final String jobName) throws Exception {
        return jobApiService.triggerJob(jobName);
    }

    /**
     * job 失效
     * @param jobName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{jobName}/disable",method = RequestMethod.POST)
    public Result disableJob(@PathVariable("jobName") final String jobName) throws Exception {
        return jobApiService.disableJob(jobName);
    }


    @RequestMapping(value = "/{jobName}/disable",method = RequestMethod.DELETE)
    public Result validJob(@PathVariable("jobName") final String jobName) throws Exception {
        return jobApiService.validJob(jobName);
    }


    @RequestMapping(value = "/{jobName}/shutdown",method = RequestMethod.POST)
    public Result shutdownJob(@PathVariable("jobName") final String jobName) throws Exception {
        return jobApiService.shutdownJob(jobName);
    }

    
    /**
     * Disable Sharding.
     *
     * @param jobName job name
     * @param item sharding item
     */
    @RequestMapping(value = "/{jobName}/sharding/{item}/disable",method = RequestMethod.POST)
    public Result disableOrEnableJobs(@PathVariable("jobName") final String jobName, @PathVariable("item") final String item) {
    	return jobApiService.disableOrEnableJobs(jobName, item,true);
    }

    /**
     * EnableS Sharding.
     *
     * @param jobName job name
     * @param item sharding item
     */
    @RequestMapping(value = "/{jobName}/sharding/{item}/disable",method = RequestMethod.DELETE)
    public Result enableSharding(@PathVariable("jobName") final String jobName, @PathVariable("item") final String item) {
    	return jobApiService.disableOrEnableJobs(jobName, item,false);
    }



}
