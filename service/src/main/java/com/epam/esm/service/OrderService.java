package com.epam.esm.service;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Page;

public interface OrderService extends ApiService<Order, Long>{
    Page<Order> findOrdersByAccountId(Long id, int page, int size);

    Order findPriceAndTimestampOfOrder(Long accountId, Long orderId);

    Page<Order> findHighestPriceOrder(Long accountId);
}
