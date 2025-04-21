package com.epam.campus.gymcrm.controllers;

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

import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TraineeCreationDto;
import com.epam.campus.gymcrm.models.dtos.TraineeResponseDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;
import com.epam.campus.gymcrm.services.impl.TraineeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/gym-crm/trainees")
public class TraineeController {
    private static final Logger logger = LoggerFactory.getLogger(TraineeController.class);
    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    // Login as Trainee
    @Operation(summary = "Login as Trainee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/login")
    public ResponseEntity<String> traineeLogin(@Valid @RequestBody LoginDto loginDto) {
        try {
            if (traineeService.traineeLogin(loginDto)) {
                return new ResponseEntity<>("Welcome " + loginDto.getUsername(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Username and password do not match.", HttpStatus.UNAUTHORIZED);
            }

        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Username and password do not match. Try again.", HttpStatus.UNAUTHORIZED);
        }
    }

    // Create Trainee Profile
    @Operation(summary = "Create a new Trainee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee created",
            content = @Content(schema = @Schema(implementation = LoginDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<LoginDto> createTrainee(@Valid @RequestBody TraineeCreationDto newTraineeDto) {
        try{
            LoginDto loginInfo = traineeService.createTrainee(newTraineeDto);
            return new ResponseEntity<>(loginInfo, HttpStatus.OK);

        } catch (PersistenceException e) {
            logger.error("There was an error and trainee could not be added.\n{}\n{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get Trainee profile by username
    @Operation(summary = "Get trainee profile by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee profile found",
            content = @Content(schema = @Schema(implementation = TraineeResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable String username) {
        try {
            TraineeResponseDto responseDto = traineeService.getTraineeByUsername(username);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Trainer with that username does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update Trainee profile
    @Operation(summary = "Update trainee profile", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee updated",
            content = @Content(schema = @Schema(implementation = TraineeUpdateResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @PutMapping("/{username}")
    public ResponseEntity<TraineeUpdateResponseDto> updateTrainee(@PathVariable String username, @Valid @RequestBody TraineeUpdateRequestDto traineeUpdateDto) {
        try {
            TraineeUpdateResponseDto responseDto = traineeService.updateTrainee(username, traineeUpdateDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Validation failed and trainee could not be updated: ", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Change Trainee Password
    @Operation(summary = "Change trainee password", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "Unauthorized or invalid password", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @PutMapping("/{username}/password")
    public ResponseEntity<String> updateTraineePassword(@PathVariable String username, @RequestBody LoginChangeDto loginChangeDto) {
        try {
            if (traineeService.updateTraineePassword(username, loginChangeDto)) {
                return new ResponseEntity<>("Password changed.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.UNAUTHORIZED);
            }

        } catch (NoSuchElementException | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.NOT_FOUND);
        }
    }

    // Activate/De-activate Trainee
    //! If not idempotent is wanted uncomment this version and comment the following one
    @Operation(summary = "Switch trainee active status", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active status changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @PatchMapping("/{username}/active")
    public ResponseEntity<String> switchTraineeActiveStatus(@PathVariable String username) {
        try {
            boolean setTo = traineeService.switchTraineeActiveStatus(username);
            return new ResponseEntity<>("Active status changed to " + setTo, HttpStatus.OK);
        } catch (NoSuchElementException | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and active status could not be changed.", HttpStatus.NOT_FOUND);
        }
    }

    //! If idempotent is wanted uncomment this version and comment the previous one
    // @PatchMapping("/{username}/active")
    // public ResponseEntity<String> updateTraineeActiveStatus(@PathVariable String username, @RequestBody Map<String, Object> requestBody) {
    //     try {
    //         boolean active = Boolean.parseBoolean(requestBody.get("active").toString());
    //         traineeService.updateActiveStatus(username, active);
    //         return new ResponseEntity<>("Active status updated.", HttpStatus.OK);
    //     } catch (NoSuchElementException | NoResultException e) {
    //         logger.error(e.getMessage());
    //         return new ResponseEntity<>("There was an error and active status could not be changed.", HttpStatus.NOT_FOUND);
    //     }
    // }

    // Update Trainee's trainers list
    @Operation(summary = "Update trainee's trainers list", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer list updated",
            content = @Content(schema = @Schema(implementation = TrainerFromListDto.class))),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerFromListDto>> updateTraineeTrainers(@PathVariable String username, @RequestBody Map<String, List<String>> body) {
        try {
            List<String> newTrainers = body.get("newTrainers");
            List<TrainerFromListDto> trainers = traineeService.updateTraineeTrainersList(username, newTrainers);
            return new ResponseEntity<>(trainers, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete Trainee
    @Operation(summary = "Delete trainee by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee deleted", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable String username) {
        try {
            traineeService.deleteTrainee(username);
            return new ResponseEntity<>("Trainee deleted.", HttpStatus.OK);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("There was an error and trainee could not be deleted.", HttpStatus.NOT_FOUND);
        }
    }

    // Get Trainers list not assigned on trainee, by trainee's username
    @Operation(summary = "Get list of active trainers not assigned to a trainee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainers list retrieved",
            content = @Content(schema = @Schema(implementation = TrainerFromListDto.class))),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    @GetMapping("{traineeUsername}/not-assigned-active-trainers")
    public ResponseEntity<List<TrainerFromListDto>> getTrainersNotAssignedToTrainee(@PathVariable String traineeUsername) {
        try {
            List<TrainerFromListDto> trainers = traineeService.getActiveTrainersNotAssignedToTrainee(traineeUsername);
            return new ResponseEntity<>(trainers, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            logger.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
