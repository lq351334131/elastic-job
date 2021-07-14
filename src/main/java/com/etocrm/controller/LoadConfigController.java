package com.etocrm.controller;

import com.etocrm.config.BaseConfig;
import com.etocrm.pojo.Result;
import com.etocrm.pojo.ResultGenerator;
import com.etocrm.service.InitDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/load")
public class LoadConfigController {

    @Autowired
    private InitDataService initDataService;

    /**
     *  load 重新加载config
     * @return
     */
    @RequestMapping(value = "/config")
    public Result loadData(){

        try {
            List<Map<String, String>> data= initDataService.loadDataConfig(BaseConfig.projectName);
        }catch (Exception e){
            return ResultGenerator.genFailResult("加载失败！");
        }

        return ResultGenerator.genSuccessResult();
    }

}
