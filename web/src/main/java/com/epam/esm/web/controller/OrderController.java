package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.service.AccountService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.PageDto;
import com.epam.esm.web.mapper.OrderMapper;
import com.epam.esm.web.mapper.PageMapper;
import com.epam.esm.web.validator.annotation.ValidOrder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@Validated
public class OrderController extends ApiController<Order, OrderDto>{
    private final OrderService orderService;
    private final AccountService accountService;
    private final OrderMapper orderMapper;
    private static final String PAGE_PATH = "http://localhost:8080/orders?page=";
    private static final String PATH = "http://localhost:8080/orders/";

    public OrderController(OrderService orderService, AccountService accountService, OrderMapper orderMapper, PageMapper<Order, OrderDto> pageMapper) {
        super(pageMapper);
        this.orderService = orderService;
        this.accountService = accountService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/orders")
        public OrderDto create(@ValidOrder @RequestBody OrderDto orderDto) {
       Order order = orderMapper.orderDtoToOrder(orderDto);
       Order createdOrder = orderService.create(order);
        return orderMapper.orderToOrderDto(createdOrder);
    }

    @GetMapping("/orders/{id}")
    public OrderDto findById(@PathVariable @Valid @Min(value = 1, message = "Id should be greater than zero") Long id) {
        return orderService.findById(id, Order.class).map(orderMapper::orderToOrderDto)
                .orElseThrow(() -> new EntityNotFoundException("Order with such id is not found"));
    }

    @GetMapping("/orders")
    public PageDto<OrderDto> findOrders(@RequestParam(defaultValue = "1") @Min(value = 1, message = "page number should be greater than zero") int page,
                                        @RequestParam(defaultValue = "3") @Min(value = 1, message = "size should be greater than zero") int size) {
        Page<Order> orderPage = orderService.findAll(page, size);
        Page<Order> filledPage = fillPageLinks(orderPage, page, size, PAGE_PATH);
        return convertPageToPageDto(filledPage, orderMapper::orderToOrderDto, PATH);
    }

    @GetMapping("accounts/{accountId}/orders")
    public PageDto<OrderDto> findOrdersByAccountId(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long accountId,
                                                   @RequestParam(required = false) String filter,
                                                   @RequestParam(defaultValue = "1") @Min(value = 1, message = "page number should be greater than zero") int page,
                                                   @RequestParam(defaultValue = "3") @Min(value = 1, message = "size should be greater than zero") int size) {

        Optional<Account> accountOptional = accountService.findById(accountId, Account.class);
        if (accountOptional.isEmpty()){
            throw new EntityNotFoundException("Account with such id is not found");
        }

        if (filter!=null && filter.equalsIgnoreCase("highestPrice")){
            Page<Order> orderPage = orderService.findHighestPriceOrder(accountId);
            Page<Order> filledPage = fillPageLinks(orderPage, page, size, PAGE_PATH);
            return convertPageToPageDto(filledPage, orderMapper::orderToOrderDto, PATH);
        }

        Page<Order> orderPage = orderService.findOrdersByAccountId(accountId, page, size);
        Page<Order> filledPage = fillPageLinks(orderPage, page, size, PAGE_PATH);
        return convertPageToPageDto(filledPage, orderMapper::orderToOrderDto, PATH);
    }

    @GetMapping("accounts/{accountId}/orders/{orderId}")
        public OrderDto findPriceAndTimestampOfOrder(@PathVariable long accountId, @PathVariable long orderId){
            Order order = orderService.findPriceAndTimestampOfOrder(accountId, orderId);
            return orderMapper.orderToOrderDtoPriceTimestamp(order);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable @Valid @Min(value = 1, message = "Id should be greater than zero") Long id) {
        orderService.delete(id, Order.class);
    }
}
