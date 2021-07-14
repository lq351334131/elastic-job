package com.etocrm.service.impl;

import com.etocrm.pojo.EventTraceDataSourceConfiguration;
import com.etocrm.service.EventTraceDataSourceConfigurationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EventTraceDataSourceConfigurationServiceImpl implements EventTraceDataSourceConfigurationService {

    @Value("${jobEventConfig.name}")
    private String name;

    @Value("${jobEventConfig.driverClassName}")
    private String driver;

    @Value("${jobEventConfig.url}")
    private String url;

    @Value("${jobEventConfig.username}")
    private String username;

    @Value("${jobEventConfig.password}")
    private String password;

    private boolean activated = true;

    @Override
    public Collection<EventTraceDataSourceConfiguration> getDataSource(HttpServletRequest request) {
        EventTraceDataSourceConfiguration dataSourceConfiguration = new EventTraceDataSourceConfiguration(
                name,driver,url,username,password,activated);
        List<EventTraceDataSourceConfiguration> eventTraceDataSourceConfigurationList = new ArrayList<>();
        eventTraceDataSourceConfigurationList.add(dataSourceConfiguration);
        return eventTraceDataSourceConfigurationList;
    }

}
