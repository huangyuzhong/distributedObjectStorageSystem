package com.taylor.hadoop_springboot.hadoop;

import com.taylor.hadoop_springboot.entity.FileEntity;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class HadoopDao {
    public String url = "hdfs://hadoop01:9000";

    /**
     * 获取指定用户所有文件列表（不是根目录列表）
     *
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public void getAllList(String username) throws IOException, URISyntaxException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        RemoteIterator<LocatedFileStatus> re = fileSystem.listFiles(new Path("/"), true);

        System.out.println("----------------------------------");
        while (re.hasNext()) {
            LocatedFileStatus locatedFileStatus = re.next();
            System.out.println(locatedFileStatus.getPath());
        }
        System.out.println("----------------------------------");
        fileSystem.close();
        con.clear();
    }


    /**
     * 获取指定用户当前目录文件列表，是File
     *
     * @param username
     * @param path
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public List<FileEntity> getPresentFileList(String username, String path) throws URISyntaxException, IOException, InterruptedException {
        List<FileEntity> fileList = new ArrayList<>();
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        RemoteIterator<LocatedFileStatus> re = fileSystem.listFiles(new Path(path), true);

        //只打印文件，不打印文件夹
        System.out.println("----------------------------------");
        while (re.hasNext()) {
            LocatedFileStatus locatedFileStatus = re.next();
            System.out.println("name:" + locatedFileStatus.getPath().getName());
            System.out.println("path:" + locatedFileStatus.getPath());
            System.out.println("len:" + locatedFileStatus.getLen());
            System.out.println("owner:" + locatedFileStatus.getOwner());

        }
        System.out.println("----------------------------------");

        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(path));
        for (FileStatus file : fileStatuses) {
            FileEntity fileEntity = new FileEntity();
            if (file.getOwner().equals(username)) {
                if (file.isFile()) {
                    fileEntity.setName(file.getPath().getName());
                    fileEntity.setLen(file.getLen());
                    fileEntity.setTime(new Date(file.getAccessTime()).toString());
                    fileList.add(fileEntity);
                }
            }

        }
        fileSystem.close();
        con.clear();
        return fileList;
    }

    /**
     * 获取指定用户当前目录文件夹列表，是Dir
     *
     * @param username
     * @param path
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public List<FileEntity> getPresentDirList(String username, String path) throws URISyntaxException, IOException, InterruptedException {
        List<FileEntity> dirList = new ArrayList<>();
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        RemoteIterator<LocatedFileStatus> re = fileSystem.listFiles(new Path(path), true);

        //只打印文件，不打印文件夹
        System.out.println("----------------------------------");
        while (re.hasNext()) {
            LocatedFileStatus locatedFileStatus = re.next();
            System.out.println("name:" + locatedFileStatus.getPath().getName());
            System.out.println("path:" + locatedFileStatus.getPath());
            System.out.println("len:" + locatedFileStatus.getLen());
            System.out.println("owner:" + locatedFileStatus.getOwner());

        }
        System.out.println("----------------------------------");

        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(path));
        for (FileStatus file : fileStatuses) {
            FileEntity fileEntity = new FileEntity();
            if (file.getOwner().equals(username)) {
                if (!file.isFile()) {
                    fileEntity.setName(file.getPath().getName());
                    fileEntity.setTime(new Date(file.getAccessTime()).toString());
                    dirList.add(fileEntity);
                }
            }

        }
        fileSystem.close();
        con.clear();
        return dirList;
    }

    /**
     * 查目标文件或文件夹是否已存在   true代表存在
     *
     * @param username
     * @param fileODirName 要查重的文件/文件夹
     * @param path         要查重的目录
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean ifAlreadyExist(String username, String fileODirName, String path) throws URISyntaxException, IOException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
/*      旧的查重方法，可以绝对保证查重正确性
 FileStatus[] fileStatuses = fileSystem.listStatus(new Path(path));
        for (FileStatus file : fileStatuses) {
            if (file.getOwner().equals(username)) {
                if (file.getPath().getName().equals(fileODirName)) {
                    return true;
                }
            }
        }*/
        boolean ifExist;
        if (fileSystem.exists(new Path(path + fileODirName))) {
            ifExist = true;
        } else {
            ifExist = false;
        }
        fileSystem.close();
        con.clear();
        return ifExist;
    }

    /**
     * 创建文件夹
     *
     * @param username
     * @param completePath
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean mkdir(String username, String completePath) throws URISyntaxException, IOException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        boolean mkdirSuccess = fileSystem.mkdirs(new Path(completePath));
        fileSystem.close();
        con.clear();
        return mkdirSuccess;
    }

    /**
     * 重命名&移动
     *
     * @param username
     * @param oldPath
     * @param newPath
     * @return
     */
    public boolean move(String username, String oldPath, String newPath) throws IOException, URISyntaxException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        Path oldName = new Path(oldPath);
        Path newName = new Path(newPath);
        boolean mvSuccess = fileSystem.rename(oldName, newName);
        fileSystem.close();
        con.clear();
        return mvSuccess;
    }

    /**
     * 下载文件
     *
     * @param username
     * @param hadoopPath
     * @param localPath
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public void download(String username, String hadoopPath, String localPath) throws URISyntaxException, IOException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        Path cloudPath = new Path(hadoopPath);
        Path winPath = new Path(localPath);
        fileSystem.copyToLocalFile(false, cloudPath, winPath, true);
        fileSystem.close();
        con.clear();
    }

    /**
     * 删除文件
     *
     * @param username
     * @param path
     */
    public boolean delete(String username, String path) throws URISyntaxException, IOException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        Path delPath = new Path(path);
        boolean ifDeleted = fileSystem.delete(delPath, true);
        fileSystem.close();
        con.clear();
        return ifDeleted;
    }

    /**
     * 上传
     *
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public void upload(String username, String localFilePath, String cloudPath) throws URISyntaxException, IOException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);

        Path winPath = new Path(localFilePath);
        Path hadoopPath = new Path(cloudPath);
        fileSystem.copyFromLocalFile(winPath, hadoopPath);

        fileSystem.close();
        con.clear();
    }

    /**
     * 进入目录(dao层只需要判断路径是否存在即可)
     *
     * @param username
     * @param path
     */
    public boolean ifDirExist(String username, String path) throws URISyntaxException, IOException, InterruptedException {
        Configuration con = new Configuration();
        con.set("fs.defaultFS", url);
        FileSystem fileSystem = FileSystem.get(new URI(url), con, username);
        Path path1 = new Path(path);
        boolean existDir = fileSystem.exists(path1);
        fileSystem.close();
        con.clear();
        return existDir;
    }

}
