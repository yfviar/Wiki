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
    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        resp.setContent(docService.all());
        return resp;
    }

    /**
     * 保存文档
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {

        docService.save(req);
        CommonResp<Boolean> resp = new CommonResp<>();
        return resp;
    }

    /**
     * 根据id删除文档
     */
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        Boolean res = docService.delete(id);
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
     * 通过名称查询文档
     */
    @GetMapping("/query")
    public CommonResp query(@Valid DocQueryReq req) {
        System.out.println("name:" + req.getName() + req.getSize());

        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        return resp;
    }
}
