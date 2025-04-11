package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.models.entities.User;

@Component
public class TrainerMapper {

    public TrainerDto toDto(Trainer trainer) {
        TrainerDto trainerDto = new TrainerDto(
            trainer.getUser().getFirstName(),
            trainer.getUser().getLastName(),
            trainer.getUser().isActive(),
            trainer.getTrainingType().getName());

        return trainerDto;
    }

    public Trainer toTrainer(TrainerDto trainerDto, TrainingType trainingType, String username, String password) {
        User user = new User(
            trainerDto.getFirstName(),
            trainerDto.getLastName(),
            username,
            password,
            trainerDto.isActive());

        return new Trainer.TrainerBuilder()
            .trainingType(trainingType)
            .user(user)
            .build();
    }

    public void updateTrainer(TrainerDto trainerDto, Trainer existingTrainer, TrainingType trainingType) {
        User updatedUser = existingTrainer.getUser();
        updatedUser.setFirstName(trainerDto.getFirstName());
        updatedUser.setLastName(trainerDto.getLastName());
        updatedUser.setActive(trainerDto.isActive());
    
        existingTrainer.setTrainingType(trainingType);
    }
    
}
