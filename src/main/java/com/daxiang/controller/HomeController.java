package com.daxiang.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jiangyitao.
 */
@Controller
public class HomeController {

    @Value("${frontend}")
    private String frontend;

    @GetMapping("/")
    public String index() {
        return String.format("redirect:/%s/index.html", frontend);
    }
}
