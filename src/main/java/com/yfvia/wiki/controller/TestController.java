package com.yfvia.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

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
}
