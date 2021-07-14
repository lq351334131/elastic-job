package com.etocrm.service.impl;

import com.etocrm.config.BaseConfig;
import com.etocrm.dao.db2.ConfigDao;
import com.etocrm.service.InitDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InitDataSreviceImpl implements InitDataService {

    @Autowired
    private ConfigDao configDao;

    /**
     * init config
     * @param projectName
     * @return
     */
    @Override
    public List<Map<String, String>> initDataConfig(String projectName) {
        List<Map<String, String>> data = configDao.queryConfig(projectName);
        return data;
    }

    /**
     * load update config
     * @param projectName
     * @return
     */
    @Override
    public List<Map<String, String>> loadDataConfig(String projectName) {
        List<Map<String, String>> data = configDao.queryConfig(projectName);
        //TODO: 后续加载到config
        if (data.size()!=0){
            for (Map<String,String> map : data){
                if (map.get("name").equals("weChat")){
                    BaseConfig.weChat = map.get("url");
                }
                
                if (map.get("name").equals("eurka")){
                    BaseConfig.eurka = map.get("url");
                }
                
            }
        }
        return data;
    }
}
