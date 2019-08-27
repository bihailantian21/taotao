package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

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
            long itemId = IDUtils.genItemId();
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
}
