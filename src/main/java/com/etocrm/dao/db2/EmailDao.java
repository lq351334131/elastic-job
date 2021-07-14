package com.etocrm.dao.db2;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface EmailDao {
	
	@Select("select  email  'email' ,pwd 'pwd'  from  email  where  id=#{id}")
	Map<String, String> getId(@Param("id") Integer id);

}
