package com.etocrm.listener;

import com.etocrm.config.BaseConfig;
import com.etocrm.service.InitDataService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

@Service
public class InitDataListener implements InitializingBean, ServletContextAware {

    @Resource
    private InitDataService initDataService;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 加载 context时 ， 赋值给config
     * @param servletContext
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        List<Map<String, String>> data = initDataService.initDataConfig(BaseConfig.projectName);
        if (data.size()!=0){
            for (Map<String,String> map:data){
                if (map.get("name").equals("weChat")){
                    BaseConfig.weChat = map.get("url");
                }
                if (map.get("name").equals("eurka")){
                    BaseConfig.eurka = map.get("url");
                }
            }
        }
    }
}
