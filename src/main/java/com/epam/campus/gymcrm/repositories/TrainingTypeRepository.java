package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Repository
public class TrainingTypeRepository {

    public Optional<TrainingType> getByName(String name) {
        Optional<TrainingType> result = Optional.empty();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            TrainingType trainingType = em.createQuery(
                "SELECT t FROM TrainingType t WHERE t.name = :name", TrainingType.class)
                .setParameter("name", name)
                .getSingleResult();

            result = Optional.ofNullable(trainingType);

        }  catch (NoResultException e) {
            return result;
        }

        return result;
    }

    public List<TrainingType> getAll() {
        List<TrainingType> results = new ArrayList<>();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<TrainingType> query = em.createQuery("SELECT t FROM TrainingType t", TrainingType.class);
            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public void save(TrainingType trainingType) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            em.persist(trainingType);

            em.getTransaction().commit();
        }
    }

}
