package com.epam.campus.gymcrm.controllers.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.campus.gymcrm.controllers.ITrainingController;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingFromUserDto;
import com.epam.campus.gymcrm.services.impl.TrainingService;

import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/gym-crm")
public class TrainingController implements ITrainingController {
    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);
    TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    // Create training
    @Override
    @PostMapping("/trainings")
    public ResponseEntity<String> createTraining(@Valid @RequestBody TrainingDto trainingDto) {
        try {
            trainingService.createTraining(trainingDto);
            return new ResponseEntity<>("Training created.", HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Validation error when adding training: {}", e.getMessage());
            return new ResponseEntity<>("There was a validation error and training could not be added.", HttpStatus.NOT_FOUND);

        } catch (PersistenceException e) {
            logger.error("Unexpected error while adding training: {} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and training could not be added.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Trainee Trainings List by trainee username and criteria
    @Override
    @GetMapping("/trainees/{username}/trainings")
    public ResponseEntity<List<TrainingFromUserDto>> getTrainingsByTraineeAndCriteria(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String trainingTypeName,
            @RequestParam(required = false) String trainerUsername
    ) {
        List<TrainingFromUserDto> trainings = trainingService.getTrainingsByTraineeAndCriteria(
                username,
                fromDate,
                toDate,
                trainerUsername,
                trainingTypeName
        );
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    // Get Trainer Trainings List by trainer username and criteria
    @Override
    @GetMapping("/trainers/{username}/trainings")
    public ResponseEntity<List<TrainingFromUserDto>> getTrainingsByTrainerAndCriteria(
        @PathVariable String username,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
        @RequestParam(required = false) String trainingTypeName,
        @RequestParam(required = false) String traineeUsername
    ) {
        List<TrainingFromUserDto> trainings = trainingService.getTrainingsByTrainerAndCriteria(
                username,
                fromDate,
                toDate,
                traineeUsername
        );

        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }
}
