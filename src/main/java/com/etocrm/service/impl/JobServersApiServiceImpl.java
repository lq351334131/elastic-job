package com.etocrm.service.impl;

import com.etocrm.pojo.ServerBriefInfo;
import com.etocrm.service.JobServersApiService;
import com.etocrm.util.RegCenterZkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Service
public class JobServersApiServiceImpl implements JobServersApiService {

    @Autowired
    private RegCenterZkUtil regCenter;

    @Override
    public int getJobsTotalCount(HttpServletRequest request) throws Exception {
        return regCenter.getServersCount();
    }

    @Override
    public Collection<ServerBriefInfo> getServers(HttpServletRequest request) throws Exception {
        return regCenter.ServerBriefInfo();
    }
}
