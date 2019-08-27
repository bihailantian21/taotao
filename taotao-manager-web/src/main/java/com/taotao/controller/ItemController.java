package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 请求的url：/item/save
     *
     * 参数：TbItemitem,String desc
     *
     * 返回值：TaotaoResult
     * /rest/page/item-edit
     */
    @RequestMapping("/item/save")
    @ResponseBody
    public TaotaoResult saveItem(TbItem item, String desc) {
        System.out.println("controllerXXXXX"+item.getTitle());
        System.out.println("controllerXXXXX"+item.getBarcode());
        System.out.println("controllerXXXXX"+desc);
        TaotaoResult result = itemService.addItem(item, desc);
        return result;
    }

    @RequestMapping("/rest/page/item-edit")
    @ResponseBody
    public TaotaoResult updateItem(TbItem item, String desc) {
        System.out.println("controllerupdateXXXXX"+item.getId());
        System.out.println("controllerupdateXXXXX"+item.getTitle());
        System.out.println("controllerupdateXXXXX"+item.getBarcode());
        System.out.println("controllerupdateXXXXX"+desc);
        TaotaoResult result = itemService.updateItem(item,desc);
        return result;
    }

    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public TaotaoResult deleteItem(@RequestParam(value = "ids",defaultValue = "0")Long itemId) {
        System.out.println("deleteitem"+itemId);

        TaotaoResult result = itemService.deleteItem(itemId);
        return result;
    }

//    @RequestMapping("/item/instock")
//    @ResponseBody
//    public TaotaoResult saveItem(TbItem item, String desc) {
//        TaotaoResult result = itemService.addItem(item, desc);
//        return result;
//    }
//
//    @RequestMapping("/item/reshelf")
//    @ResponseBody
//    public TaotaoResult saveItem(TbItem item, String desc) {
//        TaotaoResult result = itemService.addItem(item, desc);
//        return result;
//    }





}
