package com.beiming.shrio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	@PostMapping("/info")
	public String getInfo() {
		List<User> selectAll = userMapper.selectAll();
		System.out.println(selectAll);
		return "123";
		
	}
}
