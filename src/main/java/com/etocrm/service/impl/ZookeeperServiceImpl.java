package com.etocrm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etocrm.dao.db2.ZookeeperDao;
import com.etocrm.pojo.ZookeeperData;
import com.etocrm.service.ZookeeperService;


@Service
public class ZookeeperServiceImpl  implements ZookeeperService{
	
	@Autowired private  ZookeeperDao  zookeeperDao;
	
	@Override
	public int insert(ZookeeperData userEntity) {
		int insert = zookeeperDao.insert(userEntity);
		return insert;
	}

	@Override
	public int update(ZookeeperData zookeeperData) {
		return zookeeperDao.update(zookeeperData);
	}


}
