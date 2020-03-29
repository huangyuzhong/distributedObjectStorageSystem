package com.taylor.hadoop_springboot.service.impl;

import com.taylor.hadoop_springboot.entity.FileEntity;
import com.taylor.hadoop_springboot.hadoop.HadoopDao;
import com.taylor.hadoop_springboot.service.FileService;
import net.sf.json.JSONObject;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private int deadTime=60*60*24; //cookie有效时长

    @Autowired
    HadoopDao hadoopDao;

    /**
     * 获取当前用户的当前目录文件列表
     * @param request
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws URISyntaxException
     */
    @Override
    public JSONObject getPresentList(HttpServletRequest request) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }
        if (hadoopDao.ifDirExist(username,path)) {
          /*  jsonObject.put("code","001");*/
            jsonObject.put("fileList", hadoopDao.getPresentFileList(username, path));
            jsonObject.put("dirList", hadoopDao.getPresentDirList(username, path));

            /*-----------------面包屑导航-----------------------*/
            List list=new ArrayList();
/*            list.add("/");*/
            if (path.length()>1){
                path=path.substring(1);
                while (path.indexOf("/")!=-1){
                    int strEndPlace=path.indexOf("/");
                    list.add(path.substring(0,strEndPlace));
                    path=path.substring(strEndPlace+1);
                }
                if (path!=null){
                    list.add(path);
                }
            }
            /*-----------------END-----------------------*/

            jsonObject.put("path",list);    //面包屑导航


        }else {
            jsonObject.put("msg","找不到请求的路径");
           /* jsonObject.put("code","009");*/
        }
        return jsonObject;
    }

    /**
     * 新建目录
     * @param request
     * @param dirName
     */
    @Override
    public JSONObject mkdir(HttpServletRequest request, String dirName) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }
        String completePath=path+dirName;

        if (hadoopDao.ifAlreadyExist(username,dirName,path)){
            jsonObject.put("msg","创建失败\n目标路径已存在");
            jsonObject.put("code","007");
            return jsonObject;
        }else {
            if(hadoopDao.mkdir(username,completePath)){
                jsonObject.put("msg","创建成功");
                jsonObject.put("code","001");
            }else {
                jsonObject.put("msg","创建失败\n未知原因");
                jsonObject.put("code","008");
            }
            return jsonObject;
        }
    }

    /**
     * 重命名
     * @param request
     * @param oldname 是名字不是目录
     * @param newName  是名字不是目录
     * @return
     */
    @Override
    public JSONObject rename(HttpServletRequest request,String oldname,String newName) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }

        if(!hadoopDao.ifAlreadyExist(username,newName,path)){
            if (hadoopDao.move(username,path+oldname,path+newName)){
                jsonObject.put("msg","操作成功");
                jsonObject.put("code","001");
            }else {
                jsonObject.put("msg","dao层move方法错误");
                jsonObject.put("code","010");
            }
        }else {
            jsonObject.put("msg","操作失败,目标路径已存在");
            jsonObject.put("code","007");
        }

        return jsonObject;
    }

    /**
     * 移动文件/文件夹
     * @param request
     * @param oldPath   是文件或文件夹名，不是路径
     * @param newPath   是目标路径
     * @return
     */
    @Override
    public JSONObject move(HttpServletRequest request,String oldPath, String newPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }
        if (hadoopDao.ifAlreadyExist(username,oldPath,newPath)){
            jsonObject.put("msg","操作失败\n目标路径已存在");
            jsonObject.put("code","007");
            return jsonObject;
        }else {
            hadoopDao.move(username,oldPath,newPath);
            jsonObject.put("msg","操作成功");
            jsonObject.put("code","001");
            return jsonObject;
        }
    }

    /**
     * 下载文件
     * @param request
     * @param winPath
     * @return
     */
    @Override
    public JSONObject download(HttpServletRequest request,String fileODirName, String winPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }

        if (hadoopDao.ifAlreadyExist(username,fileODirName,path)){
            hadoopDao.download(username,path+fileODirName,winPath);
            jsonObject.put("code","001");
            jsonObject.put("msg","已开始下载");
        }else {
            jsonObject.put("msg","找不到请求的文件");
            jsonObject.put("code","009");
        }

        return jsonObject;
    }

    /**
     * 删除文件/文件夹
     * @param request
     * @param fileODirName
     * @return
     */
    @Override
    public JSONObject delete(HttpServletRequest request, String fileODirName) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }
        if(hadoopDao.ifAlreadyExist(username,fileODirName,path)){
            if (hadoopDao.delete(username,path+fileODirName)){
                jsonObject.put("code","001");
                jsonObject.put("msg","已删除");
            }else {
                jsonObject.put("code","010");
                jsonObject.put("msg","未知原因，删除失败");
            }
        }else {
            jsonObject.put("msg","找不到请求的文件");
            jsonObject.put("code","009");
        }
        return jsonObject;
    }

    /**
     * 上传文件
     * @param request
     * @param winPath   客户机的文件路径
     * @return
     */
    @Override
    public JSONObject upload(HttpServletRequest request, String winPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }

        String fileName=winPath.substring(winPath.lastIndexOf("/"));
        if (hadoopDao.ifAlreadyExist(username,fileName,path)){
            jsonObject.put("msg","操作失败\n目标路径已存在");
            jsonObject.put("code","007");
            return jsonObject;
        }else {
            hadoopDao.upload(username,winPath,path);
            jsonObject.put("msg","正在上传");
            jsonObject.put("code","001");
        }

        return jsonObject;
    }

    /**
     * 打开下一级目录
     * @param request
     * @param targetPath   dir's name
     * @return
     */
    @Override
    public JSONObject entryDir(HttpServletRequest request, String targetPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        String path=jedis.get(username+"Path");
        if (!path.equals("/")){
            path=path+"/";
        }

        List<FileEntity> list=hadoopDao.getPresentDirList(username,path);
        for (FileEntity fileEntity:list){
            if (fileEntity.getName().equals(targetPath)){
                jedis.setex(username+"Path",deadTime,path+targetPath+"/");
                jsonObject.put("code","001");
                return jsonObject;
            }
        }
        jsonObject.put("msg","找不到请求的路径");
        jsonObject.put("code","009");

        return jsonObject;
    }

    /**
     * 进入指定目录
     * @param request
     * @param targetPath
     * @return
     */
    @Override
    public JSONObject entryTheDir(HttpServletRequest request,String targetPath) throws InterruptedException, IOException, URISyntaxException {
        JSONObject jsonObject=new JSONObject();
        Jedis jedis=new Jedis();
        Cookie[] cookies=request.getCookies();
        String username=jedis.get(cookies[0].getValue());
        if (hadoopDao.ifDirExist(username,targetPath)){
            jedis.setex(username+"Path",deadTime,targetPath);
            /*            被弃用的面包屑（由getPresentList代替）                  */
/*            List list=new ArrayList();
            list.add("/");
            if (targetPath.length()>1){
                targetPath=targetPath.substring(1);
                while (targetPath.indexOf("/")!=-1){
                    int strEndPlace=targetPath.indexOf("/");
                    list.add(targetPath.substring(0,strEndPlace));
                    targetPath=targetPath.substring(strEndPlace+1);
                }
                if (targetPath!=null){
                    list.add(targetPath);
                }
            }

            jsonObject.put("path",list);    //面包屑导航*/
            jsonObject.put("code","001");
            return jsonObject;
        }else {
            jsonObject.put("msg","找不到请求的路径");
            jsonObject.put("code","009");
        }
        return jsonObject;
    }

    /**
     * 合并两个json信息
     * @param msgJson
     * @param pathNListJson
     * @return
     */
    JSONObject copyJson(JSONObject msgJson,JSONObject pathNListJson){
        pathNListJson.put("msg",msgJson.get("msg"));
        return pathNListJson;
    }

}
