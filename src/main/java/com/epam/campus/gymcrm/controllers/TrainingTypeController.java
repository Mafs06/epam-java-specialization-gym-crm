package com.epam.campus.gymcrm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/gym-crm/training-types")
public class TrainingTypeController {
    TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    // Get all training types
    @Operation(summary = "Get all training types", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Training types retrieved successfully",
            content = @Content(schema = @Schema(implementation = TrainingTypeDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes(){
        List<TrainingTypeDto> trainingTypeDtos = trainingTypeService.getTrainingTypes();
        return new ResponseEntity<>(trainingTypeDtos, HttpStatus.OK);
    }

}
