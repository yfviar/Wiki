package com.yfvia.wiki.controller;

import com.yfvia.wiki.req.EbookQueryReq;
import com.yfvia.wiki.req.EbookSaveReq;
import com.yfvia.wiki.resp.CommonResp;
import com.yfvia.wiki.resp.EbookQueryResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
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
    public CommonResp save(@RequestBody EbookSaveReq req) {
        System.out.println("Save Req:" + req);

        Boolean res = ebookService.save(req);

        CommonResp<Boolean> resp = new CommonResp<>();
        if (!res) {
            resp.setMessage("保存失败！");
            return resp;
        }
        resp.setMessage("保存成功");

        return resp;
    }
}
