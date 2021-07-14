package com.etocrm.pojo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRequestLog implements Serializable {

    private static final long serialVersionUID = -2557339817707532820L;

    private String id;

    private String jobName;

    private String threadId;

    private String requestParamter;

    private String responseStatusCode;

    private String responseMessage;

    private String customizeMessage;

    private String createTime;
    
    private String  updateTime;
    
    
    
    

}
