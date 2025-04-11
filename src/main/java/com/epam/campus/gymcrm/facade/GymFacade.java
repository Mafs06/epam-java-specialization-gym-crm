package com.epam.campus.gymcrm.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.services.impl.TraineeService;
import com.epam.campus.gymcrm.services.impl.TrainerService;
import com.epam.campus.gymcrm.services.impl.TrainingService;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;
import com.epam.campus.gymcrm.utils.ConsoleUtil;

import jakarta.persistence.NoResultException;

public class GymFacade {

    private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
    }

    //* Trainee operations

    // Login as Trainee
    public boolean traineeLogin(String username, String password) {
        try {
            if (traineeService.traineeLogin(username, password)) {
                System.out.println("Welcome " + username);
                return true;
            } else {
                System.err.println("Username and password do not match. Try again.");
                return false;
            }

        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            System.err.println("Username and password do not match. Try again.");
            return false;
        }
    }

    // Create Trainee profile
    public void createTrainee(TraineeDto traineeDto) {
        try{
            Trainee trainee = traineeService.createTrainee(traineeDto);
            System.out.println("Trainee created with username: " + trainee.getUser().getUsername());
        } catch (Exception e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            System.err.println("There was an error and trainee could not be added.");
            return;
        }
            
        
    }

    // Get Trainee profile by username
    public void getTraineeByUsername(String username) {
        try {
            TraineeDto traineeDto = traineeService.getTraineeByUsername(username);
            System.out.println(ConsoleUtil.formatDto(traineeDto.toString()));
        } catch (NoSuchElementException e) {
            System.err.println("Trainer with that username does not exist");
        }
        
    }

    // Update Trainee profile
    public void updateTrainee(String username, TraineeDto traineeDto) {
        try {
            traineeService.updateTrainee(username, traineeDto);
            System.out.println("Trainee updated.");
        } catch (NoSuchElementException e) {
            logger.error("Validation failed: ", e.getMessage());
            System.err.println("There was an error and trainee could not be updated.");
        }
    }

    // Change Trainer Password
    public void updateTraineePassword(String username, String newPassword) {
        try {
            traineeService.updateTraineePassword(username, newPassword);
            System.out.println("Password changed.");
        } catch (NoResultException e) {
            logger.error(e.getMessage());
            System.out.println("There was an error and password could not be changed.");
        }
    }

    // Activate/De-activate Trainee
    public void switchTraineeActiveStatus(String username) {
        try {
            traineeService.switchTraineeActiveStatus(username);
            System.out.println("Active status changed.");
        } catch (NoSuchElementException | NoResultException e) {
            logger.error(e.getMessage());
            System.err.println("There was an error and active status could not be changed.");
        }
    }

    // Update Trainee's trainers list
    public void updateTraineeTrainers(String traineUsername, List<String> newTrainers) {
        try {
            traineeService.updateTraineeTrainersList(traineUsername, newTrainers);
        } catch (NoSuchElementException e) {
            logger.error("Validation failed: ", e.getMessage());
            System.err.println("There was an error and trainee could not be updated.");
        }
    }

    public void deleteTrainee(String username) {
        try {
            traineeService.deleteTrainee(username);
            System.out.println("Trainee deleted.");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            System.err.println("There was an error and trainee could not be deleted.");
        }
    }

    //*  Trainer operations

    // Login as Trainer
    public boolean trainerLogin(String username, String password) {
        try {
            if (trainerService.trainerLogin(username, password)) {
                System.out.println("Welcome " + username);
                return true;
            } else {
                System.err.println("Username and password do not match. Try again.");
                return false;
            }
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            System.err.println("Username and password do not match. Try again.");
            return false;
        }
    }

    // Create Trainer profile
    public void createTrainer(TrainerDto newTrainerDto) {
        try {
            Trainer trainer = trainerService.createTrainer(newTrainerDto);
            System.out.println("Trainer created with username: " + trainer.getUser().getUsername());

        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            System.err.println("There was an error and trainer could not be added. Verify specialization data.");
            return;

        } catch (Exception e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            System.err.println("There was an error and trainer could not be added.");
            return;
        }
    }

    // Get Trainer profile by username
    public void getTrainerByUsername(String username) {
        try {
            TrainerDto trainerDto = trainerService.getTrainerByUsername(username);
            System.out.println(ConsoleUtil.formatDto(trainerDto.toString()));
        } catch (NoSuchElementException e) {
            logger.error("Validation failed: ", e.getMessage());
            System.err.println("Trainer with that username does not exist");
        }
        
    }

    // Get Trainers list not assigned on trainee, by trainee's username
    public void getTrainersNotAssignedToTrainee(String traineeUsername) {
        try {
            List<TrainerDto> trainers = trainerService.getTrainersNotAssignedToTrainee(traineeUsername);
    
            if (!trainers.isEmpty()) {
                trainers.forEach(t -> System.out.println("\n" + ConsoleUtil.formatDto(t.toString())));
            } else {
                System.out.println("No unassigned trainers found for the trainee.");
            }

        } catch (NoSuchElementException e) {
            logger.error("Validation failed: ", e.getMessage());
            System.err.println("Trainee with that username does not exist");
        }
    }

    // Update Trainer profile
    public void updateTrainer(String username, TrainerDto trainerDto) {
        try {
            trainerService.updateTrainer(username, trainerDto);
            System.out.println("Trainer updated.");
        } catch (NoSuchElementException e) {
            logger.error("Validation failed: ", e.getMessage());
            System.err.println("There was an error and trainer could not be updated.");
        }
        
    }

    // Change Trainer Password
    public void updateTrainerPassword(String username, String newPassword) {
        try {
            trainerService.updateTrainerPassword(username, newPassword);
            System.out.println("Password changed.");
        } catch (NoSuchElementException  | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            System.out.println("There was an error and password could not be changed.");
        }
    }

    // Activate/De-activate Trainer
    public void switchTrainerActiveStatus(String username) {
        try {
            trainerService.switchTrainerActiveStatus(username);
            System.out.println("Active status changed.");
        } catch (NoSuchElementException | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            System.err.println("There was an error and active status could not be changed.");
        }
        
    }

    //* Training operations

    // Create training
    public void createTraining(TrainingDto trainingDto) {
        try {
            trainingService.createTraining(trainingDto);
            System.out.println("Training created");
        } catch (NoSuchElementException e) {
            logger.error("Validation error when adding training: {}", e.getMessage());
            System.err.println("There was a validation error and training could not be added.");
            return;

        } catch (Exception e) {
            logger.error("Unexpected error while adding training: {} {}", e.getClass(), e.getMessage());
            System.err.println("There was an error and training could not be added.");
            return;
        }
    }

    // Get Trainee Trainings List by trainee username and criteria
    public void getTrainingsByTraineeCriteria(String username,
                                                LocalDate fromDate,
                                                LocalDate toDate,
                                                String trainerUsername,
                                                String trainingTypeName)
    {
        List<TrainingDto> trainings = trainingService.getTrainingsByTraineeCriteria(
                username, fromDate, toDate, trainerUsername, trainingTypeName
        );

        if (!trainings.isEmpty()) {
            trainings.forEach(t -> System.out.println( "\n" + ConsoleUtil.formatDto(t.toString()) ) );
        } else {
            System.out.println("No trainings found for the criteria passed");
        }
    }

    // Get Trainer Trainings List by trainer username and criteria
    public void getTrainingsByTrainerCriteria(String username,
                                                LocalDate fromDate,
                                                LocalDate toDate,
                                                String traineeUsername)
    {
        List<TrainingDto> trainings = trainingService.getTrainingsByTrainerCriteria(
                username, fromDate, toDate, traineeUsername
        );

        if (!trainings.isEmpty()) {
            trainings.forEach(t -> System.out.println( "\n" + ConsoleUtil.formatDto(t.toString()) ) );
        } else {
            System.out.println("No trainings found for the criteria passed");
        }
    }

    //* Training Type operations

    // Get all training types
    public String getTrainingTypes(){
        List<TrainingTypeDto> trainingTypeDtos = trainingTypeService.getTrainingTypes();
        
        String result = trainingTypeDtos.stream()
            .map(dto -> dto.getName())
            .collect(Collectors.joining("\n"));
        
        return result;
    }

}

