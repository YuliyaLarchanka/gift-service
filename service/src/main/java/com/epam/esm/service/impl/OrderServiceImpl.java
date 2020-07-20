package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final CertificateRepository certificateRepository;
    private final CertificateService certificateService;

    public OrderServiceImpl(OrderRepository orderRepository, AccountRepository accountRepository,
                            CertificateRepository certificateRepository, CertificateService certificateService) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.certificateRepository = certificateRepository;
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
    public List<Order> findAll() {
        return new ArrayList<>();
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
}
