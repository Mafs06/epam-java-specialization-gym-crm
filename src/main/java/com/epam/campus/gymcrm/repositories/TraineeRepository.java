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
    
    @Override
    public Optional<Trainee> get(int id) {
        return Optional.of(new Trainee());
    }

    @Override
    public List<Trainee> getAll() {
        return new ArrayList<>();
    }

    @Override
    public void save(Trainee trainee) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(trainee);

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
    public void update(Trainee trainee) {
        save(trainee);
    }

    @Override
    public void delete(Trainee trainee) {
        
    }

    @Override 
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<String> traineesQuery = em.createQuery("SELECT t.user.username FROM Trainee t", String.class);
            usernames.addAll(traineesQuery.getResultList());
            TypedQuery<String> trainersQuery = em.createQuery("SELECT t.user.username FROM Trainer t", String.class);
            usernames.addAll(trainersQuery.getResultList());
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
            Trainee trainee = em.createQuery(
                "SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)
                .setParameter("username", username)
                .getSingleResult();

            result = Optional.ofNullable(trainee);

        }  catch (NoResultException e) {
            return result;
        } finally {
            em.close();
        }
        return result;
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Query query = em.createQuery("UPDATE User u SET u.password = :password WHERE u.username = :username");
            query.setParameter("password", newPassword);
            query.setParameter("username", username);
            int updated = query.executeUpdate();

            if (updated == 0) {
                throw new NoSuchElementException("Trainee with username " + username + " not found");
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

    @Override
    public void switchActiveStatus(String username) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
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
                throw new NoSuchElementException();
            }

            em.getTransaction().commit();
        } catch (NoSuchElementException | NoResultException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;
        }
    }

}
