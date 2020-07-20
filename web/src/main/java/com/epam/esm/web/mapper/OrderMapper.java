package com.epam.esm.web.mapper;

import com.epam.esm.repository.entity.Order;
import com.epam.esm.web.dto.OrderDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto orderToOrderDto(Order order);
    Order orderDtoToOrder(OrderDto orderDto);
}
