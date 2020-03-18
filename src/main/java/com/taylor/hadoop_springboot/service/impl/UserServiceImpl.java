package com.taylor.hadoop_springboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taylor.hadoop_springboot.entity.User;
import com.taylor.hadoop_springboot.mapper.UserMapper;
import com.taylor.hadoop_springboot.service.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * InnoDB free: 3072 kB 服务实现类
 * </p>
 *
 * @author taylor
 * @since 2020-03-18
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    @Override
    public Integer toLogin(String username, String password) {

        EntityWrapper<User> ew=new EntityWrapper<>();
        ew.eq("username",username);
        ew.eq("password",password);
        Integer userLogin=userMapper.selectCount(ew);
        return userLogin;
    }
}
