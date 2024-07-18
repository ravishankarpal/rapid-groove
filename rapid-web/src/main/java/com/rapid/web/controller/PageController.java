package com.rapid.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller

@Slf4j

public class PageController {

    @GetMapping("/")
    public String homePage() {
        log.info("Home Page loading");
        return "index";
    }
    @GetMapping("/about")
    public String aboutPage(){
        log.info("About Page loading");
        return "about";
    }
    @RequestMapping("/index")
    public String indexPage(){
        log.info("About Page loading");
        return "index";
    }
}
