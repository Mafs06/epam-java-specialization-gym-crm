package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Repository
public class TrainerRepository implements BaseRepository<Trainer>, UserBehaviour{

    @Override
    public Optional<Trainer> get(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Optional<Trainer> result = Optional.empty();

        try {
            Trainer trainer = em.find(Trainer.class, id);
            result = Optional.ofNullable(trainer);
        } finally {
            em.close();
        }

        return result;
    }

    @Override
    public List<Trainer> getAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Trainer> results = new ArrayList<>();

        try {
            TypedQuery<Trainer> query = em.createQuery("SELECT t FROM Trainer t", Trainer.class);
            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return results;
    }

    @Override
    public void save(Trainer trainer) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(trainer);

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

    @Override
    public void update(Trainer trainer) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(trainer);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Trainer trainer) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            trainer = em.contains(trainer) ? trainer : em.merge(trainer);
            em.remove(trainer);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<String> trainersQuery = em.createQuery("SELECT t.user.username FROM Trainer t", String.class);
            usernames.addAll(trainersQuery.getResultList());
            TypedQuery<String> traineesQuery = em.createQuery("SELECT t.user.username FROM Trainee t", String.class);
            usernames.addAll(traineesQuery.getResultList());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }

        return usernames;
    }

    @Override
    public Optional<Object> getByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        Optional<Object> result = Optional.empty();

        try {
            Trainer trainer = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)
                .setParameter("username", username)
                .getSingleResult();

            result = Optional.ofNullable(trainer);

        }  catch (NoResultException e) {
            return result;
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public void updatePassword(int trainerID, String newPassword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Query query = em.createQuery("UPDATE User t SET t.password = :password WHERE t.id = :id");
            query.setParameter("password", newPassword);
            query.setParameter("id", trainerID);
            int updated = query.executeUpdate();

            if (updated == 0) {
                throw new NoSuchElementException("Trainer with ID " + trainerID + " not found");
            }
            em.getTransaction().commit();
        } catch (NoSuchElementException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;
        } finally {
            em.close();
        }
    }

}
