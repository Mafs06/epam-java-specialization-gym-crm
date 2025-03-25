package com.epam.campus.gymcrm.facade;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.services.impl.TraineeService;
import com.epam.campus.gymcrm.services.impl.TrainerService;
import com.epam.campus.gymcrm.services.impl.TrainingService;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;

public class GymFacade {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    //* Trainee/Trainer operations

    // TODO: Get Trainee/Trainer profile by username

    // TODO: Trainee/Trainer username and password matching
    public boolean traineeLogin(String username, String password) {
        return false;
    }

    public boolean trainerLogin(String username, String password) {
        return trainerService.trainerLogin(username, password);
    }

    // TODO: Trainee/Trainer password change

    // TODO: Activate/De-activate Trainee/Trainer

    //* Trainee operations

    // Create Trainee profile
    public void createTrainee(String[] params) {
        if (params.length != 5) {
            System.err.println("The amount of parameters is not the expected");
            return;
        }

        boolean active;
        if (params[2].equalsIgnoreCase("true") || params[2].equalsIgnoreCase("false")) {
            active = Boolean.parseBoolean(params[2]);
        } else {
            System.err.println("Incorrect active option for trainee");
            return;
        }

        TraineeDto traineeDto = new TraineeDto(
        params[0],                          // firstName
        params[1],                          // lastName
        active,                             // active
        LocalDate.parse(params[3]),         // dateOfBirth
        params[4]);                         // adress
    
        traineeService.createTrainee(traineeDto);
    }

    // Update Trainee profile
    public void updateTrainee(int id, String[] params) {
        if (params.length != 5) {
            System.err.println("The amount of parameters is not the expected");
            return;
        }

        boolean active;
        if (params[2].equalsIgnoreCase("true") || params[2].equalsIgnoreCase("false")) {
            active = Boolean.parseBoolean(params[2]);
        } else {
            System.err.println("Incorrect active option for trainee");
            return;
        }

        TraineeDto traineeDto = new TraineeDto(
            params[0],                      // firstName
            params[1],                      // lastName
            active,                         // active
            LocalDate.parse(params[3]),     // dateOfBirth
            params[4]);                     // address

        traineeService.updateTrainee(id, traineeDto);
    }

    // TODO: Update Trainee's trainers list

    // TODO: Delete Trainee profile by username

    //* Trainer operations

    // Create Trainer profile
    public void createTrainer(String[] params) {
        if (params.length != 4) {
            System.err.println("The amount of parameters is not the expected");
            return;
        }

        // TODO: match TrainingType id by get, not by hardcoded regex
        boolean active;
        if (params[2].equalsIgnoreCase("true") || params[2].equalsIgnoreCase("false")) {
            active = Boolean.parseBoolean(params[2]);
        } else {
            System.err.println("Incorrect active option for trainer");
            return;
        }

        if (!params[3].matches("[1-5]")) {
            System.err.println("Incorrect training type id for trainer");
            return;
        }

        TrainerDto trainerDto = new TrainerDto(
            params[0],                          // firstName
            params[1],                          // lastName
            active,                            // active
            Integer.parseInt(params[3]));       //trainingType

        trainerService.createTrainer(trainerDto);
    }

    // TODO: Get Trainers list not assigned on trainee, by trainee's username

    // Update Trainer profile
    public void updateTrainer(int id, String[] params) {
        if (params.length != 4) {
            System.err.println("The amount of parameters is not the expected");
            return;
        }

        boolean active;
        if (params[2].equalsIgnoreCase("true") || params[2].equalsIgnoreCase("false")) {
            active = Boolean.parseBoolean(params[2]);
        } else {
            System.err.println("Incorrect active option for trainer");
            return;
        }

        if (params[3].matches("[1-5]")) {
            System.err.println("Incorrect training type id for trainer");
            return;
        }

        TrainerDto trainerDto = new TrainerDto(
            params[0],                          // firstName
            params[1],                          // lastName
            active,                             // active
            Integer.parseInt(params[3]));       //trainingType
        trainerService.updateTrainer(id, trainerDto);
    }

    //* Training operations

    // Create training
    public void createTraining(String[] params) {
        /*TrainingDto trainingDto = new TrainingDto(
            Integer.parseInt(params[0]),
            Integer.parseInt(params[1]),
            Integer.parseInt(params[2]),
            params[3],
            LocalDate.parse(params[4]),
            Integer.parseInt(params[5]));
            
        trainingService.createTraining(trainingDto)*/;
    }

    // TODO: Get Trainee Trainings List by trainee username and criteria

    // TODO: Get Trainer Trainings List by trainer username and criteria

    public void deleteTraining(int id) {
        trainingService.deleteTraining(id);
    }

}

