package com.etocrm.service;

import com.etocrm.pojo.EventTraceDataSourceConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface EventTraceDataSourceConfigurationService {

    Collection<EventTraceDataSourceConfiguration> getDataSource(HttpServletRequest request);

}
