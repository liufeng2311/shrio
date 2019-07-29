package com.beiming.practice;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

import com.beiming.shrio.shrio.ShrioRealm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShrioPractice {

	
	SimpleAccountRealm realm = new SimpleAccountRealm();
	IniRealm inirealm = new IniRealm("classpath:ini/user.ini");
	ShrioRealm shrioRealm = new ShrioRealm();
	
//	@Before
//	public void addUser() {
//		realm.addAccount("liufeng", "123456","admin");	
//	}
	
	//认证
	@Test
	public void testAuthentication() {
		//1.构建SecurityManager
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setRealm(shrioRealm);
		//设置加密算法
		HashedCredentialsMatcher matcher = new  HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("md5");
		matcher.setHashIterations(1);
		shrioRealm.setCredentialsMatcher(matcher);
		//2.获取登陆主题Subject
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		//登录
		UsernamePasswordToken token  = new UsernamePasswordToken("liufeng", "123456");
		subject.login(token);
		//查看是否认证
		log.info("isAuthenticated = {} ", subject.isAuthenticated());
		subject.logout();
		log.info("isAuthenticated ==== {} ", subject.isAuthenticated());
	}
	
	
	//授权
	@Test
	public void testAuthorization() {
		//1.构建SecurityManager
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setRealm(shrioRealm);
		//2.获取登陆主题Subject
		SecurityUtils.setSecurityManager(securityManager);
		Subject subject = SecurityUtils.getSubject();
		//登录
		UsernamePasswordToken token  = new UsernamePasswordToken("liufeng", "123456");
		subject.login(token);
		//查看是否认证
		log.info("hasRole = {}", subject.hasRole("admin"));
		List<String> roles = new ArrayList<>() ;
		roles.add("admin");
		roles.add("user");
		//返回集合分别判断
		log.info("hasRole = {}", subject.hasRoles(roles));
		//同时具有这么多角色时才返回true
		log.info("hasRole = {}", subject.hasAllRoles(roles));
		
	}
}
