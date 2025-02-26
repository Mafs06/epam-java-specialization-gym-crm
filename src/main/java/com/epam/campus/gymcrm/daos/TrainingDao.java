package com.epam.campus.gymcrm.daos;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.epam.campus.gymcrm.models.Training;
import com.epam.campus.gymcrm.models.TrainingType;

@Repository
public class TrainingDao implements Dao<Training>{

    private List<Training> trainings = new ArrayList<>();

    public TrainingDao(List<Training> trainings) {
        trainings.add(new Training(1, 1, 1, "Agility 101", new TrainingType("Agility"), LocalDate.of(2025, Month.MARCH, 1), 10));
        trainings.add(new Training(2, 2, 2, "Basics of Self Defense", new TrainingType("Self Defense"), LocalDate.of(2025, Month.FEBRUARY, 28), 15));
    }

    @Override
    public Optional<Training> get(int id) {
        return trainings.stream()
            .filter(training -> training.getTrainerID() == id)
            .findFirst();
    }

    @Override
    public List<Training> getAll() {
        return trainings;
    }

    @Override
    public void save(Training training) {
        trainings.add(training);
    }

    @Override
    public void update(Training training, String[] params) {
        trainings.removeIf(t -> t.getTrainingID() == training.getTrainingID());

        training.setTrainingID(Integer.parseInt(params[0]));
        training.setTraineeID(Integer.parseInt(params[1]));
        training.setTrainerID(Integer.parseInt(params[2]));
        training.setTrainingName(params[3]);
        training.setTrainingType(new TrainingType(params[3]));
        training.setTrainingDate(LocalDate.parse(params[4]));
        training.setTrainingDuration(Integer.parseInt(params[5]));

        trainings.add(training);
    }

    @Override
    public void delete(Training training) {
        trainings.remove(training);
    }

}
