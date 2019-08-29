package pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbContentMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {

    @Test
    public void testhelper() {

        //2.初始化spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        //3.获取mapper的代理对象
        TbContentMapper itemMapper = context.getBean(TbContentMapper.class);

        //1.设置分页信息
        PageHelper.startPage(1,10);//获取第一页的3条内容 紧跟着的第一个查询才会被分页
        //4.调用mapper的方法查询数据
        TbContentExample example = new TbContentExample();//设置查询条件使用
        List<TbContent> list = itemMapper.selectByExample(example);//select * from tb_item;
        List<TbContent> list2 = itemMapper.selectByExample(example);//select * from tb_item;
        //取分页信息
        //用PageInfo对结果进行包装
        PageInfo<TbContent> info = new PageInfo<>(list);
        //测试PageInfo全部属性PageInfo包含了非常全面的分页属性
        System.out.println("第一个分页的list的集合长度"+list.size());
        System.out.println("第二个分页的list的集合长度"+list2.size());


        //5.遍历结果集 打印
        System.out.println("查询的总记录数："+info.getTotal());
        System.out.println("查询的总页数："+info.getPages());
        System.out.println("查询的第几页："+info.getPageNum());
        System.out.println("查询了几条记录："+info.getPageSize());

        for (TbContent tbItem : list) {
            System.out.println(tbItem.getId()+">>>mingch>>"+tbItem.getTitle());
        }


    }
}
