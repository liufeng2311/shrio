package com.beiming.shrio.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
@SuppressWarnings("unused")
public class RedisCache<K,V> implements Cache<K, V>{

	@Autowired
	private RedisTemplate<K, V> redisTemplate;

	private final String SHRIO_PREFIX = "shrio"; 
	
	@Override
	public V get(K key) throws CacheException {
		System.out.println("============从缓存中查询==================");
		V v = redisTemplate.opsForValue().get(key);
		return v;
	}

	@Override
	public V put(K key, V value) throws CacheException {
		System.out.println("============往缓存中插值==================");
		redisTemplate.opsForValue().set(key, value);
		return value;
	}

	@Override
	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public V remove(K arg0) throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

}
