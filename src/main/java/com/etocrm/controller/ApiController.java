package com.etocrm.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.zookeeper.data.Stat;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.etocrm.pojo.EventTraceDataSourceConfiguration;
import com.etocrm.pojo.JobRequestLog;
import com.etocrm.pojo.RegistryCenterConfiguration;
import com.etocrm.pojo.Result;
import com.etocrm.pojo.ResultGenerator;
import com.etocrm.pojo.UserEntity;
import com.etocrm.pojo.ZookeeperData;
import com.etocrm.service.EventTraceDataSourceConfigurationService;
import com.etocrm.service.JobLogService;
import com.etocrm.service.JobService;
import com.etocrm.service.JobSettingsService;
import com.etocrm.service.RegistryCenterConfigurationService;
import com.etocrm.service.ZookeeperService;
import com.etocrm.util.ConstantsUtils;
import com.etocrm.vo.Job;
import com.etocrm.vo.JobParmeter;
import com.etocrm.vo.JobRequestLogVo;
import com.etocrm.vo.ResultVo;


@RestController
@RequestMapping("/api")
public class ApiController {
	
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	
	@Autowired
	private JobService jobService;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private RegistryCenterConfigurationService registryCenterConfigurationService;

	@Autowired
	private EventTraceDataSourceConfigurationService eventTraceDataSourceConfigurationService;
	
	@Autowired
	private  RedisTemplate  redisTemplate;
	
	@Autowired
	private  RestTemplate restTemplate;
	
	@Autowired
	private ZookeeperRegistryCenter zCenter;
	
	@Autowired
	private  ZookeeperService  zookeeperService;
	
	
	
	@Autowired
	private JobSettingsService  jobSettingsService;
	
	

	/**
	 * 添加job 应用
	 * @param job
	 * @return
	 */
	@PostMapping("/add-job")
	public Result addJob(@RequestBody Job job,HttpServletRequest  request) {
		UserEntity userEntity =(UserEntity) request.getSession().getAttribute("session");
		if(userEntity!=null) {
			job.setUserName(userEntity.getUsername());
		}else {
			//调用接口必须带有
			if(StringUtils.isEmpty(job.getUserName())) {
				//后台访问的时候，清楚浏览器session失效
				return ResultGenerator.genFailResult("用户名不能为空");
			}else {
				job.setUserName(job.getUserName());
			}
			
		}
		if (!StringUtils.hasText(job.getJobName())) {
			return ResultGenerator.genFailResult("名称不能为空");
		}

		if (!StringUtils.hasText(job.getCron())) {
			return ResultGenerator.genFailResult("定时任务不能为空");

		}else {
			boolean validExpression = CronExpression.isValidExpression(job.getCron());
			if(!validExpression) {
				return ResultGenerator.genFailResult("定时任务格式不正确：cron="+job.getCron());

		 }

		}

		if (StringUtils.isEmpty(job.getJobType())) {
			job.setJobType(ConstantsUtils.JOB_SIMPLE_TYPE);
		}else {
		 	boolean simple = ConstantsUtils.JOB_SIMPLE_TYPE.equals(job.getJobType());
		 	boolean dataflow = ConstantsUtils.JOB_DATAFLOW_TYPE.equals(job.getJobType());
			boolean script = ConstantsUtils.JOB_SCRIPT_TYPE.equals(job.getJobType());
			if(simple==false&&dataflow==false&&script==false) {
		   		return ResultGenerator.genFailResult("作业类型（SIMPLE，DATAFLOW，SCRIPT）");
			}
		}
		
				//后台添加数据
			JobParmeter jobParmeter=job.getParmeter();
			 if(jobParmeter==null) {
					return ResultGenerator.genFailResult(202, "parmeter对象为空");
			 }
			if(StringUtils.hasText(jobParmeter.getUrl()) && !StringUtils.hasText(jobParmeter.getType())) {
				return ResultGenerator.genFailResult("url,type");
			}else {
				job.setJobParameter(JSONObject.toJSONString(jobParmeter));
			}
			
		
		
		if(StringUtils.isEmpty(job.getShardingTotalCount())) {
			job.setShardingItemParameters("0=a");
			job.setShardingTotalCount(1);
		}
		try {
			if(!exists(job)) {
				return ResultGenerator.genFailResult(201,"作业名称："+job.getJobName()+",已经存在");
			}
			jobService.addjobs(job);
			insertZookeeper(job);
			
			
		} catch (Exception e) {
			logger.error("error{}",e.getMessage());
			return ResultGenerator.genFailResult("error,系统失败");
		}
		return ResultGenerator.genSuccessResult();
  }


	@RequestMapping(value = "/registry-center",method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<RegistryCenterConfiguration> getRegCenter(final @Context HttpServletRequest request){
		return registryCenterConfigurationService.getRegCenter(request);
	}


	@RequestMapping(value = "/data-source",method = RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<EventTraceDataSourceConfiguration> load(final @Context HttpServletRequest request) {
		return eventTraceDataSourceConfigurationService.getDataSource(request);
	}

	@RequestMapping(value = "/customize",method = RequestMethod.GET)
	public Result customizeMessage(String uid,String message){
		if (uid == null || uid.equals("")){
			return ResultGenerator.genFailResult("uid不能为null.");
		}
		if(!StringUtils.isEmpty(message)) {
			redisTemplate.opsForHash().put("log", uid, message);
		}
		return ResultGenerator.genSuccessResult();
	}
	@RequestMapping(value = "log",method = RequestMethod.GET)
	    public ResultVo<JobRequestLog> log(JobRequestLogVo jobRequestLogVo) throws Exception {
	    	ResultVo<JobRequestLog> resultVO = new ResultVo<>();
	    	resultVO.setRows(jobLogService.get(jobRequestLogVo));
	    	resultVO.setTotal(jobLogService.count(jobRequestLogVo));
			return resultVO;
	        
	}
	
	@RequestMapping(value = "log1",method = RequestMethod.GET)
    public ResultVo<JobRequestLog> log1(JobRequestLogVo jobRequestLogVo,HttpSession session ) throws Exception {
    	ResultVo<JobRequestLog> resultVO = new ResultVo<>();
    	resultVO.setRows(jobLogService.getjob(jobRequestLogVo));
    	resultVO.setTotal(jobLogService.countjob(jobRequestLogVo));
		return resultVO;
        
   }
	
	
	
	
	/**
	 * 添加zookeeper数据
	 */
	public   void    insertZookeeper(Job job) {
		ZookeeperData  zookeeperData=new ZookeeperData();
		zookeeperData.setCon(job.getCron());
		zookeeperData.setJobName(job.getJobName());
		zookeeperData.setJobParamter(job.getJobParameter());
		zookeeperData.setUsername(job.getUserName());
		try {
			int insert = zookeeperService.insert(zookeeperData);
			if(insert==0) {
				logger.error("添加数据失败");
			}
			
		} catch (Exception e) {
			logger.error("添加数据失败SQL异常{}",e.getMessage());
			return ;
		}
		
	}

	
	
	public    boolean   exists(Job job)  {
        Stat stat=null;
		try {
			stat = zCenter.getClient().checkExists().forPath("/" + job.getJobName() );
			if(stat!=null) {
	        	return   false;
	        }
		} catch (Exception e) {
			logger.error("error:zookeeper出现bug{}",e.getMessage());
			 return   false;
			
		}
        return   true;

	}
	
	@RequestMapping(value = "close",method = RequestMethod.GET)
	public Result close(String jobName) throws Exception {
		if(!StringUtils.hasText(jobName)) {
			return ResultGenerator.genFailResult("jobName必填");
		}
		Result deleteJob = jobSettingsService.deleteJob(jobName);
		if(deleteJob.getCode()==200) {
			ZookeeperData zookeeperData=new ZookeeperData();
			zookeeperData.setJobName(jobName);
			zookeeperData.setStatus("1");
			zookeeperService.update(zookeeperData);
		}
		
        return  deleteJob;
    }

	
	@RequestMapping(value = "getLog",method = RequestMethod.GET)
	public Result getLog(String jobName) {
		if(!StringUtils.hasText(jobName)) {
			return ResultGenerator.genFailResult("jobName必填");
		}
		List<JobRequestLog> id2 = jobLogService.getId(jobName);
		return  ResultGenerator.genSuccessResult().setData(id2);
       
    }
		

}
