package com.etocrm.controller;

import com.etocrm.pojo.ServerBriefInfo;
import com.etocrm.service.JobServersApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping(value = "/api/servers")
public class JobServerApiController {

    @Autowired
    private JobServersApiService jobServersApiService;

    /**
     * 获取server总数.
     *
     * @return server总数
     */
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public int getJobsTotalCount(HttpServletRequest request) throws Exception {
        return jobServersApiService.getJobsTotalCount(request);
    }


    /**
     * 获取所有的server
     * @param request
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Collection<ServerBriefInfo> servers(HttpServletRequest request) throws Exception {
        return jobServersApiService.getServers(request);
    }



}
