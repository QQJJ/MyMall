package com.mall.austen.controller;

import com.mall.austen.entity.Item;
import com.mall.austen.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:30
 */
@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("toList")
    public String toList(Model model){
        List<Item> list = itemService.list();
        model.addAttribute("ItemList", list);
        return "item";
    }
}
