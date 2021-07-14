package com.etocrm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.etocrm.pojo.RegistryCenterConfiguration;
import com.etocrm.service.RegistryCenterConfigurationService;

@Service
public class RegistryCenterConfigurationServiceImpl implements RegistryCenterConfigurationService {

    @Value("${regCenter.name}")
    private String name;

    @Value("${regCenter.serverList}")
    private volatile String zkAddressList;

    @Value("${regCenter.namespace}")
    private String namespace;

    private String digest = "";

    @Value("${regCenter.activated}")
    private boolean activated;


    @Override
    public Collection<RegistryCenterConfiguration> getRegCenter(HttpServletRequest request) {
        synchronized (zkAddressList){
            if (zkAddressList.contains(":")){
                zkAddressList = zkAddressList.substring(0,zkAddressList.indexOf(":"));
            }
        }

        RegistryCenterConfiguration configuration = new RegistryCenterConfiguration(name,zkAddressList,namespace,digest,activated);
        List<RegistryCenterConfiguration> configurationList = new ArrayList<>();
        configurationList.add(configuration);
        return configurationList;
    }

}
