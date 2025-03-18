package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.entities.Trainer;

@Component
public class TrainerMapper {

    public TrainerDto toDto(Trainer trainer) {
        TrainerDto trainerDto = new TrainerDto(
            trainer.getId(),
            trainer.getFirstName(),
            trainer.getLastName(),
            trainer.isActive(),
            trainer.getSpecialization());

        return trainerDto;
    }

    public Trainer toTrainer(TrainerDto trainerDto) {
        // username and password not set
        Trainer trainer = new Trainer.TrainerBuilder()
            .id(trainerDto.getId())
            .firstName(trainerDto.getFirstName())
            .lastName(trainerDto.getLastName())
            .active(trainerDto.isActive())
            .specialization(trainerDto.getSpecialization())
            .build();

        return trainer;
    }

    public Trainer toTrainer(TrainerDto trainerDto, Trainer existingTrainer) {
        Trainer updatedTrainer = new Trainer.TrainerBuilder()
            .id(existingTrainer.getId()) // Keep id
            .firstName(trainerDto.getFirstName())
            .lastName(trainerDto.getLastName())
            .username(existingTrainer.getUsername()) // Keep username
            .password(existingTrainer.getPassword()) // Keep password
            .active(trainerDto.isActive())
            .specialization(trainerDto.getSpecialization())
            .build();

        return updatedTrainer;
    }
}
