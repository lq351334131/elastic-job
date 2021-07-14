package com.etocrm.service;

import com.etocrm.pojo.ServerBriefInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface JobServersApiService {

    int getJobsTotalCount(HttpServletRequest request) throws Exception;

    Collection<ServerBriefInfo> getServers(HttpServletRequest request) throws Exception;
}
