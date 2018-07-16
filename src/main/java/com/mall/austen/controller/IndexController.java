package com.mall.austen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:29
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping
    public String toIndex(){
        return "index";
    }
}
