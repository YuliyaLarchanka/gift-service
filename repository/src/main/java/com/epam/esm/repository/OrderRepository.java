package com.epam.esm.repository;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Page;

import java.util.Optional;

public interface OrderRepository extends ApiRepository<Order, Long> {
    Page<Order> findOrdersByAccountId(Long id, int page, int size);

    Optional<Order> findPriceAndTimestampOfOrder(Long accountId, Long orderId);

    Page<Order> findHighestPriceOrder(Long accountId);
}
