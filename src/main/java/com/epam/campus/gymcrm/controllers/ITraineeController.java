package com.epam.campus.gymcrm.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TraineeCreationDto;
import com.epam.campus.gymcrm.models.dtos.TraineeResponseDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

public interface ITraineeController {

    @Operation(summary = "Login as Trainee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Invalid credentials",
            content = @Content(mediaType = "text/plain"))
    })
    ResponseEntity<String> traineeLogin(@Valid @RequestBody LoginDto loginDto);

    @Operation(summary = "Create a new Trainee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee created",
            content = @Content(schema = @Schema(implementation = LoginDto.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content)
    })
    ResponseEntity<LoginDto> createTrainee(@Valid @RequestBody TraineeCreationDto newTraineeDto);

    @Operation(summary = "Get trainee profile by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee profile found",
            content = @Content(schema = @Schema(implementation = TraineeResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    ResponseEntity<TraineeResponseDto> getTraineeByUsername(@PathVariable String username);

    @Operation(summary = "Update trainee profile", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee updated",
            content = @Content(schema = @Schema(implementation = TraineeUpdateResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    ResponseEntity<TraineeUpdateResponseDto> updateTrainee(@PathVariable String username, @Valid @RequestBody TraineeUpdateRequestDto traineeUpdateDto);

    @Operation(summary = "Change trainee password", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Invalid old password", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    ResponseEntity<String> updateTraineePassword(@PathVariable String username, @RequestBody LoginChangeDto loginChangeDto);

    //! If not idempotent is wanted uncomment this version and comment the following one
    @Operation(summary = "Switch trainee active status", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active status changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    ResponseEntity<String> switchTraineeActiveStatus(@PathVariable String username);

    
    //! If idempotent is wanted uncomment this version and comment the previous one
    // @Operation( summary = "Update the active status of a trainee", security = @SecurityRequirement(name = "bearerAuth"))
    // @ApiResponses(value = {
    //     @ApiResponse(responseCode = "200", description = "Active status updated successfully",
    //         content = @Content(mediaType = "text/plain")),
    //     @ApiResponse(responseCode = "401", description = "User not authenticated",
    //         content = @Content(mediaType = "text/plain")),
    //     @ApiResponse(responseCode = "404", description = "Trainee not found or update failed",
    //         content = @Content(mediaType = "text/plain"))
    // })
    // ResponseEntity<String> updateTraineeActiveStatus(@PathVariable String username, @RequestBody Map<String, Object> requestBody);

    @Operation(summary = "Update trainee's trainers list", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer list updated",
            content = @Content(schema = @Schema(implementation = TrainerFromListDto.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    ResponseEntity<List<TrainerFromListDto>> updateTraineeTrainers(@PathVariable String username, @RequestBody Map<String, List<String>> body);

    @Operation(summary = "Delete trainee by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainee deleted", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    ResponseEntity<String> deleteTrainee(@PathVariable String username);

    @Operation(summary = "Get list of active trainers not assigned to a trainee", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainers list retrieved",
            content = @Content(schema = @Schema(implementation = TrainerFromListDto.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainee not found", content = @Content)
    })
    ResponseEntity<List<TrainerFromListDto>> getTrainersNotAssignedToTrainee(@PathVariable String traineeUsername);
}
