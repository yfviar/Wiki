package com.yfvia.wiki.controller;

import com.yfvia.wiki.req.DocQueryReq;
import com.yfvia.wiki.req.DocSaveReq;
import com.yfvia.wiki.resp.DocQueryResp;
import com.yfvia.wiki.resp.CommonResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    DocService docService;

    /**
     * 分页查询文档
     */
    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        System.out.println("req:" + req.getSize());
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        resp.setContent(docService.list(req));
        return resp;
    }

    /**
     * 查询所有文档
     */
    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
//        System.out.println("List-------------------:" + list);
        resp.setContent(list);
        return resp;
    }

    /**
     * 保存文档
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {

        Boolean isSuccess = docService.save(req);
        CommonResp<Boolean> resp = new CommonResp<>();
        if (!isSuccess) {
            resp.setSuccess(isSuccess);
        }
        return resp;
    }

    /**
     * 根据id删除文档
     */
    @DeleteMapping("/delete/{ids}")
    public CommonResp delete(@PathVariable String ids) {

        List<String> list = Arrays.asList(ids.split(","));
        docService.delete(list);
        CommonResp resp = new CommonResp();


        return resp;
    }

    /**
     * 通过名称查询文档
     */
    @GetMapping("/query")
    public CommonResp query(@Valid DocQueryReq req) {
        System.out.println("name:" + req.getName() + req.getSize());

        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        return resp;
    }


    /**
     * 获取文档内容
     */
    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

    /**
     * 点赞
     */
    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        CommonResp commonResp = new CommonResp();
        docService.vote(id);
        return commonResp;
    }
}
