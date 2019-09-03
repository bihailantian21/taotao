package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
    /**
     * 根据当前的页码和每页的行数进行分页查询
     * @param page
     * @param rows
     * @return
     */
    public EasyUIDataGridResult getItemList(Integer page,Integer rows);

    public TaotaoResult addItem(TbItem item, String desc);

    public TaotaoResult updateItem(TbItem item, String desc);

    public TaotaoResult deleteItem(Long itemId);

    //根据商品id来查询商品
    TbItem getItemById(long itemId);

    //根据商品ID来查询商品描述
    TbItemDesc getItemDescById(long itemId);
}
