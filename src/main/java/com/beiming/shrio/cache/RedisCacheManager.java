package com.beiming.shrio.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"rawtypes","unchecked"})
public class RedisCacheManager implements CacheManager{

	@Autowired
	private RedisCache redisCache;
	
	@Override
	public <K, V> Cache<K, V> getCache(String arg0) throws CacheException {
		return redisCache;
	}

}
