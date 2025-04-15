package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;

@Repository
public class TrainerRepository implements BaseRepository<Trainer>, UserBehaviour {

    @PersistenceContext
    private EntityManager em;

    //* Not called
    @Transactional
    @Override
    public Optional<Trainer> get(int id) {
        Optional<Trainer> result = Optional.empty();
        Trainer trainer = em.find(Trainer.class, id);
        result = Optional.ofNullable(trainer);
        return result;
    }

    //* Not called
    @Transactional
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

    @Transactional
    @Override
    public void save(Trainer trainer) {
        em.persist(trainer);
    }

    @Transactional
    @Override
    public void update(Trainer trainer) {
        em.merge(trainer);
    }

    @Transactional
    @Override
    public void delete(Trainer trainer) {
        trainer = em.contains(trainer) ? trainer : em.merge(trainer);
        em.remove(trainer);
    }

    @Transactional
    @Override
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();

        TypedQuery<String> trainersQuery = em.createQuery("SELECT t.user.username FROM Trainer t", String.class);
        usernames.addAll(trainersQuery.getResultList());
        TypedQuery<String> traineesQuery = em.createQuery("SELECT t.user.username FROM Trainee t", String.class);
        usernames.addAll(traineesQuery.getResultList());

        return usernames;
    }

    @Transactional
    @Override
    public Optional<Object> getByUsername(String username) {
        Optional<Object> result = Optional.empty();

        try {
            Object trainer = em.createQuery(
                "SELECT t FROM Trainer t LEFT JOIN FETCH t.trainees WHERE t.user.username = :username", Object.class)
                .setParameter("username", username)
                .getSingleResult();

            result = Optional.ofNullable(trainer);

        }  catch (NoResultException e) {
            return result;
        }

        return result;
    }

    @Transactional
    @Override
    public void updatePassword(String username, String newPassword) {
        Query query = em.createQuery("UPDATE User u SET u.password = :password WHERE u.username = :username");
        query.setParameter("password", newPassword);
        query.setParameter("username", username);
        int updated = query.executeUpdate();

        if (updated == 0) {
            throw new NoSuchElementException("Trainer with username %s not found".formatted(username));
        }
    }

    @Transactional
    @Override
    public boolean switchActiveStatus(String username) {
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

        return isActive;
    }

    @Transactional
    @Override
    public void updateActiveStatus(String username, boolean active) {
        Query query = em.createQuery("UPDATE User u SET u.active = :active WHERE u.username = :username");
        query.setParameter("active", active);
        query.setParameter("username", username);
        
        int updated = query.executeUpdate();
        if (updated == 0) {
            throw new NoSuchElementException("Trainee with username %s not found".formatted(username));
        }
    }

    @Transactional
    public List<Trainer> findActiveTrainersNotAssignedToTrainee(String traineeUsername) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Trainer> cq = cb.createQuery(Trainer.class);

        Root<Trainer> trainer = cq.from(Trainer.class);
        Join<Trainer, User> trainerUser = trainer.join("user");

        // Assigned trainers to the trainee
        Subquery<Trainer> subquery = cq.subquery(Trainer.class);
        Root<Trainee> trainee = subquery.from(Trainee.class);
        Join<Trainee, User> traineeUser = trainee.join("user");
        Join<Trainee, Trainer> assignedTrainers = trainee.join("trainers"); // because of Many to Many relationship

        subquery.select(assignedTrainers)
                .where(cb.equal(traineeUser.get("username"), traineeUsername));

        // Not asiggned and actives
        cq.select(trainer).where(
                cb.and(
                        cb.not(trainer.in(subquery)),
                        cb.isTrue(trainerUser.get("active"))
                )
        );

        return em.createQuery(cq).getResultList();
    }

}
