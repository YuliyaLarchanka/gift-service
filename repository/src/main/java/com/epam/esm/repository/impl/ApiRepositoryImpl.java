package com.epam.esm.repository.impl;

import com.epam.esm.repository.ApiRepository;
import com.epam.esm.repository.entity.Page;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public abstract class ApiRepositoryImpl<T, ID> implements ApiRepository<T, ID> {
    @PersistenceContext
    private EntityManager em;

    protected abstract String getClassName();

    @Override
    public Optional<T> findById(ID id, Class<T> clazz) {
        try {
            T var = em.find(clazz, id);
            return Optional.ofNullable(var);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<T> findAll(int pageNumber, int size) {
        String className = getClassName();
        int totalCount = ((Long)em.createQuery("select count(t) from " + className + " t").getSingleResult()).intValue();
        Page<T> filledPage = fillPage(pageNumber,  size, totalCount);
        Query query = em.createQuery("select t from " + className + " t");
        List<T> list = query.setFirstResult(filledPage.getOffset()).setMaxResults(size).getResultList();
        filledPage.setContent(list);
        return filledPage;
    }

    @Override
    public Page<T> filteredFindAll(int pageNumber, int size) {
        String className = getClassName();
        int totalCount = ((Long)em.createQuery("select count(c) from " + className + " c where c.isDeleted = false").getSingleResult()).intValue();
        Page<T> filledPage = fillPage(pageNumber,  size, totalCount);
        Query query = em.createQuery("select c from " + className + " c where c.isDeleted = false");
        List<T> list = query.setFirstResult(filledPage.getOffset()).setMaxResults(size).getResultList();
        filledPage.setContent(list);
        return filledPage;
    }

    protected Page<T> fillPage(int pageNumber, int size, int totalCount){
        int offset = (pageNumber - 1) * size;
        boolean hasNext = totalCount > offset + size;
        boolean hasPrevious = pageNumber > 1;
        Page<T> page = new Page<>();
        page.setTotalCount(totalCount);
        page.setOffset(offset);
        page.setHasNext(hasNext);
        page.setHasPrevious(hasPrevious);
        return page;
    }

    @Override
    public void delete(T var) {
        em.remove(var);
    }
}
