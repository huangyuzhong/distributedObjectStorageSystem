package com.taylor.hadoop_springboot.controller;

import com.taylor.hadoop_springboot.service.FileService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    FileService fileService;

    /**
     * 跳转到网盘首页
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) throws InterruptedException, IOException, URISyntaxException {
        ModelAndView modelAndView=new ModelAndView("/home/index");
        fileService.entryTheDir(request,"/");
        JSONObject jsonObject=fileService.getPresentList(request);
        modelAndView.addObject("json",jsonObject);
        return modelAndView;
    }

}
