package com.epam.esm.web.mapper;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.Order;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.web.dto.AccountDto;
import com.epam.esm.web.dto.OrderDto;
import com.epam.esm.web.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto orderToOrderDto(Order order);
    Order orderDtoToOrder(OrderDto orderDto);

    default AccountDto accountToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        return  accountDto;
    }

    default OrderDto orderToOrderDtoPriceTimestamp(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setPrice(order.getPrice());
        orderDto.setDateOfCreation(order.getDateOfCreation());
        orderDto.setCertificates(null);
        return orderDto;
    }

    @Mapping(target = "links", ignore = true)
    TagDto tagToTagDto(Tag tag);

    @Mapping(target = "certificates", ignore = true)
    Tag tagDtoToTag(TagDto tagDto);

    @Mapping(target = "orders", ignore = true)
    Account accountDtoToAccount(AccountDto accountDto);
}
