package com.epam.campus.gymcrm.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

public interface ITrainingTypeController {

    @Operation(summary = "Get all training types", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Training types retrieved successfully",
            content = @Content(schema = @Schema(implementation = TrainingTypeDto.class)))
    })
    ResponseEntity<List<TrainingTypeDto>> getTrainingTypes();

}
