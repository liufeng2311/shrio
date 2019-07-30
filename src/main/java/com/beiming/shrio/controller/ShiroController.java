package com.beiming.shrio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beiming.shrio.mapper.UserMapper;
import com.beiming.shrio.model.entity.User;

@RestController
@RequestMapping("/shiro")
public class ShiroController {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@PostMapping("/info")
	public String getInfo() {
		redisTemplate.opsForValue().set("liufeng1", "wangjing");
		List<User> selectAll = userMapper.selectAll();
		System.out.println(selectAll);
		return (String) redisTemplate.opsForValue().get("liufeng1");
		
	}
}
