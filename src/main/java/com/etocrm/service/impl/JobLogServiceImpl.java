package com.etocrm.service.impl;

import com.etocrm.dao.db2.LoggerDao;
import com.etocrm.pojo.HistroyTrace;
import com.etocrm.pojo.JobRequestLog;
import com.etocrm.service.JobLogService;
import com.etocrm.vo.JobRequestLogVo;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class JobLogServiceImpl implements JobLogService {

    @Autowired
    private LoggerDao loggerDao;

    @Override
    public void appendCustomizeMessage(String uid, String message) throws InterruptedException {
        loggerDao.updateCustomizeMessage(uid,message);
    }

    /**
     * 通过线程组获得线程
     *
     * @param threadId
     * @return
     */
    public static Thread findThread(long threadId) {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while(group != null) {
            Thread[] threads = new Thread[(int)(group.activeCount() * 1.2)];
            int count = group.enumerate(threads, true);
            for(int i = 0; i < count; i++) {
                if(threadId == threads[i].getId()) {
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }

	@Override
	public int count(JobRequestLogVo jobRequestLogVo) {
		return loggerDao.count(jobRequestLogVo);
	}

	@Override
	public List<JobRequestLog> get(JobRequestLogVo jobRequestLogVo) {
		List<JobRequestLog> log = loggerDao.get(jobRequestLogVo);
		if(CollectionUtils.isEmpty(log)) {
			return   Collections.emptyList();
		}
		return log;
	}

	@Override
	public List<JobRequestLog> getjob(JobRequestLogVo jobRequestLogVo) {
		List<JobRequestLog> log = loggerDao.getjob(jobRequestLogVo);
		if(CollectionUtils.isEmpty(log)) {
			return   Collections.emptyList();
		}
		return log;
	}

	@Override
	public Integer countjob(JobRequestLogVo jobRequestLogVo) {
		return loggerDao.countjob(jobRequestLogVo);
	}

	@Override
	public List<JobRequestLog> getId(String id) {
		return loggerDao.getId(id);
	}


}
