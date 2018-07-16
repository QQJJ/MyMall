package com.mall.austen.service.impl;

import com.mall.austen.dao.ItemDAO;
import com.mall.austen.dao.OrderDAO;
import com.mall.austen.dao.OrderItemDAO;
import com.mall.austen.entity.Item;
import com.mall.austen.entity.Order;
import com.mall.austen.entity.OrderItem;
import com.mall.austen.entity.ResultVO;
import com.mall.austen.service.OrderService;
import com.mall.austen.util.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/7/10 17:32
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private ItemDAO itemDAO;
    @Autowired
    private OrderItemDAO orderItemDAO;

    @Override
    public List<Order> list() {
        return orderDAO.list();
    }

    @Override
    public ResultVO save(Item item) {
        ResultVO resultVO = new ResultVO();
        item = itemDAO.getById(item.getId());
        if(Objects.isNull(item)){
            resultVO.setCode(0);
            resultVO.setMsg("商品不存在");
        }else{
            Double totalAmount = item.getPrice();
            Order order = new Order();
            order.setAmount(totalAmount);
            order.setCreaterId(1);
            order.setCreaterName("admin");
            order.setOrderNo(OrderUtils.getOrderNo());
            List<Item> list = new ArrayList<>();
            list.add(item);

            boolean flag = orderDAO.save(order);
            if(!flag){
                resultVO.setCode(0);
                resultVO.setMsg("下单失败");
            }else{
                // 订单商品
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setCreateId(1);
                orderItem.setItemId(item.getId());
                orderItem.setItemName(item.getName());
                orderItem.setItemPrice(item.getPrice());
                orderItemDAO.save(orderItem);
                resultVO.setCode(1);
                resultVO.setData(order);
            }
        }
        return resultVO;
    }
}
