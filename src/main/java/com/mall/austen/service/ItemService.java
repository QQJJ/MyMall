package com.mall.austen.service;

import com.mall.austen.entity.Item;
import com.mall.austen.entity.ResultVO;

import java.util.List;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:31
 */
public interface ItemService {

    Item getById(Integer id);

    List<Item> list();
}
