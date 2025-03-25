package com.epam.campus.gymcrm.facade;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.services.impl.TraineeService;
import com.epam.campus.gymcrm.services.impl.TrainerService;
import com.epam.campus.gymcrm.services.impl.TrainingService;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;

public class GymFacade {

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    //* Trainee operations

    // Login as Trainee
    public boolean traineeLogin(String username, String password) {
        return traineeService.traineeLogin(username, password);
    }

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

    // Get Trainee profile by username
    public void getTraineeByUsername(String username) {
        try {
            TraineeDto traineeDto = traineeService.getTraineeByUsername(username);
            System.out.println(traineeDto.toString());
        } catch (NoSuchElementException e) {
            System.err.println("Entered username does not exist");
        }
        
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

    // Change Trainer Password
    public void updateTraineePassword(String username, String newPassword) {
        traineeService.updateTraineePassword(username, newPassword);
    }

    // Activate/De-activate Trainee
    public void switchTraineeActiveStatus(String username) {
        traineeService.switchTraineeActiveStatus(username);
    }

    // TODO: Update Trainee's trainers list

    // TODO: Delete Trainee profile by username

    //* Trainer operations

    // Login as Trainer
    public boolean trainerLogin(String username, String password) {
        return trainerService.trainerLogin(username, password);
    }

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

    // Get Trainer profile by username
    public void getTrainerByUsername(String username) {
        try {
            TrainerDto trainerDto = trainerService.getTrainerByUsername(username);
            System.out.println(trainerDto.toString());
        } catch (NoSuchElementException e) {
            System.err.println("Entered username does not exist");
        }
        
    }

    // TODO: Get Trainers list not assigned on trainee, by trainee's username

    // Update Trainer profile
    public void updateTrainer(String username, String[] params) {
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

        if (!params[3].matches("[1-5]")) {
            System.err.println("Incorrect training type id for trainer");
            return;
        }

        TrainerDto trainerDto = new TrainerDto(
            params[0],                          // firstName
            params[1],                          // lastName
            active,                             // active
            Integer.parseInt(params[3]));       //trainingType
        trainerService.updateTrainer(username, trainerDto);
    }

    // Change Trainer Password
    public void updateTrainerPassword(String username, String newPassword) {
        trainerService.updateTrainerPassword(username, newPassword);
    }

    // Activate/De-activate Trainer
    public void switchTrainerActiveStatus(String username) {
        trainerService.switchTrainerActiveStatus(username);
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

