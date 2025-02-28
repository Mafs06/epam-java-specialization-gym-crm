package com.epam.campus.gymcrm.daos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.Trainer;
import com.epam.campus.gymcrm.storage.Storage;

@Repository
public class TrainerDao implements Dao<Trainer>{

    private final Storage storage;
    private final String ENTITY_KEY = "Trainer";

    @Autowired
    public TrainerDao(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<Trainer> get(int id) {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainer)
            .map(obj -> (Trainer) obj)
            .filter(trainer -> trainer.getTrainerID() == id)
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
    public void update(Trainer trainer, String[] params) {
        delete(trainer);
        trainer.setTrainerID(Integer.parseInt(params[0]));
        trainer.setFirstName(params[1]);
        trainer.setLastName(params[2]);
        trainer.setUsername(params[3]);
        trainer.setPassword(params[4]);
        trainer.setActive(Boolean.parseBoolean(params[5]));
        trainer.setSpecialization(params[6]);
        save(trainer);
    }

    @Override
    public void delete(Trainer trainer) {
        storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .removeIf(obj -> obj instanceof Trainer && ((Trainer) obj).getTrainerID() == trainer.getTrainerID());
    }

}
