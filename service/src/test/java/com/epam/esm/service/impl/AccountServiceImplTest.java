package com.epam.esm.service.impl;

import com.epam.esm.repository.AccountRepository;
import com.epam.esm.repository.entity.Account;
import com.epam.esm.repository.entity.RoleEnum;
import com.epam.esm.repository.entity.Tag;
import com.epam.esm.service.AccountService;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {
    private final static long VALID_ID = 1L;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    private AccountService accountService;
    private AccountService accountServiceSpy;
    private Account account;
    private Account accountWithId;
    private String encoded;

    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.initMocks(this);
        accountService = new AccountServiceImpl(accountRepositoryMock, passwordEncoderMock);
        accountServiceSpy = spy(accountService);

        account = prepareAccount("Bob123", "123456");
        accountWithId = account;
        accountWithId.setId(VALID_ID);
        accountWithId.setRole(RoleEnum.ROLE_CLIENT);

        encoded = "123456";
    }

    private Account prepareAccount(String login, String password) {
        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);
        return account;
    }

    @Test
    public void create_Account_OK() {
        when(accountRepositoryMock.findByLogin(account.getLogin())).thenReturn(Optional.empty());
        when(passwordEncoderMock.encode(account.getPassword())).thenReturn(encoded);
        when(accountRepositoryMock.create(accountWithId)).thenReturn(accountWithId);
        //when
        Account actual = accountServiceSpy.create(account);
        //then
        verify(accountRepositoryMock, times(1)).findByLogin(account.getLogin());
        verify(passwordEncoderMock, times(1)).encode(account.getPassword());
        verify(accountRepositoryMock, times(1)).create(accountWithId);
        assertEquals(accountWithId, actual);
    }

    @Test
    public void create_Account_DuplicateException() {
        //given
        when(accountRepositoryMock.findByLogin(account.getLogin())).thenThrow(new DuplicateEntityException("Account with the same login already exists"));
        String expectedMessage = "Account with the same login already exists";
        //when
        DuplicateEntityException actualException = assertThrows(DuplicateEntityException.class,
                () -> accountServiceSpy.create(account));
        //then
        verify(accountRepositoryMock, times(1)).findByLogin(account.getLogin());
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void findByLogin_Login_OK() {
        when(accountRepositoryMock.findByLogin(account.getLogin())).thenReturn(Optional.of(accountWithId));
        //when
        Optional<Account> actual = accountService.findByLogin(account.getLogin());
        //then
        verify(accountRepositoryMock, times(1)).findByLogin(account.getLogin());
        assertEquals(Optional.of(accountWithId), actual);
    }
}
