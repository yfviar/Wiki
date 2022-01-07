package com.yfvia.wiki.service;

import com.yfvia.wiki.domain.Demo;
import com.yfvia.wiki.domain.DemoExample;
import com.yfvia.wiki.domain.Ebook;
import com.yfvia.wiki.domain.EbookExample;
import com.yfvia.wiki.mapper.DemoMapper;
import com.yfvia.wiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {
    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(new EbookExample());
    }
}
