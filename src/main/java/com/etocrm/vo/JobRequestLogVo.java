package com.etocrm.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class JobRequestLogVo implements  Serializable {
	
	private static final long serialVersionUID = 1L;

    private  int per_page;
    
    private  int page;
    
    private  String sort;
    
    private  String order;
    
    
    private   String   jobName;


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


	public String getJobName() {
		return jobName;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
    
    
    
    
    
    
    
    

}
