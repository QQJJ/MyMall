package com.mall.austen.dao;

import com.mall.austen.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:33
 */
@Repository
public interface ItemDAO {

    Item getById(Integer id);

    List<Item> list();
}
