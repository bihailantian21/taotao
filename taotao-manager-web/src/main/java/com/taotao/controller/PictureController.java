package com.taotao.controller;

import com.taotao.utils.FastDFSClient;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {
    /**
     * @Value("${IMAGE_SERVER_URL}")是为了注入我们在配置文件resource.properties中配置的图片访问前缀。
     * @RequestMapping("/pic/upload")指定上传文件请求的url，与下图指定url一样，
     * public Map uploadFile(MultipartFile uploadFile)
     * 参数"uploadFile"与下图的上传文件的方法参数名称是要求一样的。
     *
     *请求的url：/pic/upload
     *
     * 参数：MultiPartFileuploadFile
     *
     * 返回值：可以创建一个pojo对应返回值。可以使用map
     *
     * 业务逻辑：
     *
     * 1、接收页面传递的图片信息uploadFile
     * 2、把图片上传到图片服务器。使用封装的工具类实现。需要取文件的内容和扩展名。
     * 3、图片服务器返回图片的url
     * 4、将图片的url补充完整，返回一个完整的url。
     * 5、把返回结果封装到一个Map对象中返回。
     *
     *
     * springmvc.xml
     * 1、需要把commons-io、fileupload 的jar包添加到工程中。
     * 2、配置多媒体解析器。
     *
     *
     *
     *KindEditor的图片上传插件，对浏览器兼容性不好。
     * 使用@ResponseBody注解返回java对象，
     *
     * Content-Type:application/json;charset=UTF-8
     *
     * 返回字符串时：
     *
     * Content-Type:text/plan;charset=UTF-8
     *
     *
     */




    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping(value = "/pic/upload",produces= MediaType.TEXT_PLAIN_VALUE + ";chaeset=utf-8")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile) {


        try {
            //1.接收上传的文件
            //2.获取扩展名
            String orignalName = uploadFile.getOriginalFilename();
            String extName = orignalName.substring(orignalName.lastIndexOf(".")+1);
            //3.上传到图片服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //4.拼接返回的URL和IP地址，拼装成完整的URL
            url = IMAGE_SERVER_URL + url;
            Map result = new HashMap<>();
            result.put("error", 0);
            result.put("url", url);
            String json = JsonUtils.objectToJson(result);
            return json;

        } catch (Exception e) {
            e.printStackTrace();
            Map result = new HashMap<>();
            result.put("error",0);
            result.put("message","上传图片失败！");
            String json = JsonUtils.objectToJson(result);
            return json;
        }

    }
}
