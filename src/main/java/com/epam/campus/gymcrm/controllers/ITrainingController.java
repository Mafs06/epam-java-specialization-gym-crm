package com.epam.campus.gymcrm.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingFromUserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

public interface ITrainingController {

    @Operation(summary = "Create a training", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Training created",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Validation error",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "text/plain"))
    })
    ResponseEntity<String> createTraining(@Valid @RequestBody TrainingDto trainingDto);

    @Operation(summary = "Get trainings for a trainee based on optional filters", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainings retrieved",
            content = @Content(schema = @Schema(implementation = TrainingFromUserDto.class)))
    })
    ResponseEntity<List<TrainingFromUserDto>> getTrainingsByTraineeAndCriteria(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String trainingTypeName,
            @RequestParam(required = false) String trainerUsername
    );

    @Operation(summary = "Get trainings for a trainer based on optional filters", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainings retrieved",
            content = @Content(schema = @Schema(implementation = TrainingFromUserDto.class)))
    })
    ResponseEntity<List<TrainingFromUserDto>> getTrainingsByTrainerAndCriteria(
        @PathVariable String username,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
        @RequestParam(required = false) String trainingTypeName,
        @RequestParam(required = false) String traineeUsername
    );
}
