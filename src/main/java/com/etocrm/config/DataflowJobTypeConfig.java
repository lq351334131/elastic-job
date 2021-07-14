package com.etocrm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.etocrm.job.DemoFlowDemo;
import com.etocrm.listener.MyElasticJobListener;

/**
 * @author xing.liu
 *作业类型：Dataflow类型作业
 * 2019年10月28日
 */
@Configuration
public   class DataflowJobTypeConfig  {
	

	   @Autowired
	    private ZookeeperRegistryCenter regCenter;
	    

	    @Autowired
	    private JobEventConfiguration jobEventConfiguration; 
	    
	   /* @Bean(name = "elasticJobListener1")
	    public ElasticJobListener elasticJobListener() {
	        return new MyElasticJobListener();
	    }*/
	    
 
	
    /**
     * 配置任务详细信息
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     */
	  private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends DataflowJob> jobClass,
                final String cron,
                final int shardingTotalCount,
                final String shardingItemParameters) {
	   // 数据流作业配置，第三个参数为（是否为流式处理）
		return LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(
		JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
		.shardingItemParameters(shardingItemParameters).build()
		, jobClass.getCanonicalName(),false)
		).overwrite(true).build();
	  }

}
