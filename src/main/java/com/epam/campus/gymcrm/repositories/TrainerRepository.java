package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Repository
public class TrainerRepository implements BaseRepository<Trainer>, UserBehaviour {

    //* Not called
    @Override
    public Optional<Trainer> get(int id) {
        Optional<Trainer> result = Optional.empty();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            Trainer trainer = em.find(Trainer.class, id);
            result = Optional.ofNullable(trainer);
        }

        return result;
    }

    //* Not called
    @Override
    public List<Trainer> getAll() {
        List<Trainer> results = new ArrayList<>();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Trainer> query = em.createQuery("SELECT t FROM Trainer t", Trainer.class);
            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public void save(Trainer trainer) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            em.persist(trainer);

            em.getTransaction().commit();
        }
    }

    @Override
    public void update(Trainer trainer) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            em.merge(trainer);

            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Trainer trainer) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            trainer = em.contains(trainer) ? trainer : em.merge(trainer);
            em.remove(trainer);

            em.getTransaction().commit();
        }
    }

    @Override
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<String> trainersQuery = em.createQuery("SELECT t.user.username FROM Trainer t", String.class);
            usernames.addAll(trainersQuery.getResultList());
            TypedQuery<String> traineesQuery = em.createQuery("SELECT t.user.username FROM Trainee t", String.class);
            usernames.addAll(traineesQuery.getResultList());
        }

        return usernames;
    }

    @Override
    public Optional<Object> getByUsername(String username) {
        Optional<Object> result = Optional.empty();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            Trainer trainer = em.createQuery(
                "SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)
                .setParameter("username", username)
                .getSingleResult();

            result = Optional.ofNullable(trainer);

        }  catch (NoResultException e) {
            return result;
        }

        return result;
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            // Verify that username is from a Trainer
            em.createQuery(
                "SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)
                .setParameter("username", username)
                .getSingleResult();

            Query query = em.createQuery("UPDATE User u SET u.password = :password WHERE u.username = :username");
            query.setParameter("password", newPassword);
            query.setParameter("username", username);
            int updated = query.executeUpdate();

            if (updated == 0) {
                throw new NoSuchElementException("Trainer with username %s not found".formatted(username));
            }

            em.getTransaction().commit();
        }
    }

    @Override
    public void switchActiveStatus(String username) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            boolean isActive = em.createQuery(
                "SELECT t.user.active FROM Trainer t WHERE t.user.username = :username", Boolean.class)
                .setParameter("username", username)
                .getSingleResult();

            isActive=!isActive;
            
            Query query = em.createQuery("UPDATE User u SET u.active = :active WHERE u.username = :username");
            query.setParameter("active", isActive);
            query.setParameter("username", username);
            int updated = query.executeUpdate();

            if (updated == 0) {
                throw new NoSuchElementException("Trainer with username %s not found".formatted(username));
            }

            em.getTransaction().commit();
        }
    }

    public List<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Trainer> cq = cb.createQuery(Trainer.class);
            Root<Trainer> trainer = cq.from(Trainer.class);

            Subquery<Trainer> subquery = cq.subquery(Trainer.class);
            Root<Training> training = subquery.from(Training.class);
            Join<Training, Trainee> trainee = training.join("trainee");
            Join<Trainee, User> traineeUser = trainee.join("user");

            subquery.select(training.get("trainer"))
                    .where(cb.equal(traineeUser.get("username"), traineeUsername));

            cq.select(trainer).where(cb.not(trainer.in(subquery)));

            return em.createQuery(cq).getResultList();
        }
    }

}
