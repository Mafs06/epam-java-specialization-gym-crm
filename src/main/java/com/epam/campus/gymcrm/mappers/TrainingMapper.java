package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.entities.Training;

@Component
public class TrainingMapper {

    public TrainingDto toDto(Training training) {
        TrainingDto trainingDto = new TrainingDto(
            training.getTrainee().getId(), 
            training.getTrainer().getId(), 
            training.getName(),
            training.getTrainingType().getId(),
            training.getDate(), 
            training.getDuration());
            
        return trainingDto;
    }

    public Training toTraining(TrainingDto trainingDto) {
        Training training = new Training.TrainingBuilder()
            .build();

        return training;
    }

    public Training toTraining(TrainingDto trainingDto, Training existingTraining) {
        Training training = new Training.TrainingBuilder()
            .build();

        return training;
    }
}
