package com.taylor.hadoop_springboot.controller;
import com.taylor.hadoop_springboot.entity.User;
import com.taylor.hadoop_springboot.service.LoginService;
import com.taylor.hadoop_springboot.service.UserService;
import net.sf.json.JSONObject;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    LoginService loginService;
    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView("/login/index");
        if(loginService.isLogin(request)){
            modelAndView.setViewName("/home/index");
        }
        return modelAndView;
    }

    /**
     * 验证登录信息
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */

    @RequestMapping("/login")
    @ResponseBody
    public JSONObject login(String username, String password, HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject=loginService.login(username,password,request,response);
        return jsonObject;
    }

    @RequestMapping("/lgout")
    public ModelAndView lgout(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView=new ModelAndView("/login/index");
        loginService.lgout(request,response);
        return modelAndView;
    }

}
