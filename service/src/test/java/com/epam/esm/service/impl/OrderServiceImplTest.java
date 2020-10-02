package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.entity.*;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private Account account;
    private Certificate certificate1;
    private Certificate certificate2;
    private OrderService orderService;
    private Order orderWithoutId;
    private Order orderWithId;
    private Account accountWithRole;
    private Page<Order> orderPage;

    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderServiceImpl(orderRepositoryMock, accountRepositoryMock, certificateRepositoryMock);

        account = prepareAccount();
        List<Order> orders = prepareOrders();
        account.setOrders(orders);
        accountWithRole = account;
        accountWithRole.setRole(RoleEnum.ROLE_CLIENT);

        certificate1 = prepareCertificates().get(0);
        certificate2 = prepareCertificates().get(1);
        orderWithoutId = prepareOrderWithoutId();
        orderWithId = prepareOrders().get(0);
        Order orderWithInvalidId = prepareOrders().get(0);
        orderWithInvalidId.setId(INVALID_ID);
        orderPage = new Page<>();
        orderPage.setContent(prepareOrders());
    }

    private List<Tag> prepareTags() {
        List<Tag> tags = new ArrayList<>();

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("dolls");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("books");

        tags.add(tag1);
        tags.add(tag2);
        return tags;
    }

    private List<Certificate> prepareCertificates() {
        List<Certificate> certificates = new ArrayList<>();
        List<Tag> tags = prepareTags();

        Certificate certificate1 = new Certificate();
        certificate1.setId(VALID_ID);
        certificate1.setName("Toy story");
        certificate1.setDescription("A certificate to buy goods in Toy story shop");
        certificate1.setPrice(new BigDecimal(100));
        certificate1.setDateOfCreation(LocalDateTime.now());
        certificate1.setDurationInDays((short) 10);
        certificate1.setTagList(tags);

        Certificate certificate2 = new Certificate();
        certificate2.setId(2L);
        certificate2.setName("Booklover");
        certificate2.setDescription("A certificate to buy books in Booklover shop");
        certificate2.setPrice(new BigDecimal(150));
        certificate1.setDateOfCreation(LocalDateTime.now());
        certificate1.setDurationInDays((short) 30);
        certificate1.setTagList(tags);

        certificates.add(certificate1);
        certificates.add(certificate2);
        return certificates;
    }

    private Account prepareAccount() {
        Account account = new Account();
        account.setLogin("Bob123");
        account.setPassword("123456");
        account.setId(VALID_ID);
        return account;
    }

    private List<Order> prepareOrders() {
        Account account = prepareAccount();
        List<Certificate> certificates = prepareCertificates();
        List<Order> orders = new ArrayList<>();

        Order order1 = new Order();
        order1.setId(1L);
        order1.setAccount(account);
        order1.setCertificates(certificates);
        order1.setPrice(new BigDecimal(200));
        order1.setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));

        Order order2 = new Order();
        order2.setId(2L);
        order2.setAccount(account);
        order2.setCertificates(certificates);
        order2.setPrice(new BigDecimal(1500));
        order2.setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));

        orders.add(order1);
        orders.add(order2);
        return orders;
    }

    private Order prepareOrderWithoutId(){
        List<Certificate> certificates = prepareCertificates();
        Order order = new Order();
        order.setAccount(account);
        order.setCertificates(certificates);
        order.setPrice(new BigDecimal(200));
        order.setDateOfCreation(LocalDateTime.now(ZoneOffset.UTC));
        return order;
    }

    @Test
    public void create_Order_OK() {
        //given
        when(accountRepositoryMock.findById(account.getId(), Account.class)).thenReturn(Optional.of(accountWithRole));
        doReturn(Optional.of(certificate1)).when(certificateRepositoryMock).findById(certificate1.getId(), Certificate.class);
        doReturn(Optional.of(certificate2)).when(certificateRepositoryMock).findById(certificate2.getId(), Certificate.class);
        when(orderRepositoryMock.create(orderWithoutId)).thenReturn(orderWithId);
        //when
        Order actual = orderService.create(orderWithoutId);
        //then
        verify(accountRepositoryMock, times(1)).findById(account.getId(), Account.class);
        verify(certificateRepositoryMock, times(1)).findById(certificate1.getId(), Certificate.class);
        verify(orderRepositoryMock, times(1)).create(orderWithoutId);
        assertEquals(orderWithId, actual);
    }

    @Test
    public void create_Order_NotFound() {
        //given
        when(accountRepositoryMock.findById(account.getId(), Account.class)).thenThrow(new EntityNotFoundException("Account with such id is not found"));
        String expectedMessage = "Account with such id is not found";
        //when
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> orderService.create(orderWithoutId));
        //then
        verify(accountRepositoryMock, times(1)).findById(account.getId(), Account.class);
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void findOrders_AccountId_OK() {
        //given
        when(orderRepositoryMock.findOrdersByAccountId(account.getId(), page, size)).thenReturn(orderPage);
        //when
        Page<Order> actual = orderService.findOrdersByAccountId(account.getId(), page, size);
        //then
        verify(orderRepositoryMock, times(1)).findOrdersByAccountId(account.getId(), page, size);
        assertEquals(orderPage, actual);
    }

    @Test
    public void findOrders_AccountId_NotFound() {
        //given
        when(orderRepositoryMock.findOrdersByAccountId(account.getId(), page, size)).thenThrow(new EntityNotFoundException("Account doesn't have orders"));
        String expectedMessage = "Account doesn't have orders";

        //when
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> orderService.findOrdersByAccountId(account.getId(), page, size));

        //then
        verify(orderRepositoryMock, times(1)).findOrdersByAccountId(account.getId(), page, size);
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void findPriceAndTimestampOfOrder_AccountIdOrderId_OK() {
        //given
        when(accountRepositoryMock.findById(account.getId(), Account.class)).thenReturn(Optional.of(accountWithRole));
        when(orderRepositoryMock.findPriceAndTimestampOfOrder(account.getId(), orderWithId.getId())).thenReturn(Optional.of(orderWithId));
        //when
        Order actual = orderService.findPriceAndTimestampOfOrder(account.getId(), orderWithId.getId());
        //then
        verify(accountRepositoryMock, times(1)).findById(account.getId(), Account.class);
        verify(orderRepositoryMock, times(1)).findPriceAndTimestampOfOrder(account.getId(), orderWithId.getId());
        assertEquals(orderWithId, actual);
    }

    @Test
    public void findPriceAndTimestampOfOrder_AccountIdOrderId_NotFound() {
        //given
        when(accountRepositoryMock.findById(account.getId(), Account.class)).thenThrow(new EntityNotFoundException("Account with such id is not found"));
        String expectedMessage = "Account with such id is not found";
        //when
        EntityNotFoundException actualException = assertThrows(EntityNotFoundException.class,
                () -> orderService.findPriceAndTimestampOfOrder(account.getId(), orderWithId.getId()));
        //then
        verify(accountRepositoryMock, times(1)).findById(account.getId(), Account.class);
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void findHighestPriceOrder_Id_OK() {
        //given
        when(orderRepositoryMock.findHighestPriceOrder(account.getId())).thenReturn(orderPage);
        //when
        Page<Order> actual = orderService.findHighestPriceOrder(account.getId());
        //then
        verify(orderRepositoryMock, times(1)).findHighestPriceOrder(account.getId());
        assertEquals(orderPage, actual);
    }
}