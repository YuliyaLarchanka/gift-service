package com.epam.esm.repository.impl;

import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.entity.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Order create(Order order) {
        em.persist(order);
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.of(new Order());

    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Optional<Order> update(Order order) {
        return Optional.of(new Order());
    }

    @Override
    public void delete(Order order) {
        //
    }

    @Override
    public List<Order> findOrdersByAccountId(Long id) {
        String sql = "SELECT o FROM Order o WHERE o.account.id = ?1";

        Query query = em.createQuery(sql, Order.class);
        return  (List<Order>) query.setParameter(1, id).getResultList().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findPriceAndTimestampOfOrder(Long accountId, Long orderId){
        String sql = "SELECT o FROM Order o WHERE o.account.id = ?1 and o.id = ?2";

        Query query = em.createQuery(sql, Order.class);
        Order order =  (Order) query.setParameter(1, accountId).setParameter(2, orderId).getSingleResult();
        return Optional.of(order);
    }

    @Override
    public Optional<Order> findHighestPriceOrder(Long id){
        String sql = "SELECT o FROM Order o WHERE o.price = (SELECT MAX(x.price) " +
                "FROM Order x WHERE x.account.id = ?1)";
        Query query = em.createQuery(sql, Order.class);
        Order order =  (Order) query.setParameter(1, id).setMaxResults(1).getSingleResult();
        return Optional.of(order);
    }
}
