package com.epam.campus.gymcrm.facade;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.services.impl.TraineeService;
import com.epam.campus.gymcrm.services.impl.TrainerService;
import com.epam.campus.gymcrm.services.impl.TrainingService;

public class GymFacade {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    // Trainee methods
    public TraineeDto getTrainee(int id) {
        return traineeService.getTrainee(id);
    }

    public List<TraineeDto> getAllTrainees() {
        return traineeService.getTrainees();
    }

    public void createTrainee(String[] params) {
        TraineeDto traineeDto = new TraineeDto(
            Integer.parseInt(params[0]),
            params[1],
            params[2],
            Boolean.parseBoolean(params[3]),
            LocalDate.parse(params[4]),
            params[5]);
        
        traineeService.createTrainee(traineeDto);
    }

    public void updateTrainee(int id, String[] params) {
        TraineeDto traineeDto = new TraineeDto(
            Integer.parseInt(params[0]),
            params[1],
            params[2],
            Boolean.parseBoolean(params[3]),
            LocalDate.parse(params[4]),
            params[5]);

        traineeService.updateTrainee(id, traineeDto);
    }

    public void deleteTrainee(int id) {
        traineeService.deleteTrainee(id);
    }

    // Trainer methods
    public TrainerDto getTrainer(int id) {
        return trainerService.getTrainer(id);
    }

    public List<TrainerDto> getAllTrainers() {
        return trainerService.getTrainers();
    }

    public void createTrainer(String[] params) {
        TrainerDto trainerDto = new TrainerDto(
            Integer.parseInt(params[0]),
            params[1],
            params[2],
            Boolean.parseBoolean(params[3]),
            params[4]);

        trainerService.createTrainer(trainerDto);
    }

    public void updateTrainer(int id, String[] params) {
        TrainerDto trainerDto = new TrainerDto(
            Integer.parseInt(params[0]),
            params[1],
            params[2],
            Boolean.parseBoolean(params[3]),
            params[4]);
        trainerService.updateTrainer(id, trainerDto);
    }

    public void deleteTrainer(int id) {
        trainerService.deleteTrainer(id);
    }

    // Training methods
    public TrainingDto getTraining(int id) {
        return trainingService.getTraining(id);
    }

    public List<TrainingDto> getAllTrainings() {
        return trainingService.getTrainings();
    }

    public void createTraining(String[] params) {
        TrainingDto trainingDto = new TrainingDto(
            Integer.parseInt(params[0]),
            Integer.parseInt(params[1]),
            Integer.parseInt(params[2]),
            params[3],
            LocalDate.parse(params[4]),
            Integer.parseInt(params[5]));
            
        trainingService.createTraining(trainingDto);
    }

    public void updateTraining(int id, String[] params) {
        TrainingDto trainingDto = new TrainingDto(
            Integer.parseInt(params[0]),
            Integer.parseInt(params[1]),
            Integer.parseInt(params[2]),
            params[3],
            LocalDate.parse(params[4]),
            Integer.parseInt(params[5]));

        trainingService.updateTraining(id, trainingDto);
    }

    public void deleteTraining(int id) {
        trainingService.deleteTraining(id);
    }
}

