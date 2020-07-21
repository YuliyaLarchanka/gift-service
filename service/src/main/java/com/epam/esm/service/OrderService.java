package com.epam.esm.service;

import com.epam.esm.repository.entity.Order;

import java.util.List;

public interface OrderService extends ApiService<Order, Long>{
    List<Order> findOrdersByAccountId(Long id);

    Order findPriceAndTimestampOfOrder(Long accountId, Long orderId);

    Order findHighestPriceOrder(Long accountId);
}
