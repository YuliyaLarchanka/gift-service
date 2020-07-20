package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.mapper.OrderMapper;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
        public OrderDto create(@Valid @RequestBody OrderDto orderDto) {
       Order order = orderMapper.orderDtoToOrder(orderDto);
       Order createdOrder = orderService.create(order);
        return orderMapper.orderToOrderDto(createdOrder);
    }

    @GetMapping("accounts/{accountId}/orders")
    public List<OrderDto> findOrdersByAccountId(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long accountId) {
        return orderService.findOrdersByAccountId(accountId).stream().map(orderMapper::orderToOrderDto).collect(Collectors.toList());
    }
}
