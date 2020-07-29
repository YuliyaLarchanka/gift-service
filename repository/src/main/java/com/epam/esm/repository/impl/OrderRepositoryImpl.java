package com.epam.esm.repository.impl;

import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl extends ApiRepositoryImpl<Order, Long> implements OrderRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    protected String getClassName() {
        return "Order";
    }

    @Override
    public Order create(Order order) {
        em.persist(order);
        return order;
    }

    @Override
    public Optional<Order> update(Order order) {
        return Optional.of(new Order());
    }

    @Override
    public Page<Order> findOrdersByAccountId(Long id, int page, int size) {
        String sql = "SELECT o FROM Order o WHERE o.account.id = ?1";

        Query query = em.createQuery(sql, Order.class);
        List<Order> totalList = (List<Order>) query.setParameter(1, id).getResultList().stream().collect(Collectors.toList());
        int totalCount = totalList.size();
        Page<Order> filledPage = fillPage(page, size, totalCount);
        List<Order> ordersPerPage = query.setFirstResult(filledPage.getOffset()).setMaxResults(size).getResultList();
        filledPage.setContent(ordersPerPage);
        return filledPage;
    }

    @Override
    public Optional<Order> findPriceAndTimestampOfOrder(Long accountId, Long orderId){
        Optional<Order> orderOptional = findById(orderId, Order.class);
        if(orderOptional.isEmpty()){
            return Optional.empty();
        }

        String sql = "SELECT o FROM Order o WHERE o.account.id = ?1 and o.id = ?2";
        Query query = em.createQuery(sql, Order.class);
        Order order =  (Order) query.setParameter(1, accountId).setParameter(2, orderId).getSingleResult();
        return Optional.of(order);
    }

    @Override
    public Page<Order> findHighestPriceOrder(Long id){
        String sql = "SELECT o FROM Order o WHERE o.price = (SELECT MAX(x.price) " +
                "FROM Order x WHERE x.account.id = ?1)";
        Query query = em.createQuery(sql, Order.class);
        Page<Order> page = new Page<>();
        try {
            Order order =  (Order) query.setParameter(1, id).setMaxResults(1).getSingleResult();
            List<Order> orders = new ArrayList<>();
            orders.add(order);
            page.setContent(orders);
        } catch (NoResultException e) {
            return page;
        }
        return page;
    }
}
