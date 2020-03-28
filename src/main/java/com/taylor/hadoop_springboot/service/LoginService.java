package com.taylor.hadoop_springboot.service;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    boolean isLogin(HttpServletRequest request);
    void lgout(HttpServletRequest request, HttpServletResponse response);
    JSONObject login(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
