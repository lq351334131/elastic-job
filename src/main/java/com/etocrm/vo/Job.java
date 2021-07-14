package com.etocrm.vo;

import java.io.Serializable;

public class Job  implements  Serializable {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * 作业名称
	 * @return
	 */
	private String jobName;

	/**
	 * 作业类型（SIMPLE，DATAFLOW）
	 */
	private String jobType;
	
	/**
	 * 任务类路径
	 */
	private String jobClass;
	
	/**
	 * cron表达式，用于控制作业触发时间
	 * @return
	 */
	private String cron;
	
	/**
	 * 作业分片总数
	 * @return
	 */
	private int shardingTotalCount = 1;

	/**
	 * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
	 * <p>分片序列号从0开始，不可大于或等于作业分片总数<p>
	 * <p>如：<p>
	 * <p>0=a,1=b,2=c<p>
	 * @return
	 */
	private String shardingItemParameters = "";

	/**
	 * 作业自定义参数
	 * <p>作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业<p>
	 * <p>例：每次获取的数据量、作业实例从数据库读取的主键等<p>
	 * @return
	 */
	private String jobParameter = "";
	
	
	private  JobParmeter  parmeter;
	
	
	private   String  userName;
	
	private  String   pkey;//唯一标志
	
	/**
	 * 请求方式：get/post
	 */
	//private  String  type;
	
	
	/*private  String  url;
	
	
	private   String  parameter;
	*/
	

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public int getShardingTotalCount() {
		return shardingTotalCount;
	}

	public void setShardingTotalCount(int shardingTotalCount) {
		this.shardingTotalCount = shardingTotalCount;
	}

	public String getShardingItemParameters() {
		return shardingItemParameters;
	}

	public void setShardingItemParameters(String shardingItemParameters) {
		this.shardingItemParameters = shardingItemParameters;
	}

	public String getJobParameter() {
		return jobParameter;
	}

	public void setJobParameter(String jobParameter) {
		this.jobParameter = jobParameter;
	}

	public JobParmeter getParmeter() {
		return parmeter;
	}

	public void setParmeter(JobParmeter parmeter) {
		this.parmeter = parmeter;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}


}
