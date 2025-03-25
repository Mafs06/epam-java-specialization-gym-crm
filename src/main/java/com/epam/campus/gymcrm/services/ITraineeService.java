package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;

public interface ITraineeService {

    TraineeDto getTrainee(int id);

    List<TraineeDto> getTrainees();

    void createTrainee(TraineeDto traineeDto);

    void updateTrainee(int id, TraineeDto traineeDtos);

    void deleteTrainee(int id);

    boolean traineeLogin(String username, String password);

    TraineeDto getTraineeByUsername(String username);

    void updateTraineePassword(String username, String newPassword);

    void switchTrainerActiveStatus(String username);
}
