package com.taylor.hadoop_springboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taylor.hadoop_springboot.entity.User;
import com.taylor.hadoop_springboot.mapper.UserMapper;
import com.taylor.hadoop_springboot.service.LoginService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    private int deadTime=60*60*24; //cookie有效时长
    /**
     * 验证是否已登录
     * @param
     * @return
     */
    @Override
    public boolean isLogin(HttpServletRequest request) {
        Cookie[] cookie=request.getCookies();
        String token;
        if(cookie!=null){
            token=cookie[0].getValue();
            Jedis jedis=new Jedis("127.0.0.1");
            if(jedis.get(token)!=null){
                return true;
            }
        }
        return false;
    }

    /**
     * 退出登录
     * @param request
     * @param response
     */
    @Override
    public void lgout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies=request.getCookies();
        String token=cookies[0].getValue();
        //销毁redis
        Jedis jedis=new Jedis("127.0.0.1");
        jedis.del(token);
        //销毁cookie(0代表立即销毁（放屁！----虽然没有用，但是保留)
        cookies[0].setMaxAge(0);
        response.addCookie(cookies[0]);
    }

    @Resource
    UserMapper userMapper;


    /**
     * 登录验证
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public JSONObject login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject=new JSONObject();
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.eq("username", username);
        ew.eq("password", password);
        if (userMapper.selectCount(ew)>=1){
            jsonObject.put("code","001");
            jsonObject.put("msg","登录成功");
            //身份验证通过
            //建立cookie
            String token= UUID.randomUUID().toString()+username;
            //记录用户名的cookie
            Cookie cookie=new Cookie("user",token);
            cookie.setMaxAge(deadTime);
            cookie.setPath("/");
            response.addCookie(cookie);
            //记录用户当前所在路径的cookie
//            Cookie cookie1=new Cookie(username+"Path","/");
//            cookie1.setMaxAge(deadTime);
//            cookie1.setPath("/");
//            response.addCookie(cookie1);
            //保存token,username,path
            Jedis jedis=new Jedis("127.0.0.1");
            jedis.setex(token,deadTime,username);
            jedis.setex(username+"Path",deadTime,"/");

        }else {
            jsonObject.put("code","006");
            jsonObject.put("msg","用户名或密码错");
        }

        return jsonObject;
    }


}
