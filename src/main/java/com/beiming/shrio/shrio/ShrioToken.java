package com.beiming.shrio.shrio;


import org.apache.shiro.authc.AuthenticationToken;

//自定义AuthenticationToken
public class ShrioToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	
	private String token;

    public ShrioToken(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
