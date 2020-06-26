package com.epam.esm.repository;

import java.util.List;
import java.util.Optional;

public interface ApiRepository<T, ID> {
    T create(T var);

    List<T> findAll();

    Optional<T> findById(ID var);

    T update(T var);

    void delete(ID var);
}
