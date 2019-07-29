package com.beiming.shrio.shrio;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

/**
 * oauth2过滤器
 *
 */
public class ShrioFilter extends AuthenticatingFilter {

	//重写token方法，使用自定义token----->ShrioToken
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        String token = getRequestToken((HttpServletRequest) request);
        if(token == null){
            return null;
        }
        return new ShrioToken(token);
    }

    //过滤器是否放行的条件为isAccessAllowed()||onAccessDenied(),我们可以直接使第一个方法返回false，在第二个方法里写我们的token逻辑
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    //在该方法验证是否携带token,若没有，则不放行，返回错误信息
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = getRequestToken((HttpServletRequest) request);
        if(token == null){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin","");
            JSONObject object = new JSONObject();
            object.put("data", null);
            object.put("code", "401");
            object.put("message", "token is null");
            httpResponse.getWriter().print(object);
            return false;
        }
        return executeLogin(request, response);  //如果token不为空，执行shrio的登录方法，如果成功，则通过该过滤器，否则的话执行onLoginFailure方法
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "");
        JSONObject object = new JSONObject();
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            object.put("data", null);
            object.put("code", "402");
            object.put("message", throwable.getMessage());
            httpResponse.getWriter().print(object);
        } catch (IOException e1) {
        	e1.printStackTrace();
        }

        return false;
    }        

    //查看请求中是否携带token
    private String getRequestToken(HttpServletRequest httpRequest){
        String token = httpRequest.getHeader("Authorization");
        if(token == null){
            token = httpRequest.getParameter("token");
        }
        return token;
    }


}
