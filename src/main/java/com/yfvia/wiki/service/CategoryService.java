package com.yfvia.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yfvia.wiki.domain.Category;
import com.yfvia.wiki.domain.CategoryExample;
import com.yfvia.wiki.mapper.CategoryMapper;
import com.yfvia.wiki.req.CategoryQueryReq;
import com.yfvia.wiki.req.CategorySaveReq;
import com.yfvia.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    /**
     * 查询分类数据
     */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categorys = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categorys);
        LOG.info("总行数：{}", pageInfo.getTotal());
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());

        List<CategoryQueryResp> copyList = new ArrayList<>();
        for (Category category : categorys) {
            CategoryQueryResp copyCategory = CopyUtil.copy(category, CategoryQueryResp.class);
            copyCategory.setId(category.getId().toString());
            copyCategory.setParent(category.getParent().toString());
            copyList.add(copyCategory);
        }

        pageResp.setList(copyList);

        return pageResp;
    }
    

    /**
     * 保存分类数据
     */
    public Boolean save(CategorySaveReq req) {

        if (ObjectUtils.isEmpty(req.getId())) {
            Category category = CopyUtil.copy(req, Category.class);
            category.setId(snowFlake.nextId());
            category.setParent(Long.valueOf(req.getParent()));
            int res = categoryMapper.insert(category);
            if (res != 1) return false;
            return true;
        }

        Category category = categoryMapper.selectByPrimaryKey(Long.parseLong(req.getId()));
        if (ObjectUtils.isEmpty(category)) {
            return false;
        }

        Category newCategory = CopyUtil.copy(req, Category.class);
        newCategory.setId(Long.valueOf(req.getId()));
        newCategory.setParent(Long.valueOf(req.getParent()));

        int res = categoryMapper.updateByPrimaryKey(newCategory);
        if (res != 1) return false;
        return true;
    }


    /**
     * 删除分类
     */
    public Boolean delete(Long id) {
        if (ObjectUtils.isEmpty(id)) return false;

        int res = categoryMapper.deleteByPrimaryKey(id);
        if (res != 1) return false;

        return true;
    }
}
