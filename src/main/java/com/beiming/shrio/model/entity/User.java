package com.beiming.shrio.model.entity;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "user")
public class User {

	@Id
	private String id;   //ID
	
	private String username;   //用户名
	
	private String password;   //密码
	
	private String version;    //版本
	
	private Date updatetime;   //更新时间
}
