package com.yfvia.wiki.controller;

import com.yfvia.wiki.req.EbookQueryReq;
import com.yfvia.wiki.req.EbookSaveReq;
import com.yfvia.wiki.resp.CommonResp;
import com.yfvia.wiki.resp.EbookQueryResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        resp.setContent(ebookService.list(req));
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        resp.setContent(ebookService.all());
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req) {

        ebookService.save(req);
        CommonResp<Boolean> resp = new CommonResp<>();
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        Boolean res = ebookService.delete(id);
        CommonResp resp = new CommonResp();
        if (!res) {
            resp.setMessage("删除失败");
            resp.setSuccess(false);
        } else {
            resp.setMessage("删除成功");
        }

        return resp;
    }
}
