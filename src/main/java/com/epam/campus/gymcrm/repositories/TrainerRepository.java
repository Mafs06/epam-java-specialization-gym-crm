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

@Repository
public class TrainerRepository implements BaseRepository<Trainer>, UserBehaviour{

    private Storage storage;
    private final String ENTITY_KEY = "Trainer";

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<Trainer> get(int id) {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainer)
            .map(obj -> (Trainer) obj)
            .filter(trainer -> trainer.getId() == id)
            .findFirst();
    }

    @Override
    public List<Trainer> getAll() {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainer)
            .map(obj -> (Trainer) obj)
            .collect(Collectors.toList());
    }

    @Override
    public void save(Trainer trainer) {
        storage.addData(ENTITY_KEY, trainer);
    }

    @Override
    public void update(Trainer trainer) {
        // Find and delete old trainer
        delete(trainer);
    
        // Rebuild the trainer with updated values
        Trainer updatedTrainer = new Trainer.TrainerBuilder()
                .id(trainer.getId())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .active(trainer.isActive())
                .specialization(trainer.getSpecialization())
                .build();
    
        // Save the updated trainer
        save(updatedTrainer);
    }

    @Override
    public void delete(Trainer trainer) {
        storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .removeIf(obj -> obj instanceof Trainer && ((Trainer) obj).getId() == trainer.getId());
    }

    @Override
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();

        List<String> trainersUsernames = storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainer)
            .map(obj -> (Trainer) obj)
            .map(User::getUsername)
            .collect(Collectors.toList());
        usernames.addAll(trainersUsernames);

        List<String> traineesUsernames = storage.getStorage().getOrDefault("Trainee", new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainee)
            .map(obj -> (Trainee) obj)
            .map(User::getUsername)
            .collect(Collectors.toList());
        usernames.addAll(traineesUsernames);

        return usernames;
    }

}
