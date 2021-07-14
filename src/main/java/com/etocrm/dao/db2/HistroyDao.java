package com.etocrm.dao.db2;

import java.util.List;

import com.etocrm.pojo.HistoryState;
import com.etocrm.pojo.HistroyTrace;
import com.etocrm.vo.HistoryStateVo;
import com.etocrm.vo.HistroyTraceVo;

public interface HistroyDao {
	
	   int   countTrace(HistroyTraceVo histroyVo);
	
	   List<HistroyTrace>  getTrace(HistroyTraceVo histroyVo);  
	   
	   int   countState(HistoryStateVo histroyVo);
		
	   List<HistoryState>  getState(HistoryStateVo histroyVo);

	   int countTrace1(HistroyTraceVo histroyVo);

	   List<HistroyTrace> getTrace1(HistroyTraceVo histroyVo);
	   
	   int   countState1(HistoryStateVo histroyVo);
		
	   List<HistoryState>  getState1(HistoryStateVo histroyVo);
	   
	   
	   

}
