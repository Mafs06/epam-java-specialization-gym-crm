package com.epam.campus.gymcrm.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TrainerCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

public interface ITrainerController {

    @Operation(summary = "Create a new Trainer", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer created",
            content = @Content(schema = @Schema(implementation = LoginDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content)
    })
    ResponseEntity<LoginDto> createTrainer(@Valid @RequestBody TrainerCreationDto newTrainerDto);

    @Operation(summary = "Get trainer profile by username", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer profile found",
            content = @Content(schema = @Schema(implementation = TrainerResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    ResponseEntity<TrainerResponseDto> getTrainerByUsername(@PathVariable String username);

    @Operation(summary = "Update trainer profile", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Trainer updated",
            content = @Content(schema = @Schema(implementation = TrainerUpdateResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    ResponseEntity<TrainerUpdateResponseDto> updateTrainer(@PathVariable String username, @Valid @RequestBody TrainerUpdateRequestDto trainerUpdateDto);

    @Operation(summary = "Change trainer password", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Invalid old password", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    ResponseEntity<String> updateTrainerPassword(@PathVariable String username, @RequestBody LoginChangeDto loginChangeDto);

    //! If not idempotent is wanted uncomment this version and comment the following one
    @Operation(summary = "Switch trainer active status", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Active status changed", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    ResponseEntity<String> switchTrainerActiveStatus(@PathVariable String username);

    //! If idempotent is wanted uncomment this version and comment the previous one
    // @Operation(summary = "Update the active status of a trainer",security = @SecurityRequirement(name = "bearerAuth"))
    // @ApiResponses(value = {
    //     @ApiResponse(responseCode = "200", description = "Active status updated successfully",
    //         content = @Content(mediaType = "text/plain")),
    //     @ApiResponse(responseCode = "401", description = "User not authenticated",
    //         content = @Content(mediaType = "text/plain")),
    //     @ApiResponse(responseCode = "404", description = "Trainer not found or update failed",
    //         content = @Content(mediaType = "text/plain"))
    // })
    // ResponseEntity<String> updateTrainerActiveStatus(@PathVariable String username, @RequestBody Map<String, Object> requestBody);
}
