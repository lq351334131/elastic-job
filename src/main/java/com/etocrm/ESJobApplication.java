package com.etocrm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.etocrm.dao")
@EnableRedisHttpSession
public class ESJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESJobApplication.class, args);
    }
    
 

}
