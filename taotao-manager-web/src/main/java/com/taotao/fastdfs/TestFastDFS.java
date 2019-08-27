package com.taotao.fastdfs;
import com.taotao.utils.FastDFSClient;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;


public class TestFastDFS {
    /**
     * 1、加载配置文件，配置文件中的内容就是tracker服务的地址。
     * 配置文件内容：tracker_server=192.168.25.133:22122
     * 2、创建一个TrackerClient对象。直接new一个。
     * 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
     * 4、创建一个StorageServer的引用，值为null
     * 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
     * 6、使用StorageClient对象上传图片。
     * 7、返回数组。包含组名和图片的路径。
     * @throws Exception
     */
    @Test
    public void testUploadFile() throws Exception{
        //1.向工程添加jar包
        //2.创建一个配置文件，配置tracker服务器地址
        //3.加载配置文件
        ClientGlobal.init("/Users/zhangcongrong/IdeaProjects/taotao/taotao-manager-web/src/main/resources/resource/client.conf");
        //4.创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //5.使用TrackerClient对象获得trackerserver对象。
        TrackerServer trackerServer = trackerClient.getConnection();
        //6.创建一个StorageServer的引用，我们用null就可以
        StorageServer storageServer = null;
        //7.创建一个StorageClient对象。trackerserver、StorageServer两个参数
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //8.使用StorageClient对象上传文件
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("fileName", "2");
        metaList[1] = new NameValuePair("createTime", "2019-08-27 16:01:00");
        metaList[2] = new NameValuePair("createUser", "zhangcongrong");
        String[] upload_files = storageClient.upload_file("/Users/zhangcongrong/Documents/WechatIMG53.jpeg", "jpeg", metaList);
        for(String file : upload_files){
            System.out.println(file);
        }
    }


    /**
     * 我们新建一个工具包com.taotao.utils，然后把我们的封装类FastDFSClient.java放到下面。我们来测测这个工具类是否好使，我们再新建个测试方法testFastDFSClient
     * @throws Exception
     */
    @Test
    public void testFastDFSClient() throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient("/Users/zhangcongrong/IdeaProjects/taotao/taotao-manager-web/src/main/resources/resource/client.conf");
        String imgPath = fastDFSClient.uploadFile("/Users/zhangcongrong/Documents/WechatIMG40.jpeg");
        System.out.println(imgPath);
    }
}