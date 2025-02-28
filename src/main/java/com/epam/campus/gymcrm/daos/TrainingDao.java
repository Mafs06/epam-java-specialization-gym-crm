package com.epam.campus.gymcrm.daos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.Training;
import com.epam.campus.gymcrm.models.TrainingType;
import com.epam.campus.gymcrm.storage.Storage;

@Repository
public class TrainingDao implements Dao<Training>{

    private final Storage storage;
    private final String ENTITY_KEY = "Training";

    @Autowired
    public TrainingDao(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Optional<Training> get(int id) {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Training)
            .map(obj -> (Training) obj)
            .filter(training -> training.getTrainingID() == id)
            .findFirst();
    }

    @Override
    public List<Training> getAll() {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Training)
            .map(obj -> (Training) obj)
            .collect(Collectors.toList());
    }

    @Override
    public void save(Training training) {
        storage.addData(ENTITY_KEY, training);
    }

    @Override
    public void update(Training training, String[] params) {
        delete(training);
        training.setTrainingID(Integer.parseInt(params[0]));
        training.setTraineeID(Integer.parseInt(params[1]));
        training.setTrainerID(Integer.parseInt(params[2]));
        training.setTrainingName(params[3]);
        training.setTrainingType(new TrainingType(params[3]));
        training.setTrainingDate(LocalDate.parse(params[4]));
        training.setTrainingDuration(Integer.parseInt(params[5]));
        save(training);
    }

    @Override
    public void delete(Training training) {
        storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .removeIf(obj -> obj instanceof Training && ((Training) obj).getTrainingID() == training.getTrainingID());
    }

}
