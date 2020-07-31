package com.epam.esm.service;

import com.epam.esm.repository.entity.Page;

import java.util.Optional;

public interface ApiService<T, ID> {
    T create(T var);

    Page<T> findAll(int page, int size);

    Optional<T> findById(ID var, Class<T> clazz);

    Optional<T> update(T var);

    void delete(ID var, Class<T> clazz);
}
