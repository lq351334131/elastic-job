package com.etocrm.service.impl;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.etocrm.pojo.JobSettingsInfo;
import com.etocrm.pojo.Result;
import com.etocrm.pojo.ResultGenerator;
import com.etocrm.service.JobSettingsService;
import com.etocrm.util.RegCenterZkUtil;
import com.etocrm.vo.JobSettingsVo;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSettingsServiceImpl implements JobSettingsService {

    @Autowired
    private RegCenterZkUtil regCenter;

    @Override
    public JobSettingsVo getJobSettings(String jobName) {
        return regCenter.getConfig(jobName);
    }

    @Override
    public Result updateJobSettings(JobSettingsVo jobSettings) throws Exception {
        JobSettingsInfo settingsInfo = new JobSettingsInfo();
        try {
            //转换对象 修改节点数据
            BeanUtils.copyProperties(jobSettings,settingsInfo);
            regCenter.updateData(settingsInfo);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }

    @Override
    public Result deleteJob(String jobName) throws Exception {

        try {
        	regCenter.delete(jobName);
            //根据jobName ，直接delete
            regCenter.remove(jobName);
            return ResultGenerator.genSuccessResult();
        }catch (Exception e){
            return ResultGenerator.genFailResult(e.getMessage());
        }

    }
}
