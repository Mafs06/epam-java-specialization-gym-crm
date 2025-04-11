package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.entities.Trainer;

public interface ITrainerService {

    Trainer createTrainer(TrainerDto trainerDto);

    void updateTrainer(String username, TrainerDto updatedTrainerDto);

    boolean trainerLogin(String username, String password);

    TrainerDto getTrainerByUsername(String username);

    void updateTrainerPassword(String username, String newPassword);

    void switchTrainerActiveStatus(String username);

    List<TrainerDto> getTrainersNotAssignedToTrainee(String traineeUsername);
}
