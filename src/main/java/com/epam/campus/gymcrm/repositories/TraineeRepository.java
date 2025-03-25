package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.storage.Storage;
import com.epam.campus.gymcrm.utils.JPAUtil;

import jakarta.persistence.EntityManager;

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

        /*
        List<String> traineesUsernames = storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainee)
            .map(obj -> (Trainee) obj)
            .map(User::getUsername)
            .collect(Collectors.toList());
        usernames.addAll(traineesUsernames);

        List<String> trainersUsernames = storage.getStorage().getOrDefault("Trainer", new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainer)
            .map(obj -> (Trainer) obj)
            .map(User::getUsername)
            .collect(Collectors.toList());
        usernames.addAll(trainersUsernames); */

        return usernames;
    }

    @Override
    public Optional<Object> getByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByUsername'");
    }

}
