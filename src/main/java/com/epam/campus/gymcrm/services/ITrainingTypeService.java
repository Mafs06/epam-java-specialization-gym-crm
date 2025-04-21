package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;

public interface ITrainingTypeService {

    TrainingTypeDto getTrainingTypeByName(String name);

    List<TrainingTypeDto> getTrainingTypes();

    void createTrainingType(TrainingTypeDto trainingTypeDto);

}
