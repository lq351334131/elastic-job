package com.etocrm.dao.db2;

import org.apache.ibatis.annotations.Param;

import com.etocrm.pojo.UserEntity;

public interface UserEntityDao{
	
    int insert(UserEntity userEntity);

    UserEntity selectByUsername(@Param("username") String username);
    

}
