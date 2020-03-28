package com.taylor.hadoop_springboot.service.impl;
import net.sf.json.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taylor.hadoop_springboot.entity.User;
import com.taylor.hadoop_springboot.mapper.UserMapper;
import com.taylor.hadoop_springboot.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;


import org.apache.http.HttpRequest;

import org.apache.http.HttpResponse;
import org.junit.Test;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.UUID;

/**
 * <p>
 * InnoDB free: 3072 kB 服务实现类
 * </p>
 *
 * @author taylor
 * @since 2020-03-18
 */
@Service
public class UserServiceImpl extends ServiceImpl implements UserService {
    private int deadTime=60*60*24; //cookie有效时长

    @Resource
    UserMapper userMapper;

    /**
     * 测试redis
     */
    @Test
    public void redis(){
        Jedis jedis=new Jedis("localhost");
        System.out.println("服务器正在运行"+jedis.ping());
    }


    /**
     * 检查用户名合法性
     *
     * @param username
     * @return
     */
    @Override
    public JSONObject checkUsername(String username) {
        JSONObject jsonObject = new JSONObject();
        EntityWrapper<User> ew = new EntityWrapper<>();
        ew.eq("username", username);
        Integer exist = userMapper.selectCount(ew);
        if (exist >= 1) {
            jsonObject.put("code", "002");
            jsonObject.put("msg", "用户名重复");
        } else {
            if(username==null){
                jsonObject.put("code","005");
                jsonObject.put("msg","用户名不能为空");
            }else {
                jsonObject.put("code", "001");
                jsonObject.put("msg", "用户名可用");
            }
        }
        return jsonObject;
    }


    /**
     * 保存用户信息
     * @param user
     * @return
     */
    @Override
    public JSONObject saveUser(User user) {
        JSONObject jsonObject=new JSONObject();
        JSONObject checkUsernameJson=checkUsername(user.getUsername());
        if (checkUsernameJson.get("code").equals("001")){  //信息合法
            if(userMapper.insert(user)==1){
                jsonObject.put("code","001");
                jsonObject.put("msg","注册成功");
            }else {
                jsonObject.put("code","003");
                jsonObject.put("msg","注册失败");
            }
        }else {
            jsonObject.put("code","004");
            jsonObject.put("msg","信息不合法");
        }

        return jsonObject;

    }


}
