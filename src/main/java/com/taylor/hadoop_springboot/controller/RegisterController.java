package com.taylor.hadoop_springboot.controller;

import com.taylor.hadoop_springboot.entity.User;
import com.taylor.hadoop_springboot.service.UserService;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/register")
public class RegisterController {

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView=new ModelAndView("register/index");
        return modelAndView;
    }

    /**
     * 检查用户名合法性
     * @param username
     * @return
     */
    @Autowired
    private UserService userService;

    @RequestMapping("/checkUsername")
    @ResponseBody
    public JSONObject checkUsername(@RequestParam(value = "username")String username){
        JSONObject jsonObject=userService.checkUsername(username);
        return jsonObject;
    }

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public JSONObject saveUser(User user){
        JSONObject jsonObject=userService.saveUser(user);

       /*
       String saveRes=userService.saveUser(user);
        if(saveRes.equals("注册成功")){
            modelAndView.addObject("msg","注册成功");
            modelAndView.setViewName("/login/index");
        }else {
            modelAndView.addObject("msg",saveRes);
            modelAndView.setViewName("/register/index");
        }
        */

        return jsonObject;
    }
}
