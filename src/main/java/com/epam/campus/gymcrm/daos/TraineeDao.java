package com.epam.campus.gymcrm.daos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.Trainee;
import com.epam.campus.gymcrm.storage.Storage;

@Repository
public class TraineeDao implements Dao<Trainee>{

    private final Storage storage;
    private final String ENTITY_KEY = "Trainer";

    @Autowired
    public TraineeDao(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<Trainee> get(int id) {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainee)
            .map(obj -> (Trainee) obj)
            .filter(trainer -> trainer.getTraineeID() == id)
            .findFirst();
    }

    @Override
    public List<Trainee> getAll() {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Trainee)
            .map(obj -> (Trainee) obj)
            .collect(Collectors.toList());
    }

    @Override
    public void save(Trainee trainee) {
        storage.addData(ENTITY_KEY, trainee);
    }

    @Override
    public void update(Trainee trainee, String[] params) {
        delete(trainee);
        trainee.setTraineeID(Integer.parseInt(params[0]));
        trainee.setFirstName(params[1]);
        trainee.setLastName(params[2]);
        trainee.setUsername(params[3]);
        trainee.setPassword(params[4]);
        trainee.setActive(Boolean.parseBoolean(params[5]));
        trainee.setDateOfBirth(LocalDate.parse(params[6]));  
        trainee.setAddress(params[7]);  
        save(trainee);
    }

    @Override
    public void delete(Trainee trainee) {
        storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .removeIf(obj -> obj instanceof Trainee && ((Trainee) obj).getTraineeID() == trainee.getTraineeID());
    }

}
