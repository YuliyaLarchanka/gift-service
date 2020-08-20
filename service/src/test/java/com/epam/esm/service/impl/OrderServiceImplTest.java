package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.entity.*;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class OrderServiceImplTest {
    private final static long VALID_ID = 1L;
    private final static long INVALID_ID = 10000000L;
    private final static int page = 1;
    private final static int size = 3;

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private CertificateRepository certificateRepositoryMock;

    private OrderService orderService;
    private Order order1;
    private Order order2;
    private Order orderWithId;
    private List<Order> orders = new ArrayList<>();
    private Order orderWithInvalidId;
    private Account account;
    private Account accountWithRole;
    private Certificate certificate1;
    private Certificate certificate2;
    private Page<Order> orderPage;


    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderServiceImpl(orderRepositoryMock, accountRepositoryMock, certificateRepositoryMock);

        account = new Account();
        account.setLogin("Bob123");
        account.setPassword("123456");
        account.setId(VALID_ID);

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("dolls");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("books");
        List<Tag> tags = new ArrayList<>();

        certificate1 = new Certificate();
        certificate1.setId(VALID_ID);
        certificate1.setName("Toy story");
        certificate1.setDescription("A certificate to buy goods in Toy story shop");
        certificate1.setPrice(new BigDecimal(100));
        certificate1.setDateOfCreation(LocalDateTime.now());
        certificate1.setDurationInDays((short) 10);
        certificate1.setTagList(tags);

        certificate2 = new Certificate();
        certificate1.setId(2L);
        certificate1.setName("Toy story");
        certificate1.setDescription("A certificate to buy goods in Toy story shop");
        certificate1.setPrice(new BigDecimal(100));
        certificate1.setDateOfCreation(LocalDateTime.now());
        certificate1.setDurationInDays((short) 10);
        certificate1.setTagList(tags);

        List<Certificate> certificates = new ArrayList<>();
        certificates.add(certificate1);
        certificates.add(certificate1);

        order1 = new Order();
        order1.setAccount(account);
        order1.setCertificates(certificates);
        order1.setPrice(new BigDecimal(200));
        order1.setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));

        orders.add(order1);
        orderWithId = order1;
        orderWithId.setId(VALID_ID);
        orderWithInvalidId = order1;
        orderWithInvalidId.setId(INVALID_ID);

        orderPage = new Page<>();
        orderPage.setContent(orders);

        account.setOrders(orders);
        accountWithRole = account;
        accountWithRole.setRole(RoleEnum.ROLE_CLIENT);
    }

    @Test
    public void create_Order_OK() {
        when(accountRepositoryMock.findById(account.getId(), Account.class)).thenReturn(Optional.of(accountWithRole));
        doReturn(Optional.of(certificate1)).when(certificateRepositoryMock).findById(certificate1.getId(), Certificate.class);
        doReturn(Optional.of(certificate2)).when(certificateRepositoryMock).findById(certificate2.getId(), Certificate.class);
        when(orderRepositoryMock.create(order1)).thenReturn(orderWithId);
        //when
        Order actual = orderService.create(order1);
        //then
        verify(accountRepositoryMock, times(1)).findById(account.getId(), Account.class);
        verify(certificateRepositoryMock, times(1)).findById(certificate1.getId(), Certificate.class);
        verify(orderRepositoryMock, times(1)).create(order1);
        assertEquals(orderWithId, actual);
    }

    @Test
    public void findOrders_AccountId_OK() {
        when(orderRepositoryMock.findOrdersByAccountId(account.getId(), page, size)).thenReturn(orderPage);
        //when
        Page<Order> actual = orderService.findOrdersByAccountId(account.getId(), page, size);
        //then
        verify(orderRepositoryMock, times(1)).findOrdersByAccountId(account.getId(), page, size);
        assertEquals(orderPage, actual);
    }

    @Test
    public void findPriceAndTimestampOfOrder_AccountIdOrderId_OK() {
        when(accountRepositoryMock.findById(account.getId(), Account.class)).thenReturn(Optional.of(accountWithRole));
        when(orderRepositoryMock.findOrdersByAccountId(account.getId(), 1, 3)).thenReturn(orderPage);
        when(orderRepositoryMock.findPriceAndTimestampOfOrder(account.getId(), order1.getId())).thenReturn(Optional.of(order1));
        //when
        Order actual = orderService.findPriceAndTimestampOfOrder(account.getId(), order1.getId());
        //then
        verify(orderRepositoryMock, times(1)).findOrdersByAccountId(account.getId(), page, size);
        assertEquals(order1, actual);
    }
}
