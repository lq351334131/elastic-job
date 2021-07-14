package com.etocrm.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.etocrm.service.JobLogService;
import com.etocrm.service.JobService;
import com.etocrm.util.ConstantsUtils;
import com.etocrm.vo.Job;
import com.google.common.base.Charsets;

@Component
public class JobConfig  {
	
	private Logger logger = LoggerFactory.getLogger(JobConfig.class);
	
	@Autowired
	private ZookeeperRegistryCenter zookeeperRegistryCenter;
	
	@Autowired
	private JobService jobService;
	
	
	@Autowired
	private JobLogService  jobLogService;
	
	
	
	/**
	 * 动态定时任务，在Elastic Job Lite v2.1.6显示的是下线
	 * 启动项目重新加载动态任务
	 */
	@PostConstruct
    public void monitorJobRegister() {
        CuratorFramework client = zookeeperRegistryCenter.getClient();
		try {
			List<String> forPath = client.getChildren().forPath("/");
			
				for(String  s:forPath) {
			        Stat stat = client.checkExists().forPath("/" + s + "/instances");
			        if(stat!=null) {
			        	String content= new String(client.getData().forPath("/"+s+"/config"));
						Job job=JSONObject.parseObject(content, Job.class);
						List<String> list = getDisName(s);
						user(job);
						jobService.addjobs(job);
						getDisName(list);
			        }
					
				}
				updateLog();
	
				
		} catch (Exception e) {
			//如果DATAFLOW转SIMPLE异常，反之
			logger.error("error{}",e.getMessage());
			
		}
		
        
        
    }
	
	/**
	 * 失效的作业
	 */
	private List<String>  getDisName(String jobName) throws Exception {
		String path="/"+jobName+"/servers";
		List<String> strings=new ArrayList();
		List<String> servers = zookeeperRegistryCenter.getClient().getChildren().forPath(path);
        for (String server : servers){
            String message = new String(zookeeperRegistryCenter.getClient().getData().forPath(path+"/" + server));
            if (message.equals("DISABLED")){
            	strings.add(path+"/" + server);
            }
        }
		return strings;
	}
	/**
	 * 失效的作业操作DISABLED
	 */
	private  void   getDisName(	List<String> strings) throws Exception {
		for(String nodePath:strings) {
			zookeeperRegistryCenter.getClient().setData().forPath(nodePath, "DISABLED".getBytes());
		}
	}
	
	
	/**
	 * 添加-日志定时任务
	 * @throws Exception 
	 */
	public    void   updateLog() throws Exception {
        Stat stat = zookeeperRegistryCenter.getClient().checkExists().forPath("/updateLogJob");
		if(stat==null) {
			Job  job=new Job();
			job.setCron("1 * * * * ?");
			job.setJobName("updateLogJob");
			job.setJobType(ConstantsUtils.JOB_SIMPLE_TYPE);
			job.setJobClass(ConstantsUtils.JOB_SIMPLE_CLASS);
			job.setShardingTotalCount(1);
			job.setUserName("updatejob");
			jobService.addjobs(job);
		}		
	}
	
	
	public   void  user(Job   job ) throws Exception {
	    String  node="/"+job.getJobName()+"/user";
		if (zookeeperRegistryCenter.getClient().checkExists().forPath(node)!=null){
			byte[] bytes = zookeeperRegistryCenter.getClient().getData().forPath(node);
			String  string = new String(bytes,"utf-8");	
			job.setUserName(string);
		}

	}
	
	

}
