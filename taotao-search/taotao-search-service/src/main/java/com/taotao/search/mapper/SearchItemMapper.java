package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

public interface SearchItemMapper {

    //获取要导入到索引库中的数据
    List<SearchItem> getSearchItemList();

    //根据商品ID查询商品详情
    // 功能分析
    //
    //1、接收消息。需要创建MessageListener接口的实现类。
    //2、取消息，取商品id。
    //3、根据商品id查询数据库。
    //4、创建一SolrInputDocument对象。
    //5、使用SolrServer对象写入索引库。
    //6、返回成功，返回TaotaoResult。
    SearchItem getItemById(long itemId);
}
