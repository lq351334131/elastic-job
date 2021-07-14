package com.etocrm.vo;

import java.io.Serializable;
import java.util.Date;

public class HistoryStateVo  implements   Serializable{

	private static final long serialVersionUID = 9034805346888938273L;

    private  int per_page;
    
    private  int page;
    
    private  String sort;
    
    private  String order;
    
    private  Date startTime;
    
    private  Date endTime;
    
    
    private   String   jobName;
    
    
    private  String   oredrbySql;
    
    private  String  state;


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOredrbySql() {
		return oredrbySql;
	}

	public void setOredrbySql(String oredrbySql) {
		this.oredrbySql = oredrbySql;
	}

	public int getPer_page() {
		return per_page;
	}

	public void setPer_page(int per_page) {
		this.per_page = per_page;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	
    
    
}
