package com.epam.campus.gymcrm.services;

import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.models.entities.TrainingType;

public interface ITrainingTypeService {

    TrainingTypeDto getTrainingType(int id);

    void createTrainingType(TrainingTypeDto trainingTypeDto);

}
