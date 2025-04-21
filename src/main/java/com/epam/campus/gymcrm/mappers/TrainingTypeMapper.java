package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.models.entities.TrainingType;

@Component
public class TrainingTypeMapper {

    public TrainingTypeDto toDto(TrainingType trainingType) {
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto(trainingType.getName());

        return trainingTypeDto;
    }

    public TrainingType toTrainingType(TrainingTypeDto trainingTypeDto) {
        TrainingType trainingType = new TrainingType();
        trainingType.setName(trainingTypeDto.getName());

        return trainingType;
    }

    public TrainingType toTrainingType(TrainingTypeDto trainingTypeDto, TrainingType existingTrainingType) {
        TrainingType updatedTrainingType = new TrainingType();
        updatedTrainingType.setId(existingTrainingType.getId());
        updatedTrainingType.setName(trainingTypeDto.getName());

        return updatedTrainingType;
    }

}
