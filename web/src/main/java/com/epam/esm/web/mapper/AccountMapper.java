package com.epam.esm.web.mapper;

import com.epam.esm.repository.entity.Account;
import com.epam.esm.web.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "links", ignore = true)
    AccountDto accountToAccountDto(Account account);
    @Mapping(target = "orders", ignore = true)
    Account accountDtoToAccount(AccountDto accountDto);
}
