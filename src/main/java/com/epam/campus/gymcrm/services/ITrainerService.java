package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TrainerDto;

public interface ITrainerService {

    TrainerDto getTrainer(int id);

    List<TrainerDto> getTrainers();

    void createTrainer(TrainerDto trainerDto);

    void updateTrainer(int id, TrainerDto updatedTrainerDto);

    void deleteTrainer(int id);

    boolean trainerLogin(String username, String password);

    TrainerDto getTrainerByUsername(String username);

    void updateTrainerPassword(String username, String newPassword);
}
