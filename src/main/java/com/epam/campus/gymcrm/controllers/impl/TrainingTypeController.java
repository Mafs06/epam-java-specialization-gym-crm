package com.epam.campus.gymcrm.controllers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.campus.gymcrm.controllers.ITrainingTypeController;
import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;

@RestController
@RequestMapping("/gym-crm/training-types")
public class TrainingTypeController implements ITrainingTypeController {
    TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    // Get all training types
    @Override
    @GetMapping
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {
        List<TrainingTypeDto> trainingTypeDtos = trainingTypeService.getTrainingTypes();
        return new ResponseEntity<>(trainingTypeDtos, HttpStatus.OK);
    }

}
