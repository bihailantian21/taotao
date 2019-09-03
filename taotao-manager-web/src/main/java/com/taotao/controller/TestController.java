package com.taotao.controller;

import com.taotao.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试使用controller 查询当前的时间
 */

@Controller
public class TestController {
    @Autowired
    private TestService testService;
    /**
     * 测试dubbo配置是否正常
     * @return
     */
    @RequestMapping("/test/queryNow")
    @ResponseBody
    public String queryNow(){
        return testService.queryNow();
    }
}


