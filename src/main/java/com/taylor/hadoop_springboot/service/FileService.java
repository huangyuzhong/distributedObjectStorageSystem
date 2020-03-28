package com.taylor.hadoop_springboot.service;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
    JSONObject getPresentList(HttpServletRequest request) throws InterruptedException, IOException, URISyntaxException;
    JSONObject mkdir(HttpServletRequest request,String dirName) throws InterruptedException, IOException, URISyntaxException;
    JSONObject move(HttpServletRequest request,String fileODirName,String newPath) throws InterruptedException, IOException, URISyntaxException;
    JSONObject download(HttpServletRequest request,String fileODirName,String winPath) throws InterruptedException, IOException, URISyntaxException;
    JSONObject delete(HttpServletRequest request,String path) throws InterruptedException, IOException, URISyntaxException;
    JSONObject upload(HttpServletRequest request,String winPath) throws InterruptedException, IOException, URISyntaxException;
    JSONObject entryDir(HttpServletRequest request,String targetPath) throws InterruptedException, IOException, URISyntaxException;
    JSONObject rename(HttpServletRequest request,String oldname,String newName) throws InterruptedException, IOException, URISyntaxException;
    JSONObject entryTheDir(HttpServletRequest request,String targetPath) throws InterruptedException, IOException, URISyntaxException;
}
