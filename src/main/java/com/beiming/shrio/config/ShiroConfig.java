package com.beiming.shrio.config;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.beiming.shrio.shrio.ShrioFilter;
import com.beiming.shrio.shrio.ShrioRealm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

@Configuration
public class ShiroConfig {
	
    //定义用户密码的加密方式
    @Bean("matcher")
    public HashedCredentialsMatcher  hashedCredentialsMatcher () {
    	HashedCredentialsMatcher  matcher = new HashedCredentialsMatcher();
    	matcher.setHashAlgorithmName("md5");
    	matcher.setHashIterations(1);
    	return matcher;
    }
	
    //定义认证和授权
    @Bean("myRelam")
    public ShrioRealm  myShrioRealm(@Qualifier("matcher") HashedCredentialsMatcher matcher) {
    	ShrioRealm relam = new ShrioRealm();
    	relam.setCredentialsMatcher(matcher);
    	return relam;
    }
    
    //创建权限管理器
    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("myRelam") ShrioRealm relam) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(relam);
        return securityManager;
    }
    

    
    //开启验证功能并定义验证的逻辑
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        //注入自定义filter
        Map<String, Filter> filters = new HashMap<>();
        filters.put("myFilter", new ShrioFilter());  //自定义的Filter只能在此处初始化，不能当做参入传入，否则会报错
        shiroFilter.setFilters(filters);
        //定义路径对应的过滤器规则,匹配原则从上到下,找到第一个适合自己的，所以/**要配置在最后
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/shiro/**", "anon"); //该过滤器表示放行,anon == AnonymousFilter
        filterMap.put("/**", "myFilter"); //所有路径都要使用我们自定义的过滤器
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }


    
    //扫描上下文，寻找通知器
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);  //必须设置为true
        return proxyCreator;
    }

    //开启注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
