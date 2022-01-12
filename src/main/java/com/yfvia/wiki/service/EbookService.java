package com.yfvia.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yfvia.wiki.domain.Demo;
import com.yfvia.wiki.domain.DemoExample;
import com.yfvia.wiki.domain.Ebook;
import com.yfvia.wiki.domain.EbookExample;
import com.yfvia.wiki.mapper.DemoMapper;
import com.yfvia.wiki.mapper.EbookMapper;
import com.yfvia.wiki.req.EbookReq;
import com.yfvia.wiki.resp.EbookResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooks);
        LOG.info("总行数：{}", pageInfo.getTotal());
        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(ebooks, EbookResp.class));

        return pageResp;
    }
}
