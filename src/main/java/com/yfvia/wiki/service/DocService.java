package com.yfvia.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yfvia.wiki.domain.Doc;
import com.yfvia.wiki.domain.DocExample;
import com.yfvia.wiki.mapper.DocMapper;
import com.yfvia.wiki.req.DocQueryReq;
import com.yfvia.wiki.req.DocSaveReq;
import com.yfvia.wiki.resp.DocQueryResp;
import com.yfvia.wiki.resp.PageResp;
import com.yfvia.wiki.utils.CopyUtil;
import com.yfvia.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocService {
    @Resource
    private DocMapper docMapper;

    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    /**
     * 查询文档数据
     */
    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docs = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docs);
        LOG.info("总行数：{}", pageInfo.getTotal());
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());

        List<DocQueryResp> copyList = new ArrayList<>();
        for (Doc doc : docs) {
            DocQueryResp copyDoc = CopyUtil.copy(doc, DocQueryResp.class);
            copyDoc.setId(doc.getId().toString());
            copyDoc.setParent(doc.getParent().toString());
            copyDoc.setEbookId(doc.getEbookId().toString());

            copyList.add(copyDoc);
        }

        pageResp.setList(copyList);

        return pageResp;
    }

    /**
     * 查询文档数据
     */
    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> categories = docMapper.selectByExample(docExample);

        ArrayList<DocQueryResp> resps = new ArrayList<>();

        for (Doc doc : categories) {
            DocQueryResp copy = CopyUtil.copy(doc, DocQueryResp.class);
            copy.setId(doc.getId().toString());
            copy.setParent(doc.getParent().toString());
            copy.setEbookId(doc.getEbookId().toString());

            resps.add(copy);
        }

        return resps;
    }


    /**
     * 保存文档数据
     */
    public Boolean save(DocSaveReq req) {

        if (ObjectUtils.isEmpty(req.getId())) {
            Doc doc = CopyUtil.copy(req, Doc.class);
            doc.setId(snowFlake.nextId());
            doc.setParent(Long.valueOf(req.getParent()));
            doc.setEbookId(Long.valueOf(req.getEbookId()));
            int res = docMapper.insert(doc);
            if (res != 1) return false;
            return true;
        }

        Doc doc = docMapper.selectByPrimaryKey(Long.parseLong(req.getId()));
        if (ObjectUtils.isEmpty(doc)) {
            return false;
        }

        Doc newDoc = CopyUtil.copy(req, Doc.class);
        System.out.println(req.getParent());
        newDoc.setId(Long.valueOf(req.getId()));
        newDoc.setParent(Long.valueOf(req.getParent()));
        newDoc.setEbookId(Long.valueOf(req.getEbookId()));

        int res = docMapper.updateByPrimaryKey(newDoc);
        if (res != 1) return false;
        return true;
    }


    /**
     * 删除文档
     */
    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(id)) return false;

        int res = docMapper.deleteByPrimaryKey(id);
        if (res != 1) return false;

        return true;
    }
}
