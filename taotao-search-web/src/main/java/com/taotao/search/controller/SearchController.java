package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;


    /**
     * 请求的url：/search
     * 参数：
     * 1、q 查询条件。
     * 2、page 页码。默认为1
     * 返回值：
     * 逻辑视图，返回值。String。
     *
     * 业务逻辑：
     * 1、接收参数
     * 2、调用服务查询商品列表
     * 3、把查询结果传递给页面。需要参数回显。
     * @param queryString
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString,
                         @RequestParam(defaultValue="1") Integer page,Model model) throws  Exception{
        /*try {
            //解决乱码问题
            //测试发现有乱码，是GET请求乱码，需要对请求参数进行转码处理：
            queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
            //调用服务执行查询
            SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
            //把结果传递给页面
            model.addAttribute("query", queryString);
            model.addAttribute("totalPages", searchResult.getTotalPages());
            model.addAttribute("itemList", searchResult.getItemList());
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回逻辑视图
        return "search";*/


        //人为设置异常
        //int aaa = 1/0;

        //解决乱码问题
        //测试发现有乱码，是GET请求乱码，需要对请求参数进行转码处理：
        queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
        //调用服务执行查询
        SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
        //把结果传递给页面
        model.addAttribute("query", queryString);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("itemList", searchResult.getItemList());
        model.addAttribute("page", page);
        //返回逻辑视图
        return "search";
    }


    //成功开启POP3/SMTP服务,在第三方客户端登录时，密码框请输入以下授权码：
    //sfvpxerpfsflbgaj

}

