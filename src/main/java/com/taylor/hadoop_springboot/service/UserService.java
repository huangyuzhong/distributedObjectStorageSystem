package com.taylor.hadoop_springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.taylor.hadoop_springboot.entity.User;
import net.sf.json.JSONObject;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * InnoDB free: 3072 kB 服务类
 * </p>
 *
 * @author taylor
 * @since 2020-03-18
 */
public interface UserService extends IService {

    JSONObject checkUsername(String username);

    JSONObject saveUser(User user);
}
