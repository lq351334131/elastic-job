package com.etocrm.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.etocrm.pojo.UserEntity;
import com.etocrm.util.TimeUtil;

@Component
public class LoginIntercepter implements HandlerInterceptor {
	
	
	private final Log log=LogFactory.getLog(getClass());
	
	
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
        // 从session中获取用户信息
		UserEntity userEntity =(UserEntity)  session.getAttribute("session");
        String url1 = request.getRequestURI();
        if(url1.contains("/api/add-job") || url1.contains("/api/getLog")|| url1.contains("/api/close")  ) {
        	log.info(TimeUtil.getDateTme("yyyy-MM-dd HH:mm:ss")+"直接访问  "+url1);
        	return true;
        }
        if(userEntity == null){
        	log.info(url1+"    当前时间："+TimeUtil.getDateTme("yyyy-MM-dd HH:mm:ss")+"==session过期==");
			response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }else{
        	log.info("==用户："+userEntity.getUsername()+"于"+TimeUtil.getDateTme("yyyy-MM-dd HH:mm:ss")+"  访问：  "+url1);
        	return true;
        }
    }
    
    public   void  get(HttpServletResponse response) throws IOException {
    	 PrintWriter printWriter = response.getWriter();
    	 String error  ="用户未登录，接口访问失败";
         String body = "{\"status\":\"failure\",\"msg\":" +error+ "}";
         printWriter.write(body);
         printWriter.flush();

    }
}
