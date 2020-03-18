package com.taylor.hadoop_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
public class indexController {
    /**
     * 测试页面
     */
    @RequestMapping("/demo")
    public ModelAndView demo(){
        ModelAndView modelAndView=new ModelAndView("/demo");
        modelAndView.addObject("name","taylor");
        return modelAndView;
    }

}
