package com.etocrm.util;

import java.util.Collection;
import java.util.List;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class SortOrOrderUtils {
	public static final List<String> FIELDS_JOB_EXECUTION_LOG = 
	            Lists.newArrayList("id", "hostname", "ip", "task_id", "job_name", "execution_source", "sharding_item", "start_time", "complete_time", "is_success", "failure_cause");
	    
	
	public  static  String buildOrder( final Collection<String> tableFields,final String sortName, final String sortOrder) {
	        if (Strings.isNullOrEmpty(sortName)) {
	            return "";
	        }
	        String lowerUnderscore = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
	        if (!tableFields.contains(lowerUnderscore)) {
	            return "";
	        }
	        StringBuilder sqlBuilder = new StringBuilder();
	        sqlBuilder.append(" ORDER BY ").append(lowerUnderscore);
	        switch (sortOrder.toUpperCase()) {
	            case "ASC":
	                sqlBuilder.append(" ASC");
	                break;
	            case "DESC":
	                sqlBuilder.append(" DESC");
	                break;
	            default :
	                sqlBuilder.append(" ASC");
	        }
	        return sqlBuilder.toString();
	    }

}
