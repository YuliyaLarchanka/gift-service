package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final CertificateService certificateService;

    public OrderServiceImpl(OrderRepository orderRepository, AccountRepository accountRepository,
                             CertificateService certificateService) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.certificateService = certificateService;
    }

    @Override
    @Transactional
    public Order create(Order order) {
        order.setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));

        List<Certificate> certificates = order.getCertificates();
        List<Certificate> fetchedCertificates;
        BigDecimal price = new BigDecimal(0);

        if (!certificates.isEmpty()) {
            fetchedCertificates = certificates.stream()
                    .map(Certificate::getId)
                    .map(certificateService::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            if (certificates.size()>fetchedCertificates.size()){
                throw new RuntimeException();//TODO: create exception
            }

            order.setCertificates(fetchedCertificates);
            price = fetchedCertificates.stream()
                    .map(Certificate::getPrice)
                    .reduce(BigDecimal::add).get();
        }

        order.setPrice(price);
        Optional<Account> accountOptional = accountRepository.findById(order.getAccount().getId());
        if (accountOptional.isEmpty()){
            throw new RuntimeException();//TODO: create exception
        }
        order.setAccount(accountOptional.get());
        return orderRepository.create(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.of(new Order());

    }

    @Override
    public Page<Order> findAll(int page, int size) {
        return new Page<>();
    }

    @Override
    public Optional<Order> update(Order order) {
        return Optional.of(new Order());
    }

    @Override
    public void delete(Long id) {
        //
    }

    @Override
    public List<Order> findOrdersByAccountId(Long id) {
        return orderRepository.findOrdersByAccountId(id);
    }

    @Override
    public Order findPriceAndTimestampOfOrder(Long accountId, Long orderId){
         Optional<Order> orderOptional = orderRepository.findPriceAndTimestampOfOrder(accountId, orderId);
         if(orderOptional.isEmpty()){
             throw new RuntimeException();//TODO
         }
         return orderOptional.get();
    }

    @Override
    public Order findHighestPriceOrder(Long id){
        Optional<Order> orderOptional = orderRepository.findHighestPriceOrder(id);
        if(orderOptional.isEmpty()){
            throw new RuntimeException();//TODO
        }
        return orderOptional.get();
    }
}
