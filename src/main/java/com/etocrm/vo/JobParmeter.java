package com.etocrm.vo;

import java.io.Serializable;

public class JobParmeter implements  Serializable {
	

  
	private static final long serialVersionUID = 1L;


   private  String  type;
	
	
	private  String  url;
	
	
	private   String  parameter;
	

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getParameter() {
		return parameter;
	}


	public void setParameter(String parameter) {
		this.parameter = parameter;
	}


	
	
	

}
