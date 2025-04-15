package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingFromUserDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.models.entities.TrainingType;

@Component
public class TrainingMapper {

    public TrainingDto toDto(Training training) {
        TrainingDto trainingDto = new TrainingDto(
            training.getTrainee().getUser().getUsername(), 
            training.getTrainer().getUser().getUsername(),
            training.getName(),
            training.getTrainingType().getName(),
            training.getDate(), 
            training.getDuration());
            
        return trainingDto;
    }

    public Training toTraining(TrainingDto trainingDto, Trainee trainee, Trainer trainer, TrainingType trainingType) {
        Training training = new Training.TrainingBuilder()
            .trainee(trainee)
            .trainer(trainer)
            .name(trainingDto.getTrainingName())
            .trainingType(trainingType)
            .date(trainingDto.getTrainingDate())
            .duration(trainingDto.getTrainingDuration())
            .build();

        return training;
    }

    public TrainingFromUserDto toTrainingFromTraineeDto(Training training) {
        return new TrainingFromUserDto(
            training.getName(),
            training.getDate(), 
            training.getTrainingType().getName(), 
            training.getDuration(), 
            training.getTrainer().getUser().getUsername());
    }
}
