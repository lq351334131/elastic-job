package com.etocrm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etocrm.pojo.JobRequestLog;
import com.etocrm.vo.JobRequestLogVo;

public interface JobLogService {

    void appendCustomizeMessage(String uid, String message) throws InterruptedException;
    
    int   count(JobRequestLogVo jobName);
	
	List<JobRequestLog>  get(JobRequestLogVo jobName);

	List<JobRequestLog> getjob(JobRequestLogVo jobRequestLogVo);

	Integer countjob(JobRequestLogVo jobRequestLogVo);
	
	List<JobRequestLog>   getId(String jobName);

    

}
