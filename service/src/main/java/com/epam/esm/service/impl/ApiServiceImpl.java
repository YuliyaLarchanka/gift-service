package com.epam.esm.service.impl;

import com.epam.esm.repository.ApiRepository;
import com.epam.esm.repository.entity.Page;
import com.epam.esm.service.ApiService;
import com.epam.esm.service.exception.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public abstract class ApiServiceImpl<T, ID, H extends ApiRepository<T, ID>> implements ApiService<T, ID> {
    protected final H repository;

    public ApiServiceImpl(H apiRepository) {
        this.repository = apiRepository;
    }

    @Override
    public Optional<T> findById(ID id, Class<T> clazz) {
        return repository.findById(id, clazz);
    }

    @Override
    public Page<T> findAll(int page, int size){
        return repository.findAll(page, size);
    }

    @Override
    @Transactional
    public void delete(ID id, Class<T> clazz) {
        Optional<T> optional = repository.findById(id, clazz);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException(clazz.getSimpleName() + " with such id is not found");
        }
        repository.delete(optional.get());
    }
}
