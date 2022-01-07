package com.yfvia.wiki.controller;

import com.yfvia.wiki.domain.Test;
import com.yfvia.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    TestService testService;


    //    启动时，SpringBoot扫描到@Value注解，就回去配置文件读取配置信息，没读到就是默认值TEST
    @Value("${test.hello:TEST}")
    private String testHello;

    @GetMapping("/hello")
    public String hello() {
        return testHello;
    }


    @PostMapping("/hello/post")
    public String postHello(String name) {
        return "Hello via!" + name;
    }

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }
}
