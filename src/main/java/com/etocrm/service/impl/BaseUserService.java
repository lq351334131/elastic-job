package com.etocrm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etocrm.dao.db2.UserEntityDao;
import com.etocrm.pojo.UserEntity;
import com.etocrm.service.UserService;
import com.etocrm.util.ConstantsUtils;

@Service
public class BaseUserService implements UserService {
	
	@Autowired
    private  UserEntityDao userMapper;

    @Override
    public boolean insert(UserEntity userEntity) {
        String username = userEntity.getUsername();
        if (exist(username)) {
            return false;
        }
        userEntity.setRoles(ConstantsUtils.ROLE_USER);
        //String password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userEntity.getPassword());
        userEntity.setPassword(userEntity.getPassword());
        int result = userMapper.insert(userEntity);
        return  result == 1;
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 判断用户是否存在
     * @param username 账号
     * @return 密码
     */
    private boolean exist(String username){
        UserEntity userEntity = userMapper.selectByUsername(username);
        return (userEntity != null);
    }

}
