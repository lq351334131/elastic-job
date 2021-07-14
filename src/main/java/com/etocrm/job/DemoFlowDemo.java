package com.etocrm.job;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

/**
 * @author xing.liu
 *流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去； 
 *非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。
 * 2019年10月28日
 */
@Component
public class DemoFlowDemo   implements  DataflowJob<String>{
    private static final Logger logger = LoggerFactory.getLogger(DemoFlowDemo.class);

	@Override
	public List<String> fetchData(ShardingContext shardingContext) {
		 logger.info(String.format("------Thread ID: %s, 任务分片数: %s, " +
                 "当前分片项: %s.当前参数: %s," +
                 "当前任务名称: %s.当前任务参数: %s"
         ,
         Thread.currentThread().getId(),
         shardingContext.getShardingTotalCount(),
         shardingContext.getShardingItem(),
         shardingContext.getShardingParameter(),
         shardingContext.getJobName(),
         shardingContext.getJobParameter()
 ));
		List<String> demoList = new ArrayList<String>();
        demoList.add("111" );
        try{
            Thread.sleep(1000*60);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return demoList;
	}

	/* 
	* 如果采用流式作业处理方式，建议processData处理数据后更新其状态，避免fetchData再次抓取到，从而使得作业永不停止
	*/
	@Override
	public void processData(ShardingContext shardingContext, List<String> data) {
		 logger.info(String.format("processData------Thread ID: %s, 任务分片数: %s, " +
                 "当前分片项: %s.当前参数: %s," +
                 "当前任务名称: %s.当前任务参数: %s"
         ,
         Thread.currentThread().getId(),
         shardingContext.getShardingTotalCount(),
         shardingContext.getShardingItem(),
         shardingContext.getShardingParameter(),
         shardingContext.getJobName(),
         shardingContext.getJobParameter()
 ));
        String testData=data != null? data.get(0):"";
		
	}


}
