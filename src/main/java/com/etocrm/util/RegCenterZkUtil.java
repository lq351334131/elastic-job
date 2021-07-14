package com.etocrm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.pojo.JobBriefInfo;
import com.etocrm.pojo.JobSettingsInfo;
import com.etocrm.pojo.ServerBriefInfo;
import com.etocrm.vo.JobSettingsVo;
import com.etocrm.vo.ShardingVo;
import com.google.common.base.Charsets;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RegCenterZkUtil {

    private CuratorFramework client = null;

    /**
     * 获取job总数
     * @return
     * @throws Exception
     */
    public int getJobsTotalCount() throws Exception {

        if (client == null){
            createCuratorFrameword();
        }
        List<String> strings = client.getChildren().forPath("/");
        return strings.size();
    }

    /**
     * 获取server总数
     * @return
     * @throws Exception
     */
    public int getServersCount() throws Exception {

        if (client == null){
            createCuratorFrameword();
        }

        Set<String> servers = getServers();

        return servers.size();
    }

    /**
     *  获取 所有servers
     * @return
     * @throws Exception
     */
    public Set<String> getServers() throws Exception {
        if (client==null){
            createCuratorFrameword();
        }
        List<String> list = client.getChildren().forPath("/");
        Set<String> servers = new HashSet<>();

        for (String str : list){
        	if(!exists("/" + str + "/servers")) {
        		continue;
        	}
            List<String> cliServers = client.getChildren().forPath("/" + str + "/servers");
            for (String s : cliServers){
                servers.add(s);
            }
        }
        return servers;
    }


    @Value("${regCenter.namespace}")
    String namespace;

    @Value("${regCenter.serverList}")
    String serverList;

    private void createCuratorFrameword() {
        //创建重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);

        //创建zookeeper客户端
        client = CuratorFrameworkFactory.builder().connectString(serverList)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace(namespace)
                .build();
        client.start();
    }

    /**
     * 拿到所有的job
     * @return
     */
    public Collection<JobBriefInfo> getJobs() throws Exception {
        if (client == null){
            createCuratorFrameword();
        }
        List<String> jobNames = client.getChildren().forPath("/");

        List<JobBriefInfo> result = new ArrayList<>(jobNames.size());

        for (String each : jobNames){
            //TODO:根据job名称 判断当前job 是否有运行实例,没有运行实例 返回state为 已下线
            JobBriefInfo jobBriefInfo = getJobBriefInfo(each);
            if(null!=jobBriefInfo) {
            result.add(jobBriefInfo);
            }
        }

        return result;
    }

    /**
     * 判断当前job 是否有运行实例。
     * @param jobName
     * @return
     */
    private boolean checkJobInstances(String jobName) throws Exception {
        Stat stat = client.checkExists().forPath("/" + jobName + "/instances");
        return stat != null;
    }

    /**
     * 拿到job 基本属性
     * @param jobName
     * @return
     * @throws Exception
     */
    private JobBriefInfo getJobBriefInfo(String jobName) throws Exception {
        JobBriefInfo result = new JobBriefInfo();
        result.setJobName(jobName);
        if(!exists("/" + jobName + "/config")) {
        	return null;
        }

        byte[] bytes = client.getData().forPath("/" + jobName + "/config");

        String config = new String(bytes,"utf-8");

        JSONObject object = JSON.parseObject(config);

        result.setCron(object.get("cron").toString());
        result.setDescription(object.get("description").toString());
        JobBriefInfo.JobStatus jobStatus = getJobStatus(jobName);
        List<String> instanceList = new ArrayList<>();
        if (jobStatus!= JobBriefInfo.JobStatus.CRASHED){
        	if(!exists("/" + jobName + "/instances")) {
            	return null;
            }
            instanceList = client.getChildren().forPath("/" + jobName + "/instances");
        }
        result.setStatus(jobStatus);
        result.setInstanceCount(instanceList.size());
        result.setShardingTotalCount(Integer.valueOf(object.get("shardingTotalCount").toString()));

        return result;
    }


    private JobBriefInfo.JobStatus getJobStatus(String jobName) throws Exception {
    	/*List<String> list = client.getChildren().forPath("/" + jobName + "/instances");
        if (list.isEmpty()) {
            return JobBriefInfo.JobStatus.CRASHED;
        }
        if (isAllDisabled(jobNodePath)) {
            return JobBriefInfo.JobStatus.DISABLED;
        }
        if (isHasShardingFlag(jobNodePath, instances)) {
            return JobBriefInfo.JobStatus.SHARDING_FLAG;
        }*/
    	 if (!checkJobInstances(jobName)){
             return JobBriefInfo.JobStatus.CRASHED;
         }else {
          	   List<String> list = client.getChildren().forPath("/" + jobName + "/instances");
               if (list.isEmpty()) {
                	 return JobBriefInfo.JobStatus.CRASHED;
               }
         }
        if (isAllDisabled(jobName)){
            return JobBriefInfo.JobStatus.DISABLED;
        }
        return JobBriefInfo.JobStatus.OK;
    }

    /**
     * server 节点上是否有 DISABLED 标记， 是否已经失效了
     * @param jobName
     * @return
     * @throws Exception
     */
    private boolean isAllDisabled(String jobName) throws Exception {
        List<String> servers = client.getChildren().forPath("/"+jobName+"/servers");
        for (String server : servers){
            String message = new String(client.getData().forPath("/"+jobName+"/servers/" + server));
            if (message.equals("DISABLED")){
                return true;
            }
        }

        return false;
    }


    /**
     * 获取所有的server
     * @return
     */
    public Collection<ServerBriefInfo> ServerBriefInfo() throws Exception {
        List<ServerBriefInfo> result = new ArrayList<>();
        List<String> servers = new ArrayList<>();
        List<String> list = client.getChildren().forPath("/");
        for (String str : list){
        	if(!exists("/" + str + "/servers")) {
        		continue;
        	}
            List<String> cliServers = client.getChildren().forPath("/" + str + "/servers");
            for (String s : cliServers){
                servers.add(s);
            }
        }
        Map map = servers.stream().collect(Collectors.groupingBy(p -> p,Collectors.counting()));

        for (Object object : map.keySet()){
            ServerBriefInfo serverBriefInfo = new ServerBriefInfo();
            serverBriefInfo.setServerIp(object.toString());
            serverBriefInfo.setJobsNum(Integer.valueOf(map.get(object).toString()));
            result.add(serverBriefInfo);
        }
        return result;
    }

    public JobSettingsVo getConfig(String jobName) {

        try {
            byte[] bytes = client.getData().forPath("/" + jobName + "/config");
            String result = new String(bytes,"UTF-8");
            JobSettingsVo settings = JSON.parseObject(result, JobSettingsVo.class);
            return settings;
        } catch (Exception e) {
            e.printStackTrace();
            return new JobSettingsVo();
        }
    }

    /**
     * get job Detail
     * @param jobName
     * @return
     */
    public Collection<ShardingVo> getJobDetails(String jobName) throws Exception {
    	if(!exists("/"+ jobName +"/sharding")) {
    		return   Collections.EMPTY_LIST;
    	}
        List<String> childrens = client.getChildren().forPath("/"+ jobName +"/sharding");
        List<ShardingVo> result = new ArrayList<>();
        for (String str : childrens){
            result.add(getSharding(str,jobName));
        }
        return result;
    }

    /**
     * get分片信息
     * @param item
     * @param jobName
     * @return
     * @throws Exception
     */
    private ShardingVo getSharding(String item,String jobName) throws Exception {

        ShardingVo result = new ShardingVo();
        List<String> forPath = client.getChildren().forPath("/"+jobName+"/instances");
        if(forPath==null||forPath.isEmpty()) {
        	return  result;
        }
        String instanceId = client.getChildren().forPath("/"+jobName+"/instances").get(0);
        String[] split = instanceId.split("@-@");
        result.setItem(Integer.valueOf(item));
        result.setServerIp(split[0]);
        result.setInstanceId(split[1]);
       
        String disabledPath = "/" + jobName + "/sharding/" + item + "/disabled";
        Stat disabledStat = client.checkExists().forPath(disabledPath);
        boolean disabled = disabledStat == null ? false : true;

        String runningPath = "/" + jobName + "/sharding/" + item + "/running";
        Stat runningStat = client.checkExists().forPath(runningPath);
        boolean running = runningStat == null ? false : true;

        String shardingErrorPath = "/" + jobName + "/instances/" + instanceId;
        Stat shardingErrorStat = client.checkExists().forPath(shardingErrorPath);
        boolean shardingError = !(shardingErrorStat==null ? false : true);

        String failoverPath = "/" + jobName + "/sharding/" + item + "/failover";
        Stat failoverStat = client.checkExists().forPath(failoverPath);
        boolean failover = failoverStat == null ? false : true;

        result.setStatus(ShardingVo.ShardingStatus.getShardingStatus(disabled, running, shardingError));
        result.setFailover(failover);
       
        return result;
    }

    /**
     * 更新job config节点上的data为jobSetings
     * @param jobSetings
     */
    public void updateData(JobSettingsInfo jobSetings) throws Exception {
        client.setData().forPath("/" + jobSetings.getJobName() + "/config",JSONObject.toJSONString(jobSetings).getBytes());
    }

    /**
     * 根据 nodePath 下 添加 对应的flag 实现对应效果
     * 触发执行 job
     * @param nodePath
     * @param flag
     */
    public void opsJob(String nodePath,String flag) throws Exception {
        if (client.checkExists().forPath(nodePath)==null){
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodePath, flag.getBytes(Charsets.UTF_8));
        }else{
            client.setData().forPath(nodePath, flag.getBytes());
        }
    }

    /**
     * 返回 instances 节点下的实例
     * @param jobName
     * @return
     */
    public String getinstancesPath(String jobName) throws Exception {
        List<String> strings = client.getChildren().forPath("/"+jobName+"/instances");
        return strings.size()==0?"":"/"+jobName+"/instances/" + strings.get(0);
    }

    public Set<String> getServerPath(String jobName) throws Exception {
        List<String> strings = client.getChildren().forPath("/"+jobName+"/servers");
        Set<String> serversPath = new HashSet<>();
        for (String str : strings){
            serversPath.add("/"+jobName+"/servers/" + str);
        }
        return serversPath;
    }

    /**
     * 删除运行实例 shutdown
     * @param jobName
     * @throws Exception
     */
    public void delete(String jobName) throws Exception {
        Stat stat = client.checkExists().forPath("/" + jobName + "/instances");
        if (stat!=null){
            client.delete().deletingChildrenIfNeeded().forPath("/" + jobName + "/instances");
        }
    }

    /**
     * 删除节点 delete
     * @param jobName
     */
    public void remove(String jobName) throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath("/" + jobName);
    }
    
    /**
     * 分片失效
     * @param jobName
     * @param item
     * @param disabled,true失效，
     * @throws Exception
     */
    public     void disableOrEnableJobs( String jobName, final String item, final boolean disabled) throws Exception {
  	  String disabledPath = "/" + jobName + "/sharding/" + item + "/disabled";
        if (disabled) {
              Stat disabledStat = client.checkExists().forPath(disabledPath);
              if(disabledStat!=null) {
            	  client.inTransaction().check().forPath(disabledPath).and().setData().forPath(disabledPath, "".getBytes(Charsets.UTF_8)).and().commit();
              }else {
                  client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(disabledPath, "".getBytes(Charsets.UTF_8));
              }
        } else {
            client.delete().deletingChildrenIfNeeded().forPath(disabledPath);

        }
    }
    /**
     * 检查节点是否存在
     * @param node
     * @return
     * @throws Exception
     */
    public   boolean    exists(String  node) throws Exception {
        Stat shardingErrorStat = client.checkExists().forPath(node);
        return shardingErrorStat!=null?true:false;
    	
    }

}