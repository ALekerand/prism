package com.dcspa.prism.repositorybase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import java.io.Serializable;

public class BaseRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(
            JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager) {

        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public void refresh(T entity) {
        entityManager.refresh(entity);
    }

    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }
}