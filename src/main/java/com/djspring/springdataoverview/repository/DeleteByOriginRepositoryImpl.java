package com.djspring.springdataoverview.repository;

import javax.persistence.EntityManager;

public class DeleteByOriginRepositoryImpl implements DeleteByOriginRepository {
    private final EntityManager entityManager;

    public DeleteByOriginRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteByOriginIs(String origin) {
        entityManager.createNativeQuery("Delete from flight where origin = ?")
                .setParameter(1,origin)
                .executeUpdate();
    }
}
