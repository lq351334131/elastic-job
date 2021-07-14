package com.etocrm.job;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.etocrm.config.BaseConfig;
import com.etocrm.dao.db2.EmailDao;
import com.etocrm.dao.db2.LoggerDao;
import com.etocrm.pojo.JobRequestLog;
import com.etocrm.service.JobLogService;
import com.etocrm.util.GeneratorUniqueID;
import com.etocrm.util.SendEmailUtil;
import com.etocrm.util.TimeUtil;
import com.etocrm.vo.JobParmeter;

@Component
public class SimpleJobDemo implements SimpleJob {
	

	@Autowired
    private RestTemplate restTemplate;

	@Autowired
    private LoggerDao loggerDao;

	@Autowired
    private GeneratorUniqueID generatorID;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(SimpleJobDemo.class);
    
	@Autowired
	private ZookeeperRegistryCenter zCenter;
	
	
    
	@Autowired
	private   SendEmailUtil   sendMailUtil;
	
	
	 @Autowired
	 private EmailDao emailDao;

    @Override
    public void execute(ShardingContext shardingContext) {
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
        if( shardingContext.getJobName().equals("updateLogJob")) {
        	log();
        	return  ;
        }
        String jobParameter = shardingContext.getJobParameter();
        JobParmeter parseObject = JSONObject.parseObject(jobParameter,JobParmeter.class);

        if (restTemplate == null){
            logger.info("------- null");
        }

        if(parseObject==null) {
        	return ;
        }
        String jobName = shardingContext.getJobName();
        String uuid = generatorID.generateShortUuid(Thread.currentThread().getId());
        if(StringUtils.isNotBlank(parseObject.getUrl()) ) {
        	logger.info("url:"+parseObject.getUrl()+",type:"+parseObject.getType()+",parameter:"+parseObject.getParameter());
        	try {
	        	if("GET".equals(parseObject.getType().toUpperCase())) {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Content-Type",MediaType.APPLICATION_JSON_UTF8_VALUE);
                    String parameter = "uid=" + uuid ;
                    if (parseObject.getParameter()!=null && !parseObject.getParameter().equals("")){
                    	JSONObject jsonObject=JSONObject.parseObject(parseObject.getParameter());
                    	for(String key:jsonObject.keySet()){
                    		String  value=key+"="+jsonObject.get(key);
                    		parameter+="&"+value;
                    		headers.put(key, (String) jsonObject.get(key));
                    	}
                    }
                    parseObject.setParameter(parameter);
                    ResponseEntity<String> response = restTemplate.getForEntity(parseObject.getUrl() + "?" + parseObject.getParameter(),
                            String.class,headers);
                    int value = response.getStatusCode().value();
                    logger.info("get接口状态码"+value);
                    add(shardingContext,uuid, parseObject, response);
                    if(value!=200&&isSend(jobName, response.getBody())) {
    					send(value, jobName);
    				}
                }
	             if("POST".equals(parseObject.getType().toUpperCase())) {
	            	 posturl(parseObject,shardingContext);
	        	}
        	}catch (Exception e) {
				logger.error("error={}",e.getMessage());
				eurka(jobName, 500, "404 null");
				ResponseEntity<String> response  =new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				add(shardingContext,uuid, parseObject, response);
				if(isSend(jobName, response.getBody())) {
					send(500, jobName);
				}


			}
        		 
             	
        }
        
       
        
    }


	private String getResponseBoby(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[0];
        bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String str = new String(bytes);
        return str;
    }


    private   void   posturl(JobParmeter jobParmeter,ShardingContext shardingContext) {
    	//设置Http Header
        HttpHeaders headers = new HttpHeaders();
        //设置请求媒体数据类型
        headers.setContentType(MediaType.APPLICATION_JSON);
        //设置返回媒体数据类型
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String uuid = generatorID.generateShortUuid(Thread.currentThread().getId());
        String parameter = jobParmeter.getParameter();
        JSONObject kindJson=null;
        if (jobParmeter.getParameter()!=null && !jobParmeter.getParameter().equals("")){
             kindJson=JSONObject.parseObject(parameter);
        }else{
    	  kindJson=new  JSONObject();
        }
  	    kindJson.put("uid", uuid);
  	    jobParmeter.setParameter(kindJson.toJSONString());
        HttpEntity<String> formEntity = new HttpEntity<String>(jobParmeter.getParameter(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(jobParmeter.getUrl(), formEntity, String.class);
        logger.info("post接口状态码"+response.getStatusCode().value());
        int value = response.getStatusCode().value();
        String   jobName=shardingContext.getJobName();
        if(value!=200) {
        	eurka(jobName, value, response.getBody());
        }
        add(shardingContext,uuid, jobParmeter, response);
        
        if(value!=200&&isSend(jobName, response.getBody())) {
			send(value, jobName);
		}
         
    }
    
    
    private   void   add(ShardingContext shardingContext,String uuid,JobParmeter parseObject,ResponseEntity<String> response) {
        JobRequestLog jobRequest = new JobRequestLog();
        jobRequest.setId(uuid);
        jobRequest.setJobName(shardingContext.getJobName());
        jobRequest.setRequestParamter(parseObject.getParameter());
        jobRequest.setThreadId(String.valueOf(Thread.currentThread().getId()));
        jobRequest.setResponseStatusCode(String.valueOf(response.getStatusCode().value()));
        jobRequest.setResponseMessage(response.getBody());
        jobRequest.setCreateTime(simpleDateFormat.format(new Date()));
        loggerDao.insertData(jobRequest);
    }
    
    @Autowired
	private JobLogService  jobService;
	
	@Autowired
	private  RedisTemplate  redisTemplate;
    public   void  log() {
    	Set<String> keySet = redisTemplate.opsForHash().keys("log");
		if(!CollectionUtils.isEmpty(keySet)) {
			 Iterator<String> iterator = keySet.iterator();
			while( iterator.hasNext()) {
				 String key = iterator.next();
				 String object= (String) redisTemplate.opsForHash().get("log", key);
				 try {
					jobService.appendCustomizeMessage(key, object);
				} catch (Exception e) {
					logger.error("{}",e);
				}
				 
				
			}
			
		}
    
    }
    
    /**
     * 判断user节点是否存在,节点数据是否存在，存在必须为eureka
     * @param jobName
     * @return
     */
    public    String   exists(String jobName )  {
    	String string="";
		try {
			Stat stat = zCenter.getClient().checkExists().forPath("/" + jobName+"/user" );
			if(stat!=null) {
				byte[] bytes = zCenter.getClient().getData().forPath("/" + jobName+"/user");
		         string = new String(bytes,"utf-8");
	        }
			logger.info("eureka用户唯一标识"+string==""?"不存在":string);
			return   string;
		} catch (Exception e) {
			logger.error("异常=={}",e.getMessage());
			return  "";
			
		}

	}
    
    /**
     * 推送消息给eurka
     * @param jobName
     * @param responseStatusCode
     * @param responseMessage
     */
    private   void   eurka(String jobName,int  responseStatusCode,String responseMessage ) {
    	
        	String pkey = exists(jobName);
        	if("eureka".equals(pkey)) {
        		//设置Http Header
                HttpHeaders headers = new HttpHeaders();
                //设置请求媒体数据类型
                headers.setContentType(MediaType.APPLICATION_JSON);
                //设置返回媒体数据类型
                headers.add("Accept", MediaType.APPLICATION_JSON.toString());
                JSONObject kindJson=new  JSONObject();
          	    kindJson.put("jobName", jobName);
          	    kindJson.put("responseStatusCode",""+responseStatusCode);
          	    kindJson.put("responseMessage", responseMessage);
                HttpEntity<String> formEntity = new HttpEntity<String>(kindJson.toJSONString(), headers);
                ResponseEntity<String> response = restTemplate.postForEntity(BaseConfig.eurka , formEntity, String.class);
                logger.info(BaseConfig.eurka+"post接口状态码"+response.getStatusCode().value());
        		
        	}
    	

         
    }
    
    
    /**
     * 邮件通知
     * @param value
     * @param jobName
     */
    public    void   send(int   value,String  jobName) {
    	if(value!=200) {
    		Map<String, String> params=new HashMap<String, String>();
    		params.put("content","定时任务："+ jobName+",出现异常");
    		params.put("list", emailDao.getId(2).get("email"));
        	sendMailUtil.emailMessage(params);
        }
    }
    
    
    /**
     * @param jobName，message,CreateTime判断数据是否存在
     * @param message
     * @return
     */
    public   boolean   isSend(String jobName,String message) {
    	JobRequestLog  jobRequestLogVo=new JobRequestLog();
    	jobRequestLogVo.setJobName(jobName);
    	jobRequestLogVo.setResponseMessage(message);
    	
    	jobRequestLogVo.setCreateTime(TimeUtil.nyr());
    	
    	int send = loggerDao.isSend(jobRequestLogVo);
    	return   send==1?true:false;
    }
    
}
