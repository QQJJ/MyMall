package com.mall.austen.controller;

import com.mall.austen.entity.Item;
import com.mall.austen.entity.ResultVO;
import com.mall.austen.service.ItemService;
import com.mall.austen.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:30
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("save")
    @ResponseBody
    private ResultVO save(Item item){
        return orderService.save(item);
    }

    @RequestMapping("list")
    private String list(Model model){
        model.addAttribute("OrderList", orderService.list());
        return "order";
    }



}
