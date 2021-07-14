/**
 * @author xing.liu
 *
 * 2019年9月26日
 */
package com.etocrm.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebInterceptorConfig extends WebMvcConfigurerAdapter{
	 /**
		 * 注入定义好的拦截器
		 */
	    @Autowired
	    private LoginIntercepter loginIntercepter;
	    
	   
	    /**
	     * 定义拦截规则, 根据自己需要进行配置拦截和排除的接口
	     */
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(loginIntercepter)
	        // .addPathPatterns() 是配置需要拦截的路径 
	        .addPathPatterns("/**")
	        // .excludePathPatterns() 用于排除拦截
	        .excludePathPatterns("/") // 排除127.0.0.1进入登录页
	        .excludePathPatterns("/login") //排除登入接口 
	        .excludePathPatterns("/app/add-job") //排除
	        .excludePathPatterns("/api/getLog") //排除
	        .excludePathPatterns("/app/close") //排除
	        .excludePathPatterns("/js/**")
	        .excludePathPatterns("/css/**")
	        .excludePathPatterns("/images/**")
	        .excludePathPatterns("/fonts/**")
	        .excludePathPatterns("/error")
	        .excludePathPatterns("/i18n/**")
	        .excludePathPatterns("/lib/**");
	        
	    }

}
