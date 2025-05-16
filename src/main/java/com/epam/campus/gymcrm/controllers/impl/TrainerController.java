package com.epam.campus.gymcrm.controllers.impl;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.campus.gymcrm.actuator.CustomMetrics;
import com.epam.campus.gymcrm.controllers.ITrainerController;
import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TrainerCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateResponseDto;
import com.epam.campus.gymcrm.services.impl.TrainerService;

import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/gym-crm/trainers")
public class TrainerController implements ITrainerController {
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    private final TrainerService trainerService;
    private final CustomMetrics customMetrics;

    @Autowired
    public TrainerController(TrainerService trainerService, CustomMetrics customMetrics) {
        this.trainerService = trainerService;
        this.customMetrics = customMetrics;
    }

    // Create Trainer profile
    @Override
    @PostMapping
    public ResponseEntity<LoginDto> createTrainer(@Valid @RequestBody TrainerCreationDto newTrainerDto) {
        try {
            LoginDto loginInfo = trainerService.createTrainer(newTrainerDto);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            customMetrics.addNewUser(); // Increment the new users metric
            return new ResponseEntity<>(loginInfo, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            logger.error("There was an error and trainer could not be added. Verify specialization data.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (PersistenceException e) {
            logger.error("There was an error and trainer could not be added.\n{}\n{}", e.getClass(), e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Trainer profile by username
    @Override
    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> getTrainerByUsername(@PathVariable String username) {
        

        try {
            TrainerResponseDto responseDto = trainerService.getTrainerByUsername(username);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Trainer with that username does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    // Update Trainer profile
    @Override
    @PutMapping("/{username}")
    public ResponseEntity<TrainerUpdateResponseDto> updateTrainer(@PathVariable String username, @Valid @RequestBody TrainerUpdateRequestDto trainerUpdateDto) {
        

        try {
            TrainerUpdateResponseDto responseDto = trainerService.updateTrainer(username, trainerUpdateDto);
            customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Validation failed and trainer could not be updated: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    // Change Trainer Password
    @Override
    @PutMapping("/{username}/password")
    public ResponseEntity<String> updateTrainerPassword(@PathVariable String username, @RequestBody LoginChangeDto loginChangeDto) {
        

        try {
            if (trainerService.updateTrainerPassword(username, loginChangeDto)) {
                customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
                return new ResponseEntity<>("Password changed.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.BAD_REQUEST);
            }
            
        } catch (NoSuchElementException  | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.NOT_FOUND);
        }
    }

    // Activate/De-activate Trainer
    //! If not idempotent is wanted uncomment this version and comment the following one
    @Override
    @PatchMapping("/{username}/active")
    public ResponseEntity<String> switchTrainerActiveStatus(@PathVariable String username) {
        

        try {
            boolean setTo = trainerService.switchTrainerActiveStatus(username);
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
    // public ResponseEntity<String> updateTrainerActiveStatus(@PathVariable String username, @RequestBody Map<String, Object> requestBody) {
    //     if (!SessionManager.isAuthenticated()) {
    //         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    //     }
    //
    //     try {
    //         boolean active = Boolean.parseBoolean(requestBody.get("active").toString());
    //         trainerService.updateActiveStatus(username, active);
    //         customMetrics.incrementSuccessfulOperation(); // Increment the successful login metric
    //         return new ResponseEntity<>("Active status updated.", HttpStatus.OK);
    //     } catch (NoSuchElementException | NoResultException e) {
    //         logger.error(e.getMessage());
    //         return new ResponseEntity<>("There was an error and active status could not be changed.", HttpStatus.NOT_FOUND);
    //     }
    // }
}
