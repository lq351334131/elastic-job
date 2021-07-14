package com.etocrm.vo;

import java.text.ParseException;
import java.util.Date;

import com.etocrm.util.ConstantsUtils;

public class HistroyTraceVo {
	private  int per_page;
    
    private  int page;
    
    private  String sort;
    
    private  String order;
    
    private  Date startTime;
    
    private  Date endTime;
    
    private  String  ip;
    
    private   String   jobName;
    
    private Date completeTime;
    
    private Integer isSuccess;
    
    
    private  String   oredrbySql;

	public int getPerPage() {
		return per_page;
	}

	public int getPage() {
		return page;
	}

	public String getSort() {
		return sort;
	}

	public String getOrder() {
		return order;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public int getPer_page() {
		return per_page;
	}

	public void setPer_page(int per_page) {
		this.per_page = per_page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	

	/**
	 * 
	 */
	public HistroyTraceVo() {
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getOredrbySql() {
		return oredrbySql;
	}

	public void setOredrbySql(String oredrbySql) {
		this.oredrbySql = oredrbySql;
	}
	
	
	
	
	

	/*public void setStartTime(String startTime) {
		Date result;
		try {
			result = ConstantsUtils.simpleDateFormat.parse(startTime);
			this.startTime = result;
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void setEndTime(String  endTime) {
		Date result;
		try {
			result = ConstantsUtils.simpleDateFormat.parse(endTime);
			this.endTime = result;
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}*/
	
    
    
	
	
	
	
	
	

}
