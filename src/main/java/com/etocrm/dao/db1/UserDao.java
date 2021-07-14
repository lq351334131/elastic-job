package com.etocrm.dao.db1;

import com.etocrm.pojo.UserInfo;

import java.util.List;

public interface UserDao {

    List<UserInfo> findAll();

}
