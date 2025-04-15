package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class TraineeRepository implements BaseRepository<Trainee>, UserBehaviour {

    @PersistenceContext
    private EntityManager em;
    
    //* Not called
    @Transactional
    @Override
    public Optional<Trainee> get(int id) {
        Optional<Trainee> result = Optional.empty();
        Trainee trainee = em.find(Trainee.class, id);
        result = Optional.ofNullable(trainee);
        return result;
    }

    //* Not called
    @Transactional
    @Override
    public List<Trainee> getAll() {
        List<Trainee> results = new ArrayList<>();

        TypedQuery<Trainee> query = em.createQuery("SELECT t FROM Trainee t", Trainee.class);
        results = query.getResultList();

        return results;
    }

    @Transactional
    @Override
    public void save(Trainee trainee) {
        em.persist(trainee);
    }

    @Transactional
    @Override
    public void update(Trainee trainee) {
        em.merge(trainee);
    }

    @Override
    @Transactional
    public void delete(Trainee trainee) {
        trainee = em.contains(trainee) ? trainee : em.merge(trainee);
        em.remove(trainee);
    }

    @Transactional
    @Override 
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();

        TypedQuery<String> traineesQuery = em.createQuery("SELECT t.user.username FROM Trainee t", String.class);
        usernames.addAll(traineesQuery.getResultList());
        TypedQuery<String> trainersQuery = em.createQuery("SELECT t.user.username FROM Trainer t", String.class);
        usernames.addAll(trainersQuery.getResultList());

        return usernames;
    }

    @Transactional
    @Override
    public Optional<Object> getByUsername(String username) {
        Optional<Object> result = Optional.empty();

        try {

            Object trainee = em.createQuery(
                "SELECT t FROM Trainee t LEFT JOIN FETCH t.trainers WHERE t.user.username = :username", Object.class)
                .setParameter("username", username)
                .getSingleResult();

            result = Optional.ofNullable(trainee);

        }  catch (NoResultException e) {
            return result;
        }

        return result;
    }

    @Transactional
    @Override
    public void updatePassword(String username, String newPassword) {
        Query query = em.createQuery(
            "UPDATE User u SET u.password = :password WHERE u.username = :username"
        );
        query.setParameter("password", newPassword);
        query.setParameter("username", username);

        int updated = query.executeUpdate();
        if (updated == 0) {
            throw new NoSuchElementException("Trainee with username %s not found".formatted(username));
        }
    }

    @Transactional
    @Override
    public boolean switchActiveStatus(String username) {
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
    public List<Trainer> updateTrainers(Trainee trainee, List<String>trainerUsernames) {
        List<Trainer> newTrainers = em.createQuery(
            "SELECT t FROM Trainer t JOIN FETCH t.user WHERE t.user.username IN :usernames", Trainer.class
        )
        .setParameter("usernames", trainerUsernames)
        .getResultList();

        if (newTrainers.size() != trainerUsernames.size()) {
            throw new NoSuchElementException("Some of the Trainers with usernames %s not found".formatted(trainerUsernames));
        }

        // Update the list of trainers
        trainee.setTrainers(newTrainers);
        em.merge(trainee);

        return newTrainers;
    }

}