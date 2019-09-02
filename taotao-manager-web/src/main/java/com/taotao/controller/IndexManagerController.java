package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;

@Controller
public class IndexManagerController {

    @Autowired
    private SearchItemService searchItemService;

    /**
     * 应该在taotao-manager-web工程中调用导入索引库的服务。
     *
     * 在taotao-manager-web后台系统中 做一个导入索引库的功能界面（例如：有个按钮，点击即可将数据从数据库中导入到索引库）。
     *
     * 业务逻辑：
     * 1.点击按钮，表现层调用服务层的工程的导入索引库的方法
     * 2.服务层实现 调用mapper接口的方法查询所有的商品的数据
     * 3.将数据一条条添加到solrinputdocument文档中
     * 4.将文档添加到索引库中
     * 5.提交，并返回导入成功即可
     * @return
     */
    @RequestMapping("/index/import")
    @ResponseBody
    public TaotaoResult importIndex(){
        /*TaotaoResult result = searchItemService.importItemsToIndex();
        return result;*/
        try {
            TaotaoResult result = searchItemService.importItemsToIndex();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, "导入数据失败");
        }
    }
}

