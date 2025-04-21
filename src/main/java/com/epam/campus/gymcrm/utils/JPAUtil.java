package com.epam.campus.gymcrm.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym_persistence_unit");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
