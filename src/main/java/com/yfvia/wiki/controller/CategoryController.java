package com.yfvia.wiki.controller;

import com.yfvia.wiki.req.CategoryQueryReq;
import com.yfvia.wiki.req.CategorySaveReq;
import com.yfvia.wiki.resp.CommonResp;
import com.yfvia.wiki.resp.CategoryQueryResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询电子书
     */
    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        System.out.println("req:" + req.getSize());
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        resp.setContent(categoryService.list(req));
        return resp;
    }

    /**
     * 查询所有电子书
     */
//    @GetMapping("/all")
//    public CommonResp all() {
//        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
//        resp.setContent(categoryService.all());
//        return resp;
//    }

    /**
     * 保存电子书
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {

        categoryService.save(req);
        CommonResp<Boolean> resp = new CommonResp<>();
        return resp;
    }

    /**
     * 根据id删除电子书
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        Boolean res = categoryService.delete(id);
        CommonResp resp = new CommonResp();
        if (!res) {
            resp.setMessage("删除失败");
            resp.setSuccess(false);
        } else {
            resp.setMessage("删除成功");
        }

        return resp;
    }

    /**
     * 通过名称查询电子书
     */
    @GetMapping("/query")
    public CommonResp query(@Valid CategoryQueryReq req) {
        System.out.println("name:" + req.getName() + req.getSize());

        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        return resp;
    }
}
