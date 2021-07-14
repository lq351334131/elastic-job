package com.etocrm.controller;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etocrm.pojo.HistoryState;
import com.etocrm.pojo.HistroyTrace;
import com.etocrm.service.HistoryApiService;
import com.etocrm.util.ConstantsUtils;
import com.etocrm.util.SortOrOrderUtils;
import com.etocrm.vo.HistoryStateVo;
import com.etocrm.vo.HistroyTraceVo;
import com.etocrm.vo.ResultVo;

@RestController
@RequestMapping(value = "/api/event-trace")
public class HistoryApiController {
	
	@Autowired
	private  HistoryApiService  historyApiService;
	
	/**
     * 解决前端传递的日期参数验证异常
     */
   @InitBinder
    protected void initBinder(WebDataBinder binder) {
        CustomDateEditor dateEditor = new CustomDateEditor(ConstantsUtils.simpleDateFormat, true);
        binder.registerCustomEditor(Date.class,dateEditor);
    }
	

	
    @RequestMapping(value = "execution",method = RequestMethod.GET)
    public ResultVo<HistroyTrace> execution(HistroyTraceVo histroyVo) throws Exception {
    	ResultVo<HistroyTrace> resultVO = new ResultVo<>();
    	if(StringUtils.isNotBlank(histroyVo.getSort())&&StringUtils.isNotBlank(histroyVo.getOrder())) {
    		String buildOrder = SortOrOrderUtils.buildOrder(SortOrOrderUtils.FIELDS_JOB_EXECUTION_LOG, histroyVo.getSort(), histroyVo.getOrder());
    		histroyVo.setOredrbySql(buildOrder);
    	}else {
    		histroyVo.setOredrbySql("order  by start_time  desc,complete_time  desc");
    	}
    	resultVO.setRows(historyApiService.getTrace(histroyVo));
    	resultVO.setTotal(historyApiService.countTrace(histroyVo));
		return resultVO;
        
    }
    @RequestMapping(value = "status",method = RequestMethod.GET)
    public ResultVo<HistoryState> status(HistoryStateVo histroyVo) throws Exception {
    	ResultVo<HistoryState> resultVO = new ResultVo<>();
    	if(StringUtils.isNotBlank(histroyVo.getSort())&&StringUtils.isNotBlank(histroyVo.getOrder())) {
    		String buildOrder = SortOrOrderUtils.buildOrder(SortOrOrderUtils.FIELDS_JOB_EXECUTION_LOG, histroyVo.getSort(), histroyVo.getOrder());
    		histroyVo.setOredrbySql(buildOrder);
    	}else {
    		histroyVo.setOredrbySql("order  by  creation_time   desc");
    	}
    	resultVO.setRows(historyApiService.getState(histroyVo));
    	resultVO.setTotal(historyApiService.countState(histroyVo));
		return resultVO;
        
    }
    
    @RequestMapping(value = "execution1",method = RequestMethod.GET)
    public ResultVo<HistroyTrace> execution1(HistroyTraceVo histroyVo) throws Exception {
    	ResultVo<HistroyTrace> resultVO = new ResultVo<>();
    	if(StringUtils.isNotBlank(histroyVo.getSort())&&StringUtils.isNotBlank(histroyVo.getOrder())) {
    		String buildOrder = SortOrOrderUtils.buildOrder(SortOrOrderUtils.FIELDS_JOB_EXECUTION_LOG, histroyVo.getSort(), histroyVo.getOrder());
    		histroyVo.setOredrbySql(buildOrder);
    	}
    	resultVO.setRows(historyApiService.getTrace1(histroyVo));
    	resultVO.setTotal(historyApiService.countTrace1(histroyVo));
		return resultVO;
        
    }
    
    @RequestMapping(value = "status1",method = RequestMethod.GET)
    public ResultVo<HistoryState> status1(HistoryStateVo histroyVo) throws Exception {
    	ResultVo<HistoryState> resultVO = new ResultVo<>();
    	if(StringUtils.isNotBlank(histroyVo.getSort())&&StringUtils.isNotBlank(histroyVo.getOrder())) {
    		String buildOrder = SortOrOrderUtils.buildOrder(SortOrOrderUtils.FIELDS_JOB_EXECUTION_LOG, histroyVo.getSort(), histroyVo.getOrder());
    		histroyVo.setOredrbySql(buildOrder);
    	}
    	resultVO.setRows(historyApiService.getState1(histroyVo));
    	resultVO.setTotal(historyApiService.countState1(histroyVo));
		return resultVO;
        
    }
    
   
}
