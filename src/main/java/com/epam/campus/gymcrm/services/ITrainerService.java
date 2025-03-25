package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TrainerDto;

public interface ITrainerService {

    TrainerDto getTrainer(int id);

    List<TrainerDto> getTrainers();

    void createTrainer(TrainerDto trainerDto);

    void updateTrainer(String username, TrainerDto updatedTrainerDto);

    void deleteTrainer(int username);

    boolean trainerLogin(String username, String password);

    TrainerDto getTrainerByUsername(String username);

    void updateTrainerPassword(String username, String newPassword);

    void switchTrainerActiveStatus(String username);
}
