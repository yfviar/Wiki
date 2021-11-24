package com.yfvia.wiki.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello via!";
    }

    @PostMapping("/hello/post")
    public String postHello(String name) {
        return "Hello via!" + name;
    }
}
