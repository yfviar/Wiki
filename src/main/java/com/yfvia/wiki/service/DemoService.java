package com.yfvia.wiki.service;

import com.yfvia.wiki.domain.Demo;
import com.yfvia.wiki.domain.DemoExample;
import com.yfvia.wiki.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {
    @Autowired
    private DemoMapper demoMapper;

    public List<Demo> list() {
        return demoMapper.selectByExample(new DemoExample());
    }
}
