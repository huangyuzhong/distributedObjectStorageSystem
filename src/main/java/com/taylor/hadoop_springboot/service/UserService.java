package com.taylor.hadoop_springboot.service;

import com.taylor.hadoop_springboot.entity.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * InnoDB free: 3072 kB 服务类
 * </p>
 *
 * @author taylor
 * @since 2020-03-18
 */
public interface UserService  {
    Integer toLogin(String username,String password);
}
