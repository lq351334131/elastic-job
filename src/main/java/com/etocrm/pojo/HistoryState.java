package com.etocrm.pojo;
	
	import java.io.Serializable;
import java.util.Date;

/**
*  job_status_trace_log
*/
public class HistoryState implements Serializable {
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
    * original_task_id
    */
    private String originalTaskId;

    /**
    * task_id
    */
    private String taskId;

    /**
    * slave_id
    */
    private String slaveId;

    /**
    * source
    */
    private String source;

    /**
    * execution_type
    */
    private String executionType;

    /**
    * sharding_item
    */
    private String shardingItem;

    /**
    * state
    */
    private String state;

    /**
    * message
    */
    private String message;

    /**
    * creation_time
    */
    private Date creationTime;

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

    public String getOriginalTaskId() {
        return originalTaskId;
    }

    public void setOriginalTaskId(String originalTaskId) {
        this.originalTaskId = originalTaskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(String slaveId) {
        this.slaveId = slaveId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getShardingItem() {
        return shardingItem;
    }

    public void setShardingItem(String shardingItem) {
        this.shardingItem = shardingItem;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

   

}
