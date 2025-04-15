package com.epam.campus.gymcrm.services;

import java.time.LocalDate;
import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingFromUserDto;
import com.epam.campus.gymcrm.models.entities.Training;

public interface ITrainingService {

    Training createTraining(TrainingDto training);

    List<TrainingFromUserDto> getTrainingsByTraineeAndCriteria(
        String username,
        LocalDate fromDate,
        LocalDate toDate,
        String trainerUsername,
        String trainingTypeName
    );

    List<TrainingFromUserDto>getTrainingsByTrainerAndCriteria(
        String username,
        LocalDate fromDate,
        LocalDate toDate,
        String traineeUsername
    );
}
