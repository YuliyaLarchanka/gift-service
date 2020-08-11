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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
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
        long accountId = order.getAccount().getId();
        Optional<Account> accountOptional = accountRepository.findById(accountId, Account.class);
        if (accountOptional.isEmpty()){
            throw new EntityNotFoundException("Account with such id is not found");
        }
        order.setAccount(accountOptional.get());

        List<Certificate> certificates = order.getCertificates();
        List<Certificate> filteredCertificates;
        List<Certificate> fetchedCertificates;
        BigDecimal price = new BigDecimal(0);

        if (!certificates.isEmpty()) {
            filteredCertificates = certificates.stream()
                    .filter(distinctByKey(Certificate::getId))
                    .collect(Collectors.toList());

                    fetchedCertificates = filteredCertificates
                            .stream()
                            .map(Certificate::getId)
                    .map(id-> certificateRepository.findById(id, Certificate.class) )
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(certificate -> !certificate.isDeleted())
                    .collect(Collectors.toList());

            if (filteredCertificates.size()>fetchedCertificates.size()){
                throw new EntityNotFoundException("Certificate with such id is not found");
            }

            order.setCertificates(fetchedCertificates);
            price = fetchedCertificates.stream()
                    .map(Certificate::getPrice)
                    .reduce(BigDecimal::add).get();
        }

        order.setPrice(price);
        return repository.create(order);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor){
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
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

         Page<Order> page = repository.findOrdersByAccountId(accountId, 1, 3);
        if (page.getContent().size()==0){
            throw new EntityNotFoundException("Account doesn't have orders");
        }

         Optional<Order> orderOptional = repository.findPriceAndTimestampOfOrder(accountId, orderId);
         if(orderOptional.isEmpty()){
             throw new EntityNotFoundException("Account doesn't have such order");
         }
         return orderOptional.get();
    }

    @Override
    public Page<Order> findHighestPriceOrder(Long id){
        Page<Order> page = repository.findOrdersByAccountId(id, 1, 3);
        if (page.getContent().size()==0){
            throw new EntityNotFoundException("Account doesn't have orders");
        }

        Page<Order> orderPage = repository.findHighestPriceOrder(id);
        if(orderPage.getContent().size() == 0){
            throw new EntityNotFoundException("Account doesn't have orders");
        }
        return orderPage;
    }
}
