package com.epam.campus.gymcrm.controllers.impl;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.campus.gymcrm.actuator.CustomMetrics;
import com.epam.campus.gymcrm.controllers.ITraineeController;
import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TraineeCreationDto;
import com.epam.campus.gymcrm.models.dtos.TraineeResponseDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;
import com.epam.campus.gymcrm.services.impl.TraineeService;

import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/gym-crm/trainees")
public class TraineeController implements ITraineeController{
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);
    private final TraineeService traineeService;
    private final CustomMetrics customMetrics;

    @Autowired
    public TraineeController(TraineeService traineeService, CustomMetrics customMetrics) {
        this.traineeService = traineeService;
        this.customMetrics = customMetrics;
    }

    // Create Trainee Profile
    @Override
    @PostMapping
    public ResponseEntity<LoginDto> createTrainee(@Valid @RequestBody TraineeCreationDto newTraineeDto) {
        try{
            LoginDto loginInfo = traineeService.createTrainee(newTraineeDto);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            customMetrics.addNewUser(); // Increment the new users metric
            return new ResponseEntity<>(loginInfo, HttpStatus.OK);

        } catch (PersistenceException e) {
            logger.error("There was an error and trainee could not be added.\n{}\n{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Trainee profile by username
    @Override
    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable String username) {
        

        try {
            TraineeResponseDto responseDto = traineeService.getTraineeByUsername(username);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Trainer with that username does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update Trainee profile
    @Override
    @PutMapping("/{username}")
    public ResponseEntity<TraineeUpdateResponseDto> updateTrainee(@PathVariable String username, @Valid @RequestBody TraineeUpdateRequestDto traineeUpdateDto) {
        

        try {
            TraineeUpdateResponseDto responseDto = traineeService.updateTrainee(username, traineeUpdateDto);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Validation failed and trainee could not be updated: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Change Trainee Password
    @Override
    @PutMapping("/{username}/password")
    public ResponseEntity<String> updateTraineePassword(@PathVariable String username, @RequestBody LoginChangeDto loginChangeDto) {
        

        try {
            if (traineeService.updateTraineePassword(username, loginChangeDto)) {
                customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
                return new ResponseEntity<>("Password changed.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.BAD_REQUEST);
            }

        } catch (NoSuchElementException | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.NOT_FOUND);
        }
    }

    // Activate/De-activate Trainee
    //! If not idempotent is wanted uncomment this version and comment the following one
    @Override
    @PatchMapping("/{username}/active")
    public ResponseEntity<String> switchTraineeActiveStatus(@PathVariable String username) {
        

        try {
            boolean setTo = traineeService.switchTraineeActiveStatus(username);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>("Active status changed to " + setTo, HttpStatus.OK);
        } catch (NoSuchElementException | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and active status could not be changed.", HttpStatus.NOT_FOUND);
        }
    }

    //! If idempotent is wanted uncomment this version and comment the previous one
    // @Override
    // @PatchMapping("/{username}/active")
    // public ResponseEntity<String> updateTraineeActiveStatus(@PathVariable String username, @RequestBody Map<String, Object> requestBody) {
    //     if (!SessionManager.isAuthenticated()) {
    //         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    //     }
    //
    //     try {
    //         boolean active = Boolean.parseBoolean(requestBody.get("active").toString());
    //         traineeService.updateActiveStatus(username, active);
    //         customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
    //         return new ResponseEntity<>("Active status updated.", HttpStatus.OK);
    //     } catch (NoSuchElementException | NoResultException e) {
    //         logger.error(e.getMessage());
    //         return new ResponseEntity<>("There was an error and active status could not be changed.", HttpStatus.NOT_FOUND);
    //     }
    // }

    // Update Trainee's trainers list
    @Override
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerFromListDto>> updateTraineeTrainers(@PathVariable String username, @RequestBody Map<String, List<String>> body) {
        

        try {
            List<String> newTrainers = body.get("newTrainers");
            List<TrainerFromListDto> trainers = traineeService.updateTraineeTrainersList(username, newTrainers);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>(trainers, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete Trainee
    @Override
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable String username) {
        

        try {
            traineeService.deleteTrainee(username);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>("Trainee deleted.", HttpStatus.OK);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("There was an error and trainee could not be deleted.", HttpStatus.NOT_FOUND);
        }
    }

    // Get Trainers list not assigned on trainee, by trainee's username
    @Override
    @GetMapping("{traineeUsername}/not-assigned-active-trainers")
    public ResponseEntity<List<TrainerFromListDto>> getTrainersNotAssignedToTrainee(@PathVariable String traineeUsername) {
        
        
        try {
            List<TrainerFromListDto> trainers = traineeService.getActiveTrainersNotAssignedToTrainee(traineeUsername);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>(trainers, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            logger.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
