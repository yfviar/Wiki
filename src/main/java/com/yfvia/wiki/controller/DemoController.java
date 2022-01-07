package com.yfvia.wiki.controller;

import com.yfvia.wiki.domain.Demo;
import com.yfvia.wiki.domain.Test;
import com.yfvia.wiki.service.DemoService;
import com.yfvia.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Autowired
    DemoService demoService;

    @GetMapping("/demo/list")
    public List<Demo> list() {
        return demoService.list();
    }
}
