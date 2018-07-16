package com.mall.austen.service.impl;

import com.mall.austen.dao.ItemDAO;
import com.mall.austen.dao.OrderDAO;
import com.mall.austen.dao.OrderItemDAO;
import com.mall.austen.entity.Item;
import com.mall.austen.entity.Order;
import com.mall.austen.entity.OrderItem;
import com.mall.austen.entity.ResultVO;
import com.mall.austen.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:31
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDAO itemDAO;

    @Override
    public Item getById(Integer id) {
        return itemDAO.getById(id);
    }

    @Override
    public List<Item> list() {
        return itemDAO.list();
    }

}
