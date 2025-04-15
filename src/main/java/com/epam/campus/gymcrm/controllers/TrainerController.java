package com.epam.campus.gymcrm.controllers;

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

import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TrainerCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateResponseDto;
import com.epam.campus.gymcrm.services.impl.TrainerService;

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
@RequestMapping("/gym-crm/trainers")
public class TrainerController {
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    // Login as Trainer
    @Operation(summary = "Login as Trainer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/login")
    public  ResponseEntity<String> trainerLogin(@Valid @RequestBody LoginDto loginDto) {
        try {
            if (trainerService.trainerLogin(loginDto)) {
                return new ResponseEntity<>("Welcome " + loginDto.getUsername(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Username and password do not match.", HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("Username and password do not match.", HttpStatus.UNAUTHORIZED);
        }
    }

    // Create Trainer profile
    @Operation(summary = "Create a new Trainer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer created",
            content = @Content(schema = @Schema(implementation = LoginDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<LoginDto> createTrainer(@Valid @RequestBody TrainerCreationDto newTrainerDto) {
        try {
            LoginDto loginInfo = trainerService.createTrainer(newTrainerDto);
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
    @Operation(summary = "Get trainer profile by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer profile found",
            content = @Content(schema = @Schema(implementation = TrainerResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponseDto> getTrainerByUsername(@PathVariable String username) {
        try {
            TrainerResponseDto responseDto = trainerService.getTrainerByUsername(username);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Trainer with that username does not exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    // Update Trainer profile
    @Operation(summary = "Update trainer profile", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer updated",
            content = @Content(schema = @Schema(implementation = TrainerUpdateResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    @PutMapping("/{username}")
    public ResponseEntity<TrainerUpdateResponseDto> updateTrainer(@PathVariable String username, @Valid @RequestBody TrainerUpdateRequestDto trainerUpdateDto) {
        try {
            TrainerUpdateResponseDto responseDto = trainerService.updateTrainer(username, trainerUpdateDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Validation failed and trainer could not be updated: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
    }

    // Change Trainer Password
    @Operation(summary = "Change trainer password", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "Unauthorized or invalid password", content = @Content),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    @PutMapping("/{username}/password")
    public ResponseEntity<String> updateTrainerPassword(@PathVariable String username, @RequestBody LoginChangeDto loginChangeDto) {
        try {
            if (trainerService.updateTrainerPassword(username, loginChangeDto)) {
                return new ResponseEntity<>("Password changed.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.UNAUTHORIZED);
            }
            
        } catch (NoSuchElementException  | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and password could not be changed.", HttpStatus.NOT_FOUND);
        }
    }

    // Activate/De-activate Trainer
    //! If not idempotent is wanted uncomment this version and comment the following one
    @Operation(summary = "Switch trainer active status", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active status changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    @PatchMapping("/{username}/active")
    public ResponseEntity<String> switchTrainerActiveStatus(@PathVariable String username) {
        try {
            boolean setTo = trainerService.switchTrainerActiveStatus(username);
            return new ResponseEntity<>("Active status changed to " + setTo, HttpStatus.OK);
        } catch (NoSuchElementException | NoResultException e) {
            logger.error("{} {}", e.getClass(), e.getMessage());
            return new ResponseEntity<>("There was an error and active status could not be changed.", HttpStatus.NOT_FOUND);
        }  
    }

    //! If idempotent is wanted uncomment this version and comment the previous one
    // @PatchMapping("/{username}/active")
    // public ResponseEntity<String> updateTrainerActiveStatus(@PathVariable String username, @RequestBody Map<String, Object> requestBody) {
    //     try {
    //         boolean active = Boolean.parseBoolean(requestBody.get("active").toString());
    //         trainerService.updateActiveStatus(username, active);
    //         return new ResponseEntity<>("Active status updated.", HttpStatus.OK);
    //     } catch (NoSuchElementException | NoResultException e) {
    //         logger.error(e.getMessage());
    //         return new ResponseEntity<>("There was an error and active status could not be changed.", HttpStatus.NOT_FOUND);
    //     }
    // }
}
