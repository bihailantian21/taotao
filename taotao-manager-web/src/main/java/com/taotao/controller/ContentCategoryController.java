package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
        return list;
    }

    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult addContentCategory(Long parentId, String name) {
        TaotaoResult taotaoResult = contentCategoryService.addContentCategory(parentId, name);
        return taotaoResult;
    }

    @RequestMapping("/update")
    @ResponseBody
    public TaotaoResult updateContentCategory(Long id, String name) {
        TaotaoResult taotaoResult = contentCategoryService.updateContentCategory(id, name);
        return taotaoResult;
    }

    @RequestMapping("/delete/")
    @ResponseBody
    public TaotaoResult deleteContentCategory(Long id) {
        TaotaoResult taotaoResult = contentCategoryService.deleteContentCategory(id);
        return taotaoResult;
    }

}
