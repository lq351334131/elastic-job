package com.etocrm.service.impl;

import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.etocrm.job.DemoFlowDemo;
import com.etocrm.job.SimpleJobDemo;
import com.etocrm.listener.MyElasticJobListener;
import com.etocrm.service.JobService;
import com.etocrm.util.ConstantsUtils;
import com.etocrm.vo.Job;
import com.google.common.base.Charsets;

@Service
public class JobServiceImpl  implements  JobService {
    private Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	
	@Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;
	
	@Autowired
    private JobEventConfiguration jobEventConfiguration;    
	
	@Autowired
	private SimpleJob simpleJob;
	
	@Autowired
	private DataflowJob<String> dataFlowJob;
	
	
	
	
	
	
	
	@Override
	 public void addjobs(final Job job ){
	    	if(ConstantsUtils.JOB_SIMPLE_TYPE.equals(job.getJobType())) {
	    		job.setJobClass(ConstantsUtils.JOB_SIMPLE_CLASS);
	    		addjobs(job,simpleJob);
	    	}else   if(ConstantsUtils.JOB_DATAFLOW_TYPE.equals(job.getJobType())) {
	    		job.setJobClass(ConstantsUtils.JOB_DATAFLOW_CLASS);
	    		addjobs(job,dataFlowJob);
	    	}else{
	    		addScriptJob(job);
			}
	    	if(StringUtils.isNotBlank(job.getUserName())) {
	    	  createzookData(job);
	    	}
	 }

	private void addScriptJob(Job job) {
		MyElasticJobListener elasticJobListener = new MyElasticJobListener();
		JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(job.getJobName(),
				job.getCron(), job.getShardingTotalCount()).build();
		ScriptJobConfiguration scriptJobConfig = new ScriptJobConfiguration(coreConfig, job.getJobParameter());
		System.out.println("script job --- > " + job.toString());
		LiteJobConfiguration config = LiteJobConfiguration.newBuilder(scriptJobConfig).build();
		new JobScheduler(zookeeperRegistryCenter,config,jobEventConfiguration,elasticJobListener).init();
	}

	private void addjobs(final Job job ,final ElasticJob simpleJob){
		 simpleJobScheduler(job.getJobName(),simpleJob,job.getCron(),job.getShardingTotalCount(),job.getShardingItemParameters(),
				 job.getJobParameter()).init();
	 }
    
	 private JobScheduler simpleJobScheduler(final String jobName ,final ElasticJob simpleJob, final String cron,
											 final int shardingTotalCount, final String shardingItemParameters,final  String   jobParameter) {
	  MyElasticJobListener elasticJobListener = new MyElasticJobListener();
	  LiteJobConfiguration liteJobConfiguration =  getLiteJobConfiguration(jobName ,simpleJob, cron, shardingTotalCount, shardingItemParameters,jobParameter);
      return new SpringJobScheduler(simpleJob, zookeeperRegistryCenter,liteJobConfiguration,jobEventConfiguration,elasticJobListener );
   } 
	private LiteJobConfiguration getLiteJobConfiguration(final String jobName ,final ElasticJob jobClass,
				final String cron, final int shardingTotalCount, final String shardingItemParameters,
				final  String  jobParameter) {
		JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(
		jobName, cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).jobParameter(jobParameter).build();
		JobTypeConfiguration  jobTypeConfiguration=null;
		if(jobClass  instanceof SimpleJobDemo) {
			 jobTypeConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass.getClass().getCanonicalName());
		}else if(jobClass  instanceof DemoFlowDemo){
			jobTypeConfiguration = new DataflowJobConfiguration(jobCoreConfiguration, jobClass.getClass().getCanonicalName(),false);
					
		}
		return LiteJobConfiguration.newBuilder(jobTypeConfiguration).overwrite(true).build();
	}
	
	
	public   void  createzookData(Job job)   {
		String  user="/user";
		try {
			if (zookeeperRegistryCenter.getClient().checkExists().forPath("/"+job.getJobName()+user)==null){
				zookeeperRegistryCenter.getClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/"+job.getJobName()+user, job.getUserName().getBytes(Charsets.UTF_8));
			}else {
				zookeeperRegistryCenter.getClient().setData().forPath("/"+job.getJobName()+user,  job.getUserName().getBytes(Charsets.UTF_8));
			}
		} catch (Exception e) {
			logger.error("error=={}", e.getMessage());
		}
	}

}
