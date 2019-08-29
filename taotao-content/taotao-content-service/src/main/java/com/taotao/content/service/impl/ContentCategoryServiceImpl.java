package com.taotao.content.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {


    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        /**
         * 请求的url：/content/category/list
         * 请求的参数：id，当前节点的id。第一次请求是没有参数，需要给默认值“0”
         * 响应数据：List（@ResponseBody）
         * Json数据。
         * [{id:1,text:节点名称,state:open(closed)},
         * {id:2,text:节点名称2,state:open(closed)},
         * {id:3,text:节点名称3,state:open(closed)}]
         *
         * 业务逻辑：
         * 1、取查询参数id，parentId
         * 2、根据parentId查询tb_content_category，查询子节点列表。
         * 3、得到List
         * 4、把列表转换成List
         */
        //创建一个查询类
        TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();

        //设置查询条件
       TbContentCategoryExample.Criteria criteria = contentCategoryExample.createCriteria();
       criteria.andParentIdEqualTo(parentId);

        //查询
        List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(contentCategoryExample);

        //将categoryList转换成List<EasyUITreeNode>
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory contentCategory : categoryList) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(contentCategory.getId());
            easyUITreeNode.setText(contentCategory.getName());
            easyUITreeNode.setState(contentCategory.getIsParent()?"closed":"open");
            //node.setState(tbItemCat.getIsParent()?"closed":"open");

            resultList.add(easyUITreeNode);
        }
        return resultList;
    }

    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        /**
         *请求的url：/content/category/create
         * 请求的参数：
         * Long parentId
         * String name
         *
         * 响应的结果：
         * json数据，TaotaoResult，其中包含一个对象，对象有id属性，新生产的内容分类id
         * 插入数据结点之后需要判断，如果在原结点是叶子节点的时候添加，更新其父节点（is_parent属性设置为1）
         *
         * 业务逻辑：
         * 1、接收两个参数：parentId、name
         * 2、向tb_content_category表中插入数据。
         * a)	创建一个TbContentCategory对象
         * b)	补全TbContentCategory对象的属性
         * c)	向tb_content_category表中插入数据
         * 3、判断父节点的isparent是否为true，不是true需要改为true。
         * 4、需要主键返回。
         * 5、返回TaotaoResult，其中包装TbContentCategory对象
         *
         *
         *
         */
        //实例化一个对象
        TbContentCategory contentCategory = new TbContentCategory();
        //填充属性值
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态。可选值:1(正常),2(删除)，刚添加的节点肯定是正常的
        contentCategory.setStatus(1);
        //刚添加的节点肯定不是父节点
        contentCategory.setIsParent(false);
        //数据库中现在默认的都是1，所以这里我们也写成1
        contentCategory.setSortOrder(1);
        //保存当前操作时间
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入节点到数据库
        contentCategoryMapper.saveAndGetId(contentCategory);
        //添加一个节点需要判断父节点是不是叶子节点，如果父节点是叶子节点的话，
        //需要改成父节点状态
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        //通过id查询节点对象
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //判断新的name值与原来的值是否相同，如果相同则不用更新
        if(name != null && name.equals(contentCategory.getName())){
            return TaotaoResult.ok();
        }
        contentCategory.setName(name);
        //设置更新时间
        contentCategory.setUpdated(new Date());
        //更新数据库
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        //返回结果
        return TaotaoResult.ok();
    }



    //通过父节点id来查询所有子节点，这是抽离出来的公共方法
    private List<TbContentCategory> getContentCategoryListByParentId(long parentId){
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        return list;
    }

    //递归删除节点
    private void deleteNode(long parentId){
        List<TbContentCategory> list = getContentCategoryListByParentId(parentId);
        for(TbContentCategory contentCategory : list){
            contentCategory.setStatus(2);
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
            if(contentCategory.getIsParent()){
                deleteNode(contentCategory.getId());
            }
        }
    }


    @Override
    public TaotaoResult deleteContentCategory(long id) {
        /**
         * 请求的url：/content/category/delete/
         * 参数：id，当前节点的id。
         * 响应的数据：json。TaotaoResult。
         *
         * 业务逻辑：
         * 1、根据id删除记录。
         * 2、判断父节点下是否还有子节点，如果没有需要把父节点的isparent改为false
         * 3、如果删除的是父节点，子节点要级联删除。
         *
         * 两种解决方案：
         * 1）如果判断是父节点不允许删除。
         * 2）递归删除。（不会推荐使用）
         */
        //删除分类，就是改节点的状态为2
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //状态。可选值:1(正常),2(删除)
        contentCategory.setStatus(2);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        //我们还需要判断一下要删除的这个节点是否是父节点，如果是父节点，那么就级联
        //删除这个父节点下的所有子节点（采用递归的方式删除）
        if(contentCategory.getIsParent()){
            deleteNode(contentCategory.getId());
        }
        //需要判断父节点当前还有没有子节点，如果有子节点就不用做修改
        //如果父节点没有子节点了，那么要修改父节点的isParent属性为false即变为叶子节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        List<TbContentCategory> list = getContentCategoryListByParentId(parent.getId());
        //判断父节点是否有子节点是判断这个父节点下的所有子节点的状态，如果状态都是2就说明
        //没有子节点了，否则就是有子节点。
        boolean flag = false;
        for(TbContentCategory tbContentCategory : list){
            if(tbContentCategory.getStatus() == 0){
                flag = true;
                break;
            }
        }
        //如果没有子节点了
        if(!flag){
            parent.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return TaotaoResult.ok();
    }


}
