package com.etocrm.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.etocrm.dao.db2.HistroyDao;
import com.etocrm.pojo.HistoryState;
import com.etocrm.pojo.HistroyTrace;
import com.etocrm.service.HistoryApiService;
import com.etocrm.vo.HistoryStateVo;
import com.etocrm.vo.HistroyTraceVo;

@Service
public class HistoryApiServiceImpl implements HistoryApiService{
	
	@Autowired
	private   HistroyDao  histroyDao;

	@Override
	public int countTrace(HistroyTraceVo histroyVo) {
		return histroyDao.countTrace(histroyVo);
	}

	@Override
	public List<HistroyTrace> getTrace(HistroyTraceVo histroyVo) {
		List<HistroyTrace> trace = histroyDao.getTrace(histroyVo);
		if(CollectionUtils.isEmpty(trace)) {
			return   Collections.emptyList();
		}
		return trace;
	}

	@Override
	public int countState(HistoryStateVo histroyVo) {
		return histroyDao.countState(histroyVo);
	}

	@Override
	public List<HistoryState>   getState(HistoryStateVo histroyVo) {
		List<HistoryState> state = histroyDao.getState(histroyVo);
		if(CollectionUtils.isEmpty(state)) {
					return   Collections.emptyList();
		}
      return state;
	}

	@Override
	public int countTrace1(HistroyTraceVo histroyVo) {
		return histroyDao.countTrace1(histroyVo);
	}

	@Override
	public List<HistroyTrace> getTrace1(HistroyTraceVo histroyVo) {
		List<HistroyTrace> trace = histroyDao.getTrace1(histroyVo);
		if(CollectionUtils.isEmpty(trace)) {
			return   Collections.emptyList();
		}
		return trace;
	}

	@Override
	public int countState1(HistoryStateVo histroyVo) {
		return histroyDao.countState1(histroyVo);
	}

	@Override
	public List<HistoryState>   getState1(HistoryStateVo histroyVo) {
		List<HistoryState> state = histroyDao.getState1(histroyVo);
		if(CollectionUtils.isEmpty(state)) {
					return   Collections.emptyList();
		}
      return state;
	}

}
