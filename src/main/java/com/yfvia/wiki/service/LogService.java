package com.yfvia.wiki.service;

import com.yfvia.wiki.utils.SnowFlake;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LogService {
    @Resource
    private SnowFlake snowFlake;


    public void addLog() {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
    }
}
