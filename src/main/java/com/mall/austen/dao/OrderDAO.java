package com.mall.austen.dao;

import com.mall.austen.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:33
 */
@Repository
public interface OrderDAO {

    boolean save(Order order);

    List<Order> list();

    Order getById(Integer id);

    boolean editStatus(@Param("orderNo")String orderNo, @Param("status")Integer status);

    boolean editStatusAndTradeNo(@Param("orderNo")String orderNo, @Param("status")Integer status, @Param("tradeNo")String tradeNo);

}
