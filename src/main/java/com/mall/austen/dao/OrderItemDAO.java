package com.mall.austen.dao;

import com.mall.austen.entity.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:33
 */
@Repository
public interface OrderItemDAO {

    OrderItem getById(Integer id);

    boolean save(OrderItem orderItem);

    List<OrderItem> getByOrderId(Integer id);
}
