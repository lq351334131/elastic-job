package com.etocrm.vo;

import java.util.List;


public class ResultVo<T> {
	 private  Integer total;
     
     private  List<T> rows;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	
	
     
     
     
     
     
     
     

}
