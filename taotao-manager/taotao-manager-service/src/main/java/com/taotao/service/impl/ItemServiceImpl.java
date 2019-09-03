package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.service.ItemService;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;




@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name="itemAddTopic")
    private Destination destination;

    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${ITEM_EXPIRE}")
    private Integer ITEM_EXPIRE;



    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {

        //1.设置分页的信息 使用pagehelper
        if (page == null) page = 1;
        if (rows == null) rows = 30;
        PageHelper.startPage(page,rows);
        //2.注入mapper
        //3.创建example对象，不需要设置查询条件
        TbItemExample example = new TbItemExample();
        //4.根据mapper调用查询所有数据的方法
        List<TbItem> list = itemMapper.selectByExample(example);
        //5.获取分页的信息
        PageInfo<TbItem> info = new PageInfo<>(list);
        //6.封装到EasyUIDataGridResult
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        //7.返回

        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem item, String desc) {
        /**
         * 1、生成商品id
         * 实现方案：
         * a) Uuid，字符串，不推荐使用。
         * b) 数值类型，不重复。日期+时间+随机数20160402151333123123
         * c) 可以直接去毫秒值+随机数。可以使用。
         * d) 使用redis。Incr。推荐使用。
         * 使用IDUtils生成商品id
         * 2、补全TbItem对象的属性
         * 3、向商品表插入数据
         * 4、创建一个TbItemDesc对象
         * 5、补全TbItemDesc的属性
         * 6、向商品描述表插入数据
         * 7、TaotaoResult.ok()
         */
            // 1、生成商品id
            final long itemId = IDUtils.genItemId();
            // 2、补全TbItem对象的属性
            item.setId(itemId);
            //商品状态，1-正常，2-下架，3-删除
            item.setStatus((byte) 1);
            Date date = new Date();
            item.setCreated(date);
            item.setUpdated(date);
            // 3、向商品表插入数据
            itemMapper.insert(item);
            // 4、创建一个TbItemDesc对象
            TbItemDesc itemDesc = new TbItemDesc();
            // 5、补全TbItemDesc的属性
            itemDesc.setItemId(itemId);
            itemDesc.setItemDesc(desc);
            itemDesc.setCreated(date);
            itemDesc.setUpdated(date);
            // 6、向商品描述表插入数据
            itemDescMapper.insert(itemDesc);


            //发送activemq消息
        //需要在商品的添加/修改，删除的时候，同步索引库。将数据从数据库中查询出来导入到索引库更新。
        //消息的发送方为：所在工程taotao-manager-service
        //功能分析：
        //当商品添加完成后发送一个TextMessage，包含一个商品id即可。
        //接收端接收到商品id通过数据库查询到商品的信息（搜索的结果商品的信息）再同步索引库。
        //消息的接收方为：所在工程taotao-search-service
        //
        //两个工程都需要依赖activmq:

        //第一步：初始化一个spring容器
        //第二步：从容器中获得JMSTemplate对象。
        //第三步：从容器中获得一个Destination对象
        //第四步：使用JMSTemplate对象发送消息，需要知道Destination
        		jmsTemplate.send(destination,new MessageCreator() {

        			@Override
        			public Message createMessage(Session session) throws JMSException {
        				TextMessage textMessage = session.createTextMessage(itemId+"");
        				return textMessage;
        			}
        		});

            // 7、TaotaoResult.ok()
            return TaotaoResult.ok();
    }





    @Override
    public TaotaoResult updateItem(TbItem item, String desc) {
        // 1、生成商品id
        long itemId = item.getId();
        // 2、补全TbItem对象的属性
        Date date = new Date();
        item.setUpdated(date);

        // 3、向商品表插入数据
        itemMapper.updateByPrimaryKey(item);

        // 4、创建一个TbItemDesc对象
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        // 5、补全TbItemDesc的属性

        itemDesc.setItemDesc(desc);

        itemDesc.setUpdated(date);
        // 6、向商品描述表插入数据
        itemDescMapper.updateByPrimaryKey(itemDesc);
        // 7、TaotaoResult.ok()
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteItem(Long itemId) {
        itemMapper.deleteByPrimaryKey(itemId);
        itemDescMapper.deleteByPrimaryKey(itemId);
        return TaotaoResult.ok();
    }
/*
    @Override
    public TbItem getItemById(long itemId) {
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        return tbItemDesc;
    }*/


    //添加缓存

    /**
     * 使用redis做缓存。
     *
     * 业务逻辑：
     * 1、根据商品id 到缓存中查询。
     * 2、查到缓存，直接返回。
     * 3、查不到，查询数据库。
     * 4、把数据放到缓存中。
     * 5、返回数据。
     *
     * 缓存中缓存热点数据，提高缓存的使用率。需要设置缓存的有效期expire。一般是一天的时间，可以根据实际情况调整。
     *
     * 需要使用String类型来保存商品数据。
     * 可以加前缀方法对redis中的key进行归类。
     * 例如：key:
     * ITEM_INFO:123456:BASE
     * ITEM_INFO:123456:DESC
     *
     * 作用：方便进行管理。
     *
     *
     * 如果把二维表保存到redis中:
     * 1、表名就是第一层
     * 2、主键是第二层
     * 3、字段名第三层
     * 三层使用“:”分隔作为key，value就是字段中的内容。
     * 表名：主键：字段名
     * @param itemId
     * @return
     */
    @Override
    public TbItem getItemById(long itemId) {
        //查询数据库之前先查询缓存
        try {
            String json = jedisClient.get(ITEM_INFO+":"+itemId+":BASE");
            if(!StringUtils.isBlank(json)){
                //把json转换成对象
                return JsonUtils.jsonToPojo(json, TbItem.class);//.parseObject(json, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        //把查询结果添加到缓存
        try {
            //把查询结果添加到缓存
            jedisClient.set(ITEM_INFO+":"+itemId+":BASE", JsonUtils.objectToJson(tbItem));//.toJSONString(tbItem));
            //设置过期时间，提高缓存的利用率
            jedisClient.expire(ITEM_INFO+":"+itemId+":BASE", ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        //查询数据库之前先查询缓存
        try {
            String json = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");
            if(!StringUtils.isBlank(json)){
                //把json转换成对象
                return JsonUtils.jsonToPojo(json, TbItemDesc.class);//.parseObject(json, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        //把查询结果添加到缓存
        try {
            //把查询结果添加到缓存
            jedisClient.set(ITEM_INFO+":"+itemId+":DESC", JsonUtils.objectToJson(tbItemDesc));//.toJSONString(tbItemDesc));
            //设置过期时间，提高缓存的利用率
            jedisClient.expire(ITEM_INFO+":"+itemId+":DESC", ITEM_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }




}
