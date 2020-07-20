package com.epam.esm.repository;

import com.epam.esm.repository.entity.Order;

import java.util.List;

public interface OrderRepository extends ApiRepository<Order, Long> {
    List<Order> findOrdersByAccountId(Long id);
}
