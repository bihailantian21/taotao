package com.taotao.service.impl;

import com.taotao.service.TestService;
import com.taotao.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper mapper;


    @Override
    public String queryNow() {
        //1.注入mapper
        //2.调用mapper的方法返回
        return mapper.queryNow();
    }

}

