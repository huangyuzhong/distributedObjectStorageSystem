package com.taylor.hadoop_springboot.hadoop;

import com.taylor.hadoop_springboot.entity.FileEntity;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


@Repository
public class HadoopDaoDemo {
    public String url = "hdfs://hadoop01:9000";
    Configuration con = null;
    FileSystem fileSystem = null;

    @Before
    public void qz() throws Exception {
        con = new Configuration();
        con.set("fs.defaultFS","hdfs://192.168.5.112:9000");
        fileSystem = FileSystem.get(new URI(url), con, "root");
        System.out.println("前置操作");
    }

    @After
    public void hz() throws Exception {
        System.out.println("后置操作");
        fileSystem.close();
        con.clear();
    }

    /**
     * 读取hadoop文件
     *
     * @throws Exception
     */
    @Test
    public void read() throws Exception {
        FSDataInputStream in = fileSystem.open(new Path("/2.txt"));
        IOUtils.copyBytes(in, System.out, 1024);
    }

    /**
     * 写入hadoop文件
     *
     * @throws Exception
     */
    @Test
    public void write() throws Exception {
        FileSystem fileSystem = FileSystem.get(new URI(url), con, "root");
        FSDataOutputStream out = fileSystem.create(new Path("/2.txt"));
        out.writeUTF("hello world222222");
        out.flush();
    }

    /**
     * 创建hadoop文件
     */
    @Test
    public void mkdir() throws Exception {
        fileSystem.mkdirs(new Path("/test4d"));
    }

    /**
     * 重命名（剪切）
     *
     * @throws Exception
     */
    @Test
    public void rename() throws Exception {
        Path oldPath = new Path("/2.txt");
        Path newPath = new Path("/b.txt");
        fileSystem.rename(oldPath, newPath);
    }

    /**
     * 拷贝文件(从linux/windows 下上传文件到hadoop)
     */
    @Test
    public void upload() throws Exception {

        Path originPath = new Path("F:\\photo\\匿名头像.png");
        Path targetPath = new Path("/");
        fileSystem.copyFromLocalFile(originPath, targetPath);

    }

    /**
     * 从hadoop服务器上下载文件到本地
     *
     * @throws Exception
     */
    @Test
    public void down() throws Exception {
        Path originPath = new Path("/hello.txt");
        Path targetPath = new Path("d:/ywz");
        fileSystem.copyToLocalFile(false, originPath, targetPath, true);
    }

    /**
     * 获取文件列表
     *
     * @throws Exception
     */
/*    @Test
    public void list() throws Exception {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));
        List<FileEntity> list=new ArrayList<>();
        int i=1;
        for (FileStatus file : fileStatuses) {
            System.out.println(file.getPath() + "---" + file.getLen());
            FileEntity fileEntity=new FileEntity();

            fileEntity.setId(i);
            fileEntity.setPath(file.getPath().toString());
            fileEntity.setLen(file.getLen());
            i++;
        }
    }*/


    /**
     * 获取到所有的文件列表
     *
     * @throws Exception
     */
    @Test
    public void allList() throws Exception {

        RemoteIterator<LocatedFileStatus> re = fileSystem.listFiles(new Path("/"), true);
        while (re.hasNext()) {
            LocatedFileStatus locatedFileStatus = re.next();
            System.out.println(locatedFileStatus.getPath());
        }
    }

    /**
     * 测试获取和更改所有者
     *
     */
    @Test
    public void ownerOp() throws IOException {
        RemoteIterator<LocatedFileStatus> re=fileSystem.listFiles(new Path("/"),true);
        while (re.hasNext()){
            LocatedFileStatus locatedFileStatus=re.next();
            if (locatedFileStatus.getPath().getName().equals("timg.jpg")){
                System.out.println("owner:"+locatedFileStatus.getOwner());
                System.out.println("hashcode:"+locatedFileStatus.hashCode());
                System.out.println("permission:"+locatedFileStatus.getPermission());
                Path path=new Path("/timg.jpg");
                FsPermission permission=new FsPermission("770");
                fileSystem.setPermission(path,permission);
                System.out.println("permission:"+locatedFileStatus.getPermission());
                
            }


        }
    }
}

