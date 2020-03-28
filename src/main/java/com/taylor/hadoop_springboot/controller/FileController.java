package com.taylor.hadoop_springboot.controller;

import com.taylor.hadoop_springboot.entity.FileEntity;
import com.taylor.hadoop_springboot.service.FileService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/file")
public class FileController {

    /**
     * 获取当前文件列表
     * @param session
     * @return
     */
    @Autowired
    FileService fileService;

    @RequestMapping("/getPresentFileList")
    @ResponseBody
    public JSONObject getPresentFileList(HttpServletRequest request) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=fileService.getPresentList(request);
        return jsonObject;
    }

    /**
     * 新建文件夹
     * @param request
     * @param dirName
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws URISyntaxException
     */
    @RequestMapping("/mkdir")
    @ResponseBody
    public JSONObject mkdir(HttpServletRequest request,String dirName) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=fileService.mkdir(request,dirName);
        return jsonObject;
    }

    /**
     * 重命名
     * @param request 获取目录
     * @param oldName 旧名字，不是目录
     * @param newName 新名字，不是目录
     * @return
     */
    @RequestMapping("/rename")
    public ModelAndView rename(HttpServletRequest request, String oldName, String newName) throws InterruptedException, IOException, URISyntaxException {
        ModelAndView modelAndView=new ModelAndView("/home/fileOp");
        JSONObject jsonObject=fileService.rename(request,oldName,newName);
        modelAndView.addObject("json",jsonObject);
        return modelAndView;
    }

    /**
     * 删除
     * @param request
     * @param delPath
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(HttpServletRequest request,String delPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=fileService.delete(request,delPath);

        return jsonObject;
    }

    /**
     * 进入下一级目录
     * @param request
     * @param dirPath
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws URISyntaxException
     */
    @RequestMapping("/openDir")
    @ResponseBody
    public JSONObject openDir(HttpServletRequest request,String dirPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=fileService.entryDir(request,dirPath);
        return jsonObject;
    }

    /**
     * 进入指定目录
     * @param request
     * @param targetPath   为在html里拼接好的完整目录:eg. /123/fduia/
     * @return
     */
    @RequestMapping("/openTheDir")
    @ResponseBody
    public JSONObject openTheDir(HttpServletRequest request,String targetPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=fileService.entryTheDir(request,targetPath);
        return jsonObject;
    }

}
