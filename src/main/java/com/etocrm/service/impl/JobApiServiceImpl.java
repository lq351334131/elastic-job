package com.etocrm.service.impl;

import com.etocrm.pojo.JobBriefInfo;
import com.etocrm.pojo.Result;
import com.etocrm.pojo.ResultGenerator;
import com.etocrm.service.JobApiService;
import com.etocrm.util.RegCenterZkUtil;
import com.etocrm.vo.ShardingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;

@Service
public class JobApiServiceImpl implements JobApiService {

    @Autowired
    RegCenterZkUtil regCenter;

    @Override
    public int getJobsTotalCount(HttpServletRequest request) throws Exception {
        return regCenter.getJobsTotalCount();
    }

    @Override
    public Collection<JobBriefInfo> jobs(HttpServletRequest request) throws Exception {
        return regCenter.getJobs();
    }

    @Override
    public Collection<ShardingVo> jobDetail(String jobName) throws Exception {
        return regCenter.getJobDetails(jobName);
    }

    @Override
    public Result triggerJob(String jobName) {
        try {
            String nodePath = "";
            nodePath = regCenter.getinstancesPath(jobName);
            if (nodePath.equals("")){
                throw new NullPointerException();
            }
            String flag = "TRIGGER";
            regCenter.opsJob(nodePath,flag);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            return ResultGenerator.genFailResult(e.getMessage());
        }

    }

    @Override
    public Result disableJob(String jobName) {
        try {
            Set<String> nodePath = null;
            nodePath = regCenter.getServerPath(jobName);
            if (nodePath == null){
                throw new NullPointerException("servers 节点为空的。");
            }
            String flag = "DISABLED";
            for (String str : nodePath){
                regCenter.opsJob(str,flag);
            }
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            return ResultGenerator.genFailResult(e.getMessage());
        }

    }

    @Override
    public Result shutdownJob(String jobName) {
        try{
            regCenter.delete(jobName);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            e.printStackTrace();
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }

    @Override
    public Result validJob(String jobName) {
        try {
            Set<String> nodePath = null;
            nodePath = regCenter.getServerPath(jobName);
            if (nodePath == null){
                throw new NullPointerException("servers 节点为空的。");
            }
            String flag = "";
            for (String str : nodePath){
                regCenter.opsJob(str,flag);
            }
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }
    
    @Override
    public Result disableOrEnableJobs(String jobName,String item,boolean disabled ) {
        try {
        	regCenter.disableOrEnableJobs(jobName, item, disabled);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }

}
