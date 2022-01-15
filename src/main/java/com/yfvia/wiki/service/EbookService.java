package com.yfvia.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yfvia.wiki.domain.Ebook;
import com.yfvia.wiki.domain.EbookExample;
import com.yfvia.wiki.mapper.EbookMapper;
import com.yfvia.wiki.req.EbookQueryReq;
import com.yfvia.wiki.req.EbookSaveReq;
import com.yfvia.wiki.resp.EbookQueryResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooks);
        LOG.info("总行数：{}", pageInfo.getTotal());
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(ebooks, EbookQueryResp.class));

        return pageResp;
    }

    public PageResp<EbookQueryResp> all() {
        List<Ebook> ebooks = ebookMapper.selectByExample(new EbookExample());
        PageResp<EbookQueryResp> resp = new PageResp<>();
        resp.setTotal(0L);
        resp.setList(CopyUtil.copyList(ebooks, EbookQueryResp.class));
        return resp;
    }

    public Boolean save(EbookSaveReq req) {

        if (ObjectUtils.isEmpty(req.getId())) {
            Ebook ebook = CopyUtil.copy(req, Ebook.class);
            int res = ebookMapper.insert(ebook);
            if (res != 1) return false;
            return true;
        }

        Ebook ebook = ebookMapper.selectByPrimaryKey(req.getId());
        if (ObjectUtils.isEmpty(ebook)) {
            return false;
        }
        Ebook newEbook = CopyUtil.copy(req, Ebook.class);
        int res = ebookMapper.updateByPrimaryKey(newEbook);
        if (res != 1) return false;
        return true;
    }

}
