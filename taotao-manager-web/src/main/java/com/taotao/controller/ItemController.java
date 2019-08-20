package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    /**
     * 1、初始化表格请求的url：/item/list
     * 2、Datagrid默认请求参数：
     * 1、page：当前的页码，从1开始。
     * 2、rows：每页显示的记录数。
     * 3、响应的数据：json数据。EasyUIDataGridResult
     */
    @RequestMapping(value = "/item/list",method = RequestMethod.GET)
    @ResponseBody

    public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
        //1.引入服务
        //2.注入服务
        //3.调用服务的方法
        return itemService.getItemList(page,rows);

        /**
         * EasyUIDataGridResult result = itemService.getItemList(page, rows);
         * 		return result;
         */
    }

}
