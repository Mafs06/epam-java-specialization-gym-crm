package com.epam.campus.gymcrm.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.storage.Storage;

@Repository
public class TrainingRepository implements BaseRepository<Training>{

    @Override
    public Optional<Training> get(int id) {
        return storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .stream()
            .filter(obj -> obj instanceof Training)
            .map(obj -> (Training) obj)
            .filter(training -> training.getId() == id)
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
    public void update(Training training) {
        // Find and delete old training
        delete(training);

        // Rebuild the training with updated value
        Training updatedTraining = new Training.TrainingBuilder()
            .id(training.getId())
            .traineeID(training.getTraineeID())
            .trainerID(training.getTrainerID())
            .trainingName(training.getTrainingName())
            .trainingDate(training.getTrainingDate())
            .trainingDuration(training.getTrainingDuration())
            .build();

        // Save the updated training
        save(updatedTraining);
    }

    @Override
    public void delete(Training training) {
        storage.getStorage().getOrDefault(ENTITY_KEY, new ArrayList<>())
            .removeIf(obj -> obj instanceof Training && ((Training) obj).getId() == training.getId());
    }

}
