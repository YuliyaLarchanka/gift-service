package com.epam.esm.repository;

import com.epam.esm.repository.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends ApiRepository<Order, Long> {
    List<Order> findOrdersByAccountId(Long id);

    Optional<Order> findPriceAndTimestampOfOrder(Long accountId, Long orderId);
}
