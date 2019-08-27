package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    /**
     * 参数：longparentId
     *
     * 业务逻辑：
     * 1、根据parentId查询节点列表
     * 2、转换成EasyUITreeNode列表。
     * 3、返回。
     *
     * 返回值：List
     */
    public List<EasyUITreeNode> getCatList(long parentId);
}
