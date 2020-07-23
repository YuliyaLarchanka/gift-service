package com.epam.esm.repository;

import com.epam.esm.repository.entity.Page;

import java.util.Optional;

public interface ApiRepository<T, ID>{
    T create(T var);

    Page findAll(int page, int size);

    Optional<T> findById(ID var);

    Optional<T> update(T var);

    void delete(T var);
}
