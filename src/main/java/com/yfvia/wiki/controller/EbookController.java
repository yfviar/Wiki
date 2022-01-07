package com.yfvia.wiki.controller;

import com.yfvia.wiki.domain.Demo;
import com.yfvia.wiki.domain.Ebook;
import com.yfvia.wiki.req.EbookReq;
import com.yfvia.wiki.resp.CommonResp;
import com.yfvia.wiki.resp.EbookResp;
import com.yfvia.wiki.service.DemoService;
import com.yfvia.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EbookController {

    @Autowired
    EbookService ebookService;

    @GetMapping("/ebook/list")
    public CommonResp list(EbookReq req) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        resp.setContent(ebookService.list(req));
        return resp;
    }
}