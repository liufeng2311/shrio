package com.beiming.shrio.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.beiming.shrio.model.entity.User;

import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{

	User getUserByUsername(String username);
}
