package com.taotao.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HtmlGenController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    /**
     * 请求的url：/genhtml
     * 参数：无
     * 返回值：ok （String， 需要使用@ResponseBody）
     * 业务逻辑：
     * 1、从spring容器中获得FreeMarkerConfigurer对象。
     * 2、从FreeMarkerConfigurer对象中获得Configuration对象。
     * 3、使用Configuration对象获得Template对象。
     * 4、创建数据集
     * 5、创建输出文件的Writer对象。
     * 6、调用模板对象的process方法，生成文件。
     * 7、关闭流。
     * @return
     * @throws Exception
     */
    @RequestMapping("genhtml")
    @ResponseBody
    public String genHtml() throws Exception{
        //生成静态页面
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap<>();
        data.put("hello", "spring freemarker test");
        Writer out = new FileWriter(new File("/Users/zhangcongrong/IdeaProjects/taotao/taotao-item-web/src/main/webapp/WEB-INF/freemarker/out/test.html"));
        template.process(data, out);
        out.close();
        //返回结果
        return "OK";
    }
}

