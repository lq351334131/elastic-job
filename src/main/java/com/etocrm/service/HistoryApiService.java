package com.etocrm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etocrm.pojo.HistoryState;
import com.etocrm.pojo.HistroyTrace;
import com.etocrm.vo.HistoryStateVo;
import com.etocrm.vo.HistroyTraceVo;

public interface  HistoryApiService {
	
	  int   countTrace(HistroyTraceVo histroyVo);
		
	  List<HistroyTrace>  getTrace(HistroyTraceVo histroyVo);   
	   
		
	  int  countState(HistoryStateVo histroyVo);   
	   
	  List<HistoryState>     getState(HistoryStateVo histroyVo);

	  List<HistroyTrace> getTrace1(HistroyTraceVo histroyVo);

	  int countTrace1(HistroyTraceVo histroyVo);
	  
	  int   countState1(HistoryStateVo histroyVo);
		
	   List<HistoryState>  getState1(HistoryStateVo histroyVo);

	   
	   

}
