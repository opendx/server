package com.daxiang.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jiangyitao.
 */
@Controller
public class HomeController {

    @Value("redirect:/${frontend}/index.html")
    private String url;

    @GetMapping("/")
    public String index() {
        return url;
    }
}
