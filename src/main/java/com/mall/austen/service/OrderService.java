package com.mall.austen.service;

import com.mall.austen.entity.Item;
import com.mall.austen.entity.Order;
import com.mall.austen.entity.ResultVO;

import java.util.List;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:32
 */
public interface OrderService {

    List<Order> list();

    ResultVO save(Item item);
}
