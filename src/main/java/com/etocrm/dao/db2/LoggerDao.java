package com.etocrm.dao.db2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etocrm.pojo.JobRequestLog;
import com.etocrm.vo.JobRequestLogVo;


public interface LoggerDao {

    void insertData(JobRequestLog jobRequest);

    void updateCustomizeMessage(@Param("uid") String uid, @Param("message") String message);
    
    int   count(JobRequestLogVo jobRequestLogVo);
	
	List<JobRequestLog>  get(JobRequestLogVo jobRequestLogVo);

	List<JobRequestLog> getjob(JobRequestLogVo jobRequestLogVo);

	Integer countjob(JobRequestLogVo jobRequestLogVo);  
	
	List<JobRequestLog>   getId(@Param("jobName") String id);
	
	int isSend(JobRequestLog jobRequestLog);  
    
    
}
