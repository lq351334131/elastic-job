package com.etocrm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etocrm.pojo.Result;
import com.etocrm.pojo.ResultGenerator;
import com.etocrm.pojo.UserEntity;
import com.etocrm.service.UserService;

@Controller
public class HomeController {

    @Autowired private UserService userService;
    
    @Autowired
	private  RedisTemplate  redisTemplate;
    
    
    @GetMapping({"/", "/login"})
    public String login(HttpSession session){
        return "login";
    }


    @PostMapping("/login")
	public String  getinfo(@RequestParam String username,@RequestParam String password,HttpServletRequest  request,Model model) {
    	if(StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
    		model.addAttribute("error", "用户不能为空");
    		return "login";
		}
    	UserEntity user = userService.getByUsername(username);
		if(user==null) {
			model.addAttribute("error", "用户不存在");
			return "login";
		}else {
			if(!password.equals(user.getPassword())) {
				model.addAttribute("error", "密码错误");
				return "login";
			}
			request.getSession().setAttribute("session", user);
		}
        return "redirect:index";
    }
    
    @GetMapping("/index")
    public   String    index( Model model) {
    	return "index"; 
    }


    @PostMapping("/register")
    @ResponseBody
    public String doRegister(UserEntity userEntity){
        if (userService.insert(userEntity))
            return "success";
        return "error";
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session){
		if(session != null){
    		session.invalidate();
		}		return "login"; 
     }
    
   
}


