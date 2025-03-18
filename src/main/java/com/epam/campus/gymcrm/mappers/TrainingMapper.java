package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.entities.Training;

@Component
public class TrainingMapper {

    public TrainingDto toDto(Training training) {
        TrainingDto trainingDto = new TrainingDto(
            training.getId(), 
            training.getTraineeID(), 
            training.getTrainerID(), 
            training.getTrainingName(), 
            training.getTrainingDate(), 
            training.getTrainingDuration());
            
        return trainingDto;
    }

    public Training toTraining(TrainingDto trainingDto) {
        Training training = new Training.TrainingBuilder()
            .id(trainingDto.getId())
            .traineeID(trainingDto.getTraineeID())
            .trainerID(trainingDto.getTrainerID())
            .trainingName(trainingDto.getTrainingName())
            .trainingDate(trainingDto.getTrainingDate())
            .trainingDuration(trainingDto.getTrainingDuration())
            .build();

        return training;
    }

    public Training toTraining(TrainingDto trainingDto, Training existingTraining) {
        Training training = new Training.TrainingBuilder()
            .id(existingTraining.getId()) // Keep id
            .traineeID(trainingDto.getTraineeID())
            .trainerID(trainingDto.getTrainerID())
            .trainingName(trainingDto.getTrainingName())
            .trainingDate(trainingDto.getTrainingDate())
            .trainingDuration(trainingDto.getTrainingDuration())
            .build();

        return training;
    }
}
