package com.epam.campus.gymcrm;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.Trainee;
import com.epam.campus.gymcrm.models.Trainer;
import com.epam.campus.gymcrm.models.Training;
import com.epam.campus.gymcrm.servises.TraineeService;
import com.epam.campus.gymcrm.servises.TrainerService;
import com.epam.campus.gymcrm.servises.TrainingService;

@Component
public class GymFacade {

    private TrainerService trainerService;
    private TraineeService traineeService;
    private TrainingService trainingService;

    @Autowired
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    // Trainee methods
    public Optional<Trainee> getTrainee(int id) {
        return traineeService.getTrainee(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.getTrainees();
    }

    public void createTrainee(Trainee trainee) {
        traineeService.createTrainee(trainee);
    }

    public void updateTrainee(Trainee trainee, String[] params) {
        traineeService.updateTrainee(trainee, params);
    }

    public void deleteTrainee(Trainee trainee) {
        traineeService.deleteTrainee(trainee);
    }

    // Trainer methods
    public Optional<Trainer> getTrainer(int id) {
        return trainerService.getTrainer(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.getTrainers();
    }

    public void createTrainer(Trainer trainer) {
        trainerService.createTrainer(trainer);
    }

    public void updateTrainer(Trainer trainer, String[] params) {
        trainerService.updateTrainer(trainer, params);
    }

    public void deleteTrainer(Trainer trainer) {
        trainerService.deleteTrainer(trainer);
    }

    // Training methods
    public Optional<Training> getTraining(int id) {
        return trainingService.getTraining(id);
    }

    public List<Training> getAllTrainings() {
        return trainingService.getTrainings();
    }

    public void createTraining(Training trainin) {
        trainingService.createTraining(trainin);
    }

    public void updateTraining(Training trainin, String[] params) {
        trainingService.updateTraining(trainin, params);
    }

    public void deleteTraining(Training trainin) {
        trainingService.deleteTraining(trainin);
    }
}

