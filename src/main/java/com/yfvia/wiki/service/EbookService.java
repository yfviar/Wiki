package com.yfvia.wiki.service;

import com.yfvia.wiki.domain.Demo;
import com.yfvia.wiki.domain.DemoExample;
import com.yfvia.wiki.domain.Ebook;
import com.yfvia.wiki.domain.EbookExample;
import com.yfvia.wiki.mapper.DemoMapper;
import com.yfvia.wiki.mapper.EbookMapper;
import com.yfvia.wiki.req.EbookReq;
import com.yfvia.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {

        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        ArrayList<EbookResp> ebookResps = new ArrayList<>();
        for (Ebook ebook : ebooks) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook, ebookResp);
            ebookResps.add(ebookResp);
        }

        return ebookResps;
    }
}
