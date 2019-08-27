package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

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
}
