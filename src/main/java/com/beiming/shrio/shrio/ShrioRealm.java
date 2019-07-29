package com.beiming.shrio.shrio;


import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beiming.shrio.mapper.UserMapper;
import com.beiming.shrio.model.entity.User;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义认证和授权过程 
 *
 */
@Component
public class ShrioRealm extends AuthorizingRealm {

	@Autowired
	private UserMapper userMapper;
	
	//token自定义时，必须重写此方法，该方法表示我们的relam是否支持该token，默认支持UsernamePasswordToken，我们的系统使用ShrioToken
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof ShrioToken;
	}


	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// String string = principals.getPrimaryPrincipal().toString();

		//用户权限列表
		Set<String> permsSet = new HashSet<String>();

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		User user = userMapper.getUserByUsername(username);
		if(user == null) {
			throw new IncorrectCredentialsException("登录已过期，请重新登录");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, user.getPassword(), getName()); //getName获取的是relam的名字
		info.setCredentialsSalt(ByteSource.Util.bytes("liufeng"));//为密码加将盐，实际项目应使用随机数，生成密码时将salt存入数据库，此处从数据库查出该用户的salt
		return info;
	}
}
