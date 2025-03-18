package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TrainingDto;

public interface ITrainingService {

    TrainingDto getTraining(int id);

    List<TrainingDto> getTrainings();

    void createTraining(TrainingDto training);

    void updateTraining(int id, TrainingDto training);

    void deleteTraining(int id);

}
