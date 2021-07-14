package com.etocrm.controller;

import com.etocrm.pojo.Result;
import com.etocrm.pojo.ResultGenerator;
import com.etocrm.pojo.ZookeeperData;
import com.etocrm.service.JobSettingsService;
import com.etocrm.service.ZookeeperService;
import com.etocrm.vo.JobSettingsVo;
import io.swagger.annotations.ApiImplicitParam;
import javafx.print.JobSettings;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs/config")
public class JobConfigController {

    @Autowired
    private JobSettingsService jobSettingsService;
    
    @Autowired
	private  ZookeeperService  zookeeperService;

    /**
     * 修改时 查询的settings
     * @param jobName
     * @return
     */
    @RequestMapping(value = "/{jobName}",method = RequestMethod.GET)
    public JobSettingsVo getJobSettings(@PathVariable("jobName") final String jobName) {
        return jobSettingsService.getJobSettings(jobName);
    }

    /**
     * job 状态为已下线时，彻底删除job
     * @param jobName
     * @return
     */
    @RequestMapping(value = "/{jobName}",method = RequestMethod.DELETE)
    public Result deleteJob(@PathVariable("jobName") final String jobName) throws Exception {
    	Result deleteJob = jobSettingsService.deleteJob(jobName);
		if(deleteJob.getCode()==200) {
			ZookeeperData zookeeperData=new ZookeeperData();
			zookeeperData.setJobName(jobName);
			zookeeperData.setStatus("1");
			zookeeperService.update(zookeeperData);
		}
        return deleteJob;
    }



    /**
     * 修改job
     * @param jobSettings
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
	@RequestMapping(value = "",method = RequestMethod.PUT)
    public Result updateJobSettings(@RequestBody final JobSettingsVo jobSettings) throws Exception {
    	return jobSettingsService.updateJobSettings(jobSettings);
    }


}
