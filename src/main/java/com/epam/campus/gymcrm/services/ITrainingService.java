package com.epam.campus.gymcrm.services;

import java.time.LocalDate;
import java.util.List;

import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.entities.Training;

public interface ITrainingService {

    Training createTraining(TrainingDto training);

    List<TrainingDto> getTrainingsByTraineeCriteria(String username,
                                                    LocalDate fromDate,
                                                    LocalDate toDate,
                                                    String trainerUsername,
                                                    String trainingTypeName);

    List<TrainingDto>getTrainingsByTrainerCriteria(String username,
                                                    LocalDate fromDate,
                                                    LocalDate toDate,
                                                    String traineeUsername);
}
