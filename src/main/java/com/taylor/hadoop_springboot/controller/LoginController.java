package com.taylor.hadoop_springboot.controller;
import com.taylor.hadoop_springboot.entity.User;
import com.taylor.hadoop_springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView=new ModelAndView("/login/index");

        return modelAndView;
    }

    /**
     * 验证身份信息

     * @param session
     * @return
     */

    @RequestMapping("/login")
    public ModelAndView login(String username,String password, HttpSession session){
        ModelAndView modelAndView=new ModelAndView();
        Integer userLogin=userService.toLogin(username,password);
        if(username!=null&&password!=null&&userLogin==1){
            modelAndView.setViewName("/home/index");
        }else modelAndView.setViewName("/login/error");
        return modelAndView;
    }

}
