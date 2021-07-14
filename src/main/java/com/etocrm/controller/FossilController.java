package com.etocrm.controller;

import com.etocrm.pojo.Result;
import com.etocrm.pojo.ResultGenerator;
import com.etocrm.util.FossilDecryptUtil;
import com.etocrm.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/fossil")
public class FossilController {

    private final static Logger logger = LoggerFactory.getLogger(FossilController.class);

    @Autowired
    private FossilDecryptUtil decryptUtil;

    private final static String REQUESTIP = "10.10.82.16";

    private final static String REQUESTIP1 = "10.20.20.146";

    private final static String SECRETKEY = "thisissecret";



    @RequestMapping(value = "/decrypt",method = RequestMethod.POST)
    public Result decrypt(HttpServletRequest request, @RequestBody Map<String,String> map){
        String remoteAddr = request.getRemoteAddr();
        logger.info("request ip : " + remoteAddr);
        if (remoteAddr.equals(REQUESTIP1) || remoteAddr.equals(REQUESTIP)) {
            //IP通过
            if (map.get("timespan")==null || map.get("timespan").equals("") || map.get("ciphertext")==null ||
                    map.get("ciphertext").equals("") || map.get("sign")==null || map.get("sign").equals("")){
                return ResultGenerator.genFailResult("Invalid params");
            }
            if (!MD5Utils.verification(map.get("timespan"),map.get("ciphertext"),map.get("sign"))){
                return ResultGenerator.genFailResult("Invalid sign");
            }
            logger.info("decrypt 入参 " + map);
            try {
                String decrypt = decryptUtil.decrypt(map.get("ciphertext"),SECRETKEY);
                return ResultGenerator.genSuccessResult(decrypt);
            } catch (Exception e) {
                return ResultGenerator.genFailResult(e.getMessage());
            }
        }else{
            return ResultGenerator.genFailResult("Invalid request");
        }

    }

}
