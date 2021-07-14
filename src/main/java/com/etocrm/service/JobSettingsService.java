package com.etocrm.service;

import com.etocrm.pojo.Result;
import com.etocrm.vo.JobSettingsVo;

public interface JobSettingsService {

    JobSettingsVo getJobSettings(String jobName);

    Result updateJobSettings(JobSettingsVo jobSettings) throws Exception;

    Result deleteJob(String jobName) throws Exception;
}
