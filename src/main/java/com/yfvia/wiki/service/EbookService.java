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
import com.yfvia.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    /**
     * 查询电子书数据
     */
    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        if (!ObjectUtils.isEmpty(req.getCategoryId2())) {
            criteria.andCategory2IdEqualTo(Long.valueOf(req.getCategoryId2()));
        }


        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebooks = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooks);
        LOG.info("总行数：{}", pageInfo.getTotal());
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());

        List<EbookQueryResp> copyList = new ArrayList<>();
        for (Ebook ebook : ebooks) {
            EbookQueryResp copyEbook = CopyUtil.copy(ebook, EbookQueryResp.class);
            copyEbook.setId(ebook.getId().toString());
            copyList.add(copyEbook);
        }

        pageResp.setList(copyList);

        return pageResp;
    }

    /**
     * 查询所有的电子书数据
     */
    public PageResp<EbookQueryResp> all() {
        List<Ebook> ebooks = ebookMapper.selectByExample(new EbookExample());
        PageResp<EbookQueryResp> resp = new PageResp<>();
        resp.setTotal(0L);

        List<EbookQueryResp> copyList = new ArrayList<>();
        for (Ebook ebook : ebooks) {
            EbookQueryResp copyEbook = CopyUtil.copy(ebook, EbookQueryResp.class);
            copyEbook.setId(ebook.getId().toString());
            copyList.add(copyEbook);
        }

        resp.setList(copyList);
        return resp;
    }

    /**
     * 保存电子书数据
     */
    public Boolean save(EbookSaveReq req) {

        if (ObjectUtils.isEmpty(req.getId())) {
            Ebook ebook = CopyUtil.copy(req, Ebook.class);
            ebook.setId(snowFlake.nextId());
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            int res = ebookMapper.insert(ebook);
            if (res != 1) return false;
            return true;
        }

        Ebook ebook = ebookMapper.selectByPrimaryKey(Long.parseLong(req.getId()));
        if (ObjectUtils.isEmpty(ebook)) {
            return false;
        }

        Ebook newEbook = CopyUtil.copy(req, Ebook.class);
        newEbook.setId(Long.valueOf(req.getId()));

        int res = ebookMapper.updateByPrimaryKey(newEbook);
        if (res != 1) return false;
        return true;
    }


    /**
     * 删除电子书
     */
    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(id)) return false;

        int res = ebookMapper.deleteByPrimaryKey(id);
        if (res != 1) return false;

        return true;
    }
}
