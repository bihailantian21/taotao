package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {

    /**
     * 第一步：把solrJ的jar包添加到工程中。
     * 第二步：创建一个SolrServer，使用HttpSolrServer创建对象。
     * 第三步：创建一个文档对象SolrInputDocument对象。
     * 第四步：向文档中添加域。必须有id域，域的名称必须在schema.xml中定义。
     * 第五步：把文档添加到索引库中。
     * 第六步：提交。
     * @throws Exception
     */
    @Test
    public void testAddDocument() throws Exception{
        //创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
        //如果有多个collection则需要指定要操作哪个collection，如果只有一个，可以不指定
        SolrServer solrServer = new HttpSolrServer("http://192.168.173.148:80/solr/collection1");
        //创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
        document.addField("id", "1111");
        document.addField("item_title", "海尔空调");
        document.addField("item_sell_point", "送电暖宝一个哦");
        document.addField("item_price", 10000);
        document.addField("item_image", "http://www.123.jpg");
        document.addField("item_category_name", "电器");
        document.addField("item_desc", "这是一款最新的空调，质量好，值得信赖！！");
        //将document添加到索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }


    /**
     * 第一步：创建一个SolrServer对象。
     * 第二步：调用SolrServer对象的根据id删除的方法。
     * 第三步：提交。
     * @throws Exception
     */
    @Test
    public void testDeleteDocument() throws Exception{
        //创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
        SolrServer solrServer = new HttpSolrServer("http://192.168.173.148:80/solr/collection1");
        //通过id来删除文档
        solrServer.deleteById("1111");
        //提交
        solrServer.commit();
    }

    /**
     * 查询步骤：
     * 第一步：创建一个SolrServer对象
     * 第二步：创建一个SolrQuery对象。
     * 第三步：向SolrQuery中添加查询条件、过滤条件。。。
     * 第四步：执行查询。得到一个Response对象。
     * 第五步：取查询结果。
     * 第六步：遍历结果并打印。
     * @throws Exception
     */
    @Test
    public void queryDocument() throws Exception{
        //创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
        SolrServer solrServer = new HttpSolrServer("http://192.168.173.148:80/solr/collection1");
        //通过id来删除文档
        SolrQuery query = new SolrQuery();
        query.setQuery("id:1111");
        QueryResponse response = solrServer.query(query);
        SolrDocumentList list = response.getResults();
        for(SolrDocument document : list){
            String id = document.getFieldValue("id").toString();
            String title = document.getFieldValue("item_title").toString();
            System.out.println(id);
            System.out.println(title);
        }
    }

    /**
     * 带高亮显示
     * @throws Exception
     */
    /*@Test
    public void queryDocumentWithHighLighting() throws Exception {
        // 第一步：创建一个SolrServer对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.173.148:80/solr/collection1");
        // 第二步：创建一个SolrQuery对象。
        SolrQuery query = new SolrQuery();
        // 第三步：向SolrQuery中添加查询条件、过滤条件。。。
        query.setQuery("测试");
        //指定默认搜索域
        query.set("df", "item_keywords");
        //开启高亮显示
        query.setHighlight(true);
        //高亮显示的域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("");
        query.setHighlightSimplePost("");
        // 第四步：执行查询。得到一个Response对象。
        QueryResponse response = solrServer.query(query);
        // 第五步：取查询结果。
        SolrDocumentList solrDocumentList = response.getResults();
        System.out.println("查询结果的总记录数：" + solrDocumentList.getNumFound());
        // 第六步：遍历结果并打印。
        for (SolrDocument solrDocument : solrDocumentList) {
            System.out.println(solrDocument.get("id"));
        //取高亮显示
            Map<String, Map<String, List>> highlighting = response.getHighlighting();
            List list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = null;
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(solrDocument.get("item_price"));
        }
    }

*/

    @Test
    public void queryDocumentPhone() throws Exception{
        //创建一个SolrServer对象，创建一个HttpSolrServer对象，需要指定solr服务的url
        SolrServer solrServer = new HttpSolrServer("http://192.168.173.148:80/solr/collection1");
        //创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件、过滤条件、分页条件、排序条件、高亮
        //query.set("q", "*:*");
        query.setQuery("手机");
        //分页条件
        query.setStart(0);
        query.setRows(3);
        //设置默认搜索域
        query.set("df", "item_keywords");
        //设置高亮
        query.setHighlight(true);
        //高亮显示的域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //执行查询，得到一个Response对象
        QueryResponse response = solrServer.query(query);
        //取查询结果
        SolrDocumentList solrDocumentList = response.getResults();
        //取查询结果总记录数
        System.out.println("查询结果总记录数："+solrDocumentList.getNumFound());
        for(SolrDocument document : solrDocumentList){
            System.out.println(document.getFieldValue("id"));
            //取高亮显示
            Map<String,Map<String,List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(document.getFieldValue("id")).get("item_title");
            String itemTitle = "";
            if(list != null && list.size() > 0){
                itemTitle = list.get(0);
            }else {
                itemTitle = (String)document.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(document.get("item_sell_point"));
            System.out.println(document.get("item_price"));
            System.out.println(document.get("item_image"));
            System.out.println(document.get("item_category_name"));
            System.out.println("===============================================");
        }
    }




}

