package com.taotao.content.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;

public interface ContentService {

    //获取内容列表
    EasyUIDataGridResult getContentList(long categoryId, int page, int rows);

    //添加内容
    TaotaoResult addContent(TbContent content);

    //获取单个内容信息
    TaotaoResult getContent(long id);

    //修改内容
    TaotaoResult updateContent(TbContent content);

    //删除内容
    TaotaoResult deleteContent(String ids);

    //根据内容分类id来获取内容列表
    List<TbContent> getContentListByCid(long cid);



}
