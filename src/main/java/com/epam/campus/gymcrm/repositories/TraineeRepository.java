package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class TraineeRepository implements BaseRepository<Trainee>, UserBehaviour {
    
    //* Not called
    @Override
    public Optional<Trainee> get(int id) {
        Optional<Trainee> result = Optional.empty();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            Trainee trainee = em.find(Trainee.class, id);
            result = Optional.ofNullable(trainee);
        }

        return result;
    }

    //* Not called
    @Override
    public List<Trainee> getAll() {
        List<Trainee> results = new ArrayList<>();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Trainee> query = em.createQuery("SELECT t FROM Trainee t", Trainee.class);
            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public void save(Trainee trainee) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            em.persist(trainee);

            em.getTransaction().commit();
        }
    }

    @Override
    public void update(Trainee trainee) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            em.merge(trainee);

            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Trainee trainee) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            trainee = em.contains(trainee) ? trainee : em.merge(trainee);
            em.remove(trainee);

            em.getTransaction().commit();
        }
    }

    @Override 
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<String> traineesQuery = em.createQuery("SELECT t.user.username FROM Trainee t", String.class);
            usernames.addAll(traineesQuery.getResultList());
            TypedQuery<String> trainersQuery = em.createQuery("SELECT t.user.username FROM Trainer t", String.class);
            usernames.addAll(trainersQuery.getResultList());
        }

        return usernames;
    }

    @Override
    public Optional<Object> getByUsername(String username) {
        Optional<Object> result = Optional.empty();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            Trainee trainee = em.createQuery(
                "SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)
                .setParameter("username", username)
                .getSingleResult();

            result = Optional.ofNullable(trainee);

        }  catch (NoResultException e) {
            return result;
        }

        return result;
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            // Verify that username is from a Trainee
            em.createQuery(
                "SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)
                .setParameter("username", username)
                .getSingleResult();

            Query query = em.createQuery(
                "UPDATE User u SET u.password = :password WHERE u.username = :username"
            );
            query.setParameter("password", newPassword);
            query.setParameter("username", username);
            int updated = query.executeUpdate();

            if (updated == 0) {
                throw new NoSuchElementException("Trainee with username %s not found".formatted(username));
            }

            em.getTransaction().commit();
        }
    }

    @Override
    public void switchActiveStatus(String username) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            boolean isActive = em.createQuery(
                "SELECT t.user.active FROM Trainee t WHERE t.user.username = :username", Boolean.class)
                .setParameter("username", username)
                .getSingleResult();

            isActive=!isActive;
            
            Query query = em.createQuery("UPDATE User u SET u.active = :active WHERE u.username = :username");
            query.setParameter("active", isActive);
            query.setParameter("username", username);
            int updated = query.executeUpdate();

            if (updated == 0) {
                throw new NoSuchElementException("Trainee with username %s not found".formatted(username));
            }

            em.getTransaction().commit();
        }
    }

}
