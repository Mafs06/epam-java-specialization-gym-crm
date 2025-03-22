package com.epam.campus.gymcrm.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;

@Repository
public class TrainingTypeRepository {

    public Optional<TrainingType> get(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Optional<TrainingType> result = Optional.empty();

        try {
            em.getTransaction().begin();

            TrainingType trainingType = em.find(TrainingType.class, id);

            result = Optional.ofNullable(trainingType);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return result;
    }

    public void save(TrainingType trainingType) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(trainingType);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

            throw e;
        } finally {
            em.close();
        }
    }

}
