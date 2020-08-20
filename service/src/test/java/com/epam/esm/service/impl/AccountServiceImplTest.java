package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.RoleEnum;
import com.epam.esm.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {
    private final static long VALID_ID = 1L;
    private final static long INVALID_ID = 10000000L;


    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    private AccountService accountService;
    private AccountService accountServiceSpy;
    private Account account1;
    private Account accountWithId;
    private String encoded;



    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(accountRepositoryMock, passwordEncoderMock);
        accountServiceSpy = spy(accountService);

        account1 = new Account();
        account1.setLogin("Bob123");
        account1.setPassword("123456");

        Account account2 = new Account();
        account2.setLogin("Joe123");
        account1.setPassword("123456");

        accountWithId = account1;
        accountWithId.setId(VALID_ID);
        accountWithId.setRole(RoleEnum.ROLE_CLIENT);

        Account accountWithInvalidId = account1;
        accountWithInvalidId.setId(INVALID_ID);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);

        encoded = "123456";
    }

    @Test
    public void create_Account_OK() {
        when(accountRepositoryMock.findByLogin(account1.getLogin())).thenReturn(Optional.empty());
        when(passwordEncoderMock.encode(account1.getPassword())).thenReturn(encoded);
        when(accountRepositoryMock.create(accountWithId)).thenReturn(accountWithId);
        //when
        Account actual = accountServiceSpy.create(account1);
        //then
        verify(accountRepositoryMock, times(1)).findByLogin(account1.getLogin());
        verify(passwordEncoderMock, times(1)).encode(account1.getPassword());
        verify(accountRepositoryMock, times(1)).create(accountWithId);
        assertEquals(accountWithId, actual);
    }

    @Test
    public void find_Login_OK() {
        when(accountRepositoryMock.findByLogin(account1.getLogin())).thenReturn(Optional.of(accountWithId));
        //when
        Optional<Account> actual = accountService.findByLogin(account1.getLogin());
        //then
        verify(accountRepositoryMock, times(1)).findByLogin(account1.getLogin());
        assertEquals(Optional.of(accountWithId), actual);
    }
}
