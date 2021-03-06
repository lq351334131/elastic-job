package com.etocrm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {
	
		private  RedisTemplate  redisTemplate;
		
		@Autowired(required = false)
		public void setRedisTemplate(RedisTemplate redisTemplate) {
		    RedisSerializer stringSerializer = new StringRedisSerializer();
		    redisTemplate.setKeySerializer(stringSerializer);
		    redisTemplate.setValueSerializer(stringSerializer);
		    redisTemplate.setHashKeySerializer(stringSerializer);
		    redisTemplate.setHashValueSerializer(stringSerializer);
		    this.redisTemplate = redisTemplate;
		}

}
