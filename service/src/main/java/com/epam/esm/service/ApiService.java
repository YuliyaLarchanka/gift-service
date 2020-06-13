package com.epam.esm.service;

import java.util.List;
import java.util.Optional;

public interface ApiService<T, ID> {
    T create(T var);

    List<T> findAll();

    Optional<T> findById(ID var);

    Optional<T> update(T var);

    void delete(ID var);
}
