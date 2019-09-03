package com.taotao.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreeMarker {

    /**
     * 需要创建模板文件和编写测试类：
     *
     * 使用步骤：
     * 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
     * 第二步：设置模板文件所在的路径。
     * 第三步：设置模板文件使用的字符集。一般就是utf-8.
     * 第四步：加载一个模板，创建一个模板对象。
     * 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
     * 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
     * 第七步：调用模板对象的process方法输出文件。
     * 第八步：关闭流。
     * @throws Exception
     */
    @Test
    public void testFreeMarker() throws Exception{
        //1.创建一个模板文件(就是我们刚创建的hello.ftl模板)
        //2.创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3.设置模板所在的路径
        configuration.setDirectoryForTemplateLoading(new File("/Users/zhangcongrong/IdeaProjects/taotao/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
        //4.设置模板的字符集，一般为utf-8
        configuration.setDefaultEncoding("utf-8");
        //5.使用Configuration对象加载一个模板文件，需要指定模板文件的文件名。
        Template template = configuration.getTemplate("student.ftl");
        //6.创建一个数据集，可以是pojo也可以是map，推荐使用map
        Map data = new HashMap<>();
        data.put("hello", "hello freemarker");

        Student student = new Student(1,"张三",20,"北京市");
        data.put("student",student);

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"小米",20,"西安市"));
        studentList.add(new Student(2,"小米2",20,"西安市2"));
        studentList.add(new Student(3,"小米3",20,"西安市3"));
        studentList.add(new Student(4,"小米4",20,"西安市4"));
        studentList.add(new Student(5,"小米5",20,"西安市5"));
        studentList.add(new Student(6,"小米6",20,"西安市6"));
        data.put("stuList",studentList);

        //日期类型
        data.put("date",new Date());

        //给变量赋值
        data.put("var",123);


        //7.创建一个Writer对象，指定输出文件的路径及文件名
        Writer out = new FileWriter(new File("/Users/zhangcongrong/IdeaProjects/taotao/taotao-item-web/src/main/webapp/WEB-INF/freemarker/out/student.html"));
        //8.使用模板对象的process方法输出文件
        template.process(data, out);
        //9.关闭流
        out.close();
    }
}

