package com.epam.esm.web.controller;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.mapper.OrderMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
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

    @PostMapping("/orders")
        public OrderDto create(@Valid @RequestBody OrderDto orderDto) {
       Order order = orderMapper.orderDtoToOrder(orderDto);
       Order createdOrder = orderService.create(order);
        return orderMapper.orderToOrderDto(createdOrder);
    }

    @GetMapping("accounts/{accountId}/orders")
    public List<OrderDto> findOrdersByAccountId(@PathVariable @Min(value = 1, message = "Id should be greater than zero") long accountId,
                                                @RequestParam(required = false) String filter) {

        if (filter!=null && filter.equalsIgnoreCase("highestPrice")){
            Order order = orderService.findHighestPriceOrder(accountId);
            List<OrderDto> orderDtos= new ArrayList<>();
            orderDtos.add(orderMapper.orderToOrderDtoPriceTimestamp(order));
            return orderDtos;
        }
        return orderService.findOrdersByAccountId(accountId).stream().map(orderMapper::orderToOrderDto).collect(Collectors.toList());
    }

    @GetMapping("accounts/{accountId}/orders/{orderId}")
        public OrderDto findPriceAndTimestampOfOrder(@PathVariable long accountId, @PathVariable long orderId){
            Order order = orderService.findPriceAndTimestampOfOrder(accountId, orderId);
            return orderMapper.orderToOrderDtoPriceTimestamp(order);
    }
}