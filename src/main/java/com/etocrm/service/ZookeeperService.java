package com.etocrm.service;

import com.etocrm.pojo.ZookeeperData;

public interface ZookeeperService {
    int insert(ZookeeperData userEntity);
    
    int  update(ZookeeperData zookeeperData);


}
