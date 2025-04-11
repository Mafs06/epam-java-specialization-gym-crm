package com.epam.campus.gymcrm.services;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.entities.Trainee;

public interface ITraineeService {

    Trainee createTrainee(TraineeDto traineeDto);

    void updateTrainee(String username, TraineeDto traineeDtos);

    void deleteTrainee(String username);

    boolean traineeLogin(String username, String password);

    TraineeDto getTraineeByUsername(String username);

    void updateTraineePassword(String username, String newPassword);

    void switchTraineeActiveStatus(String username);
}
