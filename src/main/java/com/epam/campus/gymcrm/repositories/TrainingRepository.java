package com.epam.campus.gymcrm.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

@Repository
public class TrainingRepository implements BaseRepository<Training>{

    //* Not called
    @Override
    public Optional<Training> get(int id) {
        Optional<Training> result = Optional.empty();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            Training training = em.find(Training.class, id);
            result = Optional.ofNullable(training);
        }

        return result;
    }

    //* Not called
    @Override
    public List<Training> getAll() {
        List<Training> results = new ArrayList<>();

        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Training> query = em.createQuery("SELECT t FROM Training t", Training.class);
            results = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public void save(Training training) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            em.persist(training);

            em.getTransaction().commit();
        }
    }

    //* Not called
    @Override
    public void update(Training training) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            em.merge(training);

            em.getTransaction().commit();
        }
    }

    //* Not called
    @Override
    public void delete(Training training) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            training = em.contains(training) ? training : em.merge(training);
            em.remove(training);

            em.getTransaction().commit();
        }
    }

    public List<Training> findTrainingsByTraineeCriteria(
        String username,
        LocalDate fromDate,
        LocalDate toDate,
        String trainerUsername,
        String trainingTypeName)
    {
        try (EntityManager em = JPAUtil.getEntityManager()) {

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Training> cq = cb.createQuery(Training.class);
            Root<Training> training = cq.from(Training.class);

            // Avoid LazyInitializationException
            training.fetch("trainee", JoinType.LEFT).fetch("user", JoinType.LEFT);
            training.fetch("trainer", JoinType.LEFT).fetch("user", JoinType.LEFT);
            training.fetch("trainingType", JoinType.LEFT);

            // Create joins only for the filters (without repeating the fetches)
            Join<Training, Trainee> trainee = training.join("trainee");
            Join<Trainee, User> traineeUser = trainee.join("user");

            Join<Training, Trainer> trainer = training.join("trainer");
            Join<Trainer, User> trainerUser = trainer.join("user");

            Join<Training, TrainingType> trainingType = training.join("trainingType");

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(traineeUser.get("username"), username));

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(training.get("date"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(training.get("date"), toDate));
            }

            if (trainerUsername != null && !trainerUsername.isEmpty()) {
                predicates.add(cb.like(cb.lower(trainerUser.get("username")), "%" + trainerUsername.toLowerCase() + "%"));
            }

            if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
                predicates.add(cb.equal(cb.lower(trainingType.get("name")), trainingTypeName.toLowerCase()));
            }

            cq.select(training).where(cb.and(predicates.toArray(new Predicate[0])));

            return em.createQuery(cq).getResultList();
        }
    }

    public List<Training> findTrainingsByTrainerCriteria(
        String username,
        LocalDate fromDate,
        LocalDate toDate,
        String traineeUsername)
    {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Training> cq = cb.createQuery(Training.class);
            Root<Training> training = cq.from(Training.class);

            // Avoid LazyInitializationException
            training.fetch("trainee", JoinType.LEFT).fetch("user", JoinType.LEFT);
            training.fetch("trainer", JoinType.LEFT).fetch("user", JoinType.LEFT);
            training.fetch("trainingType", JoinType.LEFT);

            // Create joins only for the filters (without repeating the fetches)
            Join<Training, Trainer> trainer = training.join("trainer");
            Join<Trainer, User> trainerUser = trainer.join("user");

            Join<Training, Trainee> trainee = training.join("trainee");
            Join<Trainee, User> traineeUser = trainee.join("user");

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(trainerUser.get("username"), username));

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(training.get("date"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(training.get("date"), toDate));
            }

            if (traineeUsername != null && !traineeUsername.isEmpty()) {
                predicates.add(cb.like(cb.lower(traineeUser.get("username")), "%" + traineeUsername.toLowerCase() + "%"));
            }

            cq.select(training).where(cb.and(predicates.toArray(new Predicate[0])));

            return em.createQuery(cq).getResultList();
        }
    }

}
