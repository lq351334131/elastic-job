package com.etocrm.pojo;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;

/**
*  job_status_trace_log
* @author 大狼狗 2019-10-31
*/
public class HistroyTrace implements Serializable {
	
    private static final long serialVersionUID = 1L;


    /**
    * id
    */
    private String id;

    /**
    * job_name
    */
    private String jobName;

    /**
    * task_id
    */
    private String taskId;

    /**
    * hostname
    */
    private String hostname;

    /**
    * ip
    */
    private String ip;

    /**
    * sharding_item
    */
    private Integer shardingItem;

    /**
    * execution_source
    */
    private String executionSource;

    /**
    * failure_cause
    */
    private String failureCause;

    /**
    * is_success
    */
    private Integer isSuccess;

    /**
    * start_time
    */
    private Date startTime;

    /**
    * complete_time
    */
    private Date completeTime;

    public HistroyTrace() {
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getShardingItem() {
		return shardingItem;
	}

	public void setShardingItem(Integer shardingItem) {
		this.shardingItem = shardingItem;
	}

	public String getExecutionSource() {
		return executionSource;
	}

	public void setExecutionSource(String executionSource) {
		this.executionSource = executionSource;
	}

	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	
    
    

    

}