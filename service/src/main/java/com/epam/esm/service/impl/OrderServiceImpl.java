package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Certificate;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ApiServiceImpl<Order, Long, OrderRepository> implements OrderService {
    private final AccountRepository accountRepository;
    private final CertificateRepository certificateRepository;

    public OrderServiceImpl(OrderRepository orderRepository, AccountRepository accountRepository,
                            CertificateRepository certificateRepository) {
        super(orderRepository);
        this.accountRepository = accountRepository;
        this.certificateRepository = certificateRepository;
    }

    @Override
    @Transactional
    public Order create(Order order) {
        Account account = findAccount(order);
        order.setAccount(account);
        List<Certificate> certificates = new ArrayList<>(new HashSet<>(order.getCertificates()));
        BigDecimal price = new BigDecimal(0);
        if (!certificates.isEmpty()) {
            price = fetchPrice(order, certificates, price);
        }
        order.setPrice(price);
        return repository.create(order);
    }

    private Account findAccount(Order order){
        long accountId = order.getAccount().getId();
        Optional<Account> accountOptional = accountRepository.findById(accountId, Account.class);
        if (accountOptional.isEmpty()){
            throw new EntityNotFoundException("Account with such id is not found");
        }
        return accountOptional.get();
    }


    private BigDecimal fetchPrice(Order order,  List<Certificate> certificates, BigDecimal price){
        certificates = fetchExistedCertificates(certificates);
        order.setCertificates(certificates);
        return calcPriceOfCertificates(certificates);
    }

    private List<Certificate> fetchExistedCertificates(List<Certificate> certificates){
        return certificates
                .stream()
                .map(Certificate::getId)
                .map(id-> certificateRepository.findById(id, Certificate.class) )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(certificate -> !certificate.isDeleted())
                .collect(Collectors.toList());
    }

    private BigDecimal calcPriceOfCertificates(List<Certificate> certificates){
        return certificates.stream()
                .map(Certificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Optional<Order> update(Order order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Order> findOrdersByAccountId(Long id, int pageNumber, int size) {
        Page<Order> page = repository.findOrdersByAccountId(id, pageNumber, size);
        if (page.getContent().size()==0){
            throw new EntityNotFoundException("Account doesn't have orders");
        }
        return page;
    }

    @Override
    public Order findPriceAndTimestampOfOrder(Long accountId, Long orderId){
        Optional<Account> accountOptional = accountRepository.findById(accountId, Account.class);
        if(accountOptional.isEmpty()){
            throw new EntityNotFoundException("Account with such id is not found");
        }
         Optional<Order> orderOptional = repository.findPriceAndTimestampOfOrder(accountId, orderId);
         if(orderOptional.isEmpty()){
             throw new EntityNotFoundException("Account doesn't have such order");
         }
         return orderOptional.get();
    }

    @Override
    public Page<Order> findHighestPriceOrder(Long id){
        Page<Order> orderPage = repository.findHighestPriceOrder(id);
        if(orderPage.getContent().size() == 0){
            throw new EntityNotFoundException("Account doesn't have orders");
        }
        return orderPage;
    }
}
