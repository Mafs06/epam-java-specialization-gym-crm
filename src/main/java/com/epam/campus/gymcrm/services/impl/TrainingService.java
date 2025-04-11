package com.epam.campus.gymcrm.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TrainingMapper;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.repositories.TrainingRepository;
import com.epam.campus.gymcrm.repositories.TrainingTypeRepository;
import com.epam.campus.gymcrm.services.ITrainingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainingService implements ITrainingService{

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private TrainingRepository trainingRepository;
    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingMapper mapper;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, TrainingMapper mapper) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public Training createTraining(TrainingDto newTrainingDto) {
        logger.info("Adding new training with data: {}", newTrainingDto.toString());

        Trainee trainee = (Trainee) traineeRepository.getByUsername(newTrainingDto.getTraineeUsername())
            .orElseThrow(() -> new NoSuchElementException("Invalid trainee username"));
        Trainer trainer = (Trainer) trainerRepository.getByUsername(newTrainingDto.getTrainerUsername())
            .orElseThrow(() -> new NoSuchElementException("Invalid trainer username"));
        TrainingType trainingType = trainingTypeRepository.getByName(newTrainingDto.getTrainingTypeName())
            .orElseThrow(() -> new NoSuchElementException("Invalid training type name"));
        
        Training training = mapper.toTraining(newTrainingDto, trainee, trainer, trainingType);

        trainingRepository.save(training);
        logger.info("Training created: {}", training);
        return training;
    }

    @Override
    public List<TrainingDto> getTrainingsByTraineeCriteria(String username,
                                                            LocalDate fromDate,
                                                            LocalDate toDate,
                                                            String trainerUsername,
                                                            String trainingTypeName)
    {
        List<Training> trainings = trainingRepository.findTrainingsByTraineeCriteria(username, fromDate, toDate, trainerUsername, trainingTypeName);
        return trainings.stream().map(t -> mapper.toDto(t)).toList();
    }

    @Override
    public List<TrainingDto> getTrainingsByTrainerCriteria(String username,
                                                            LocalDate fromDate,
                                                            LocalDate toDate,
                                                            String traineeUsername)
    {
                List<Training> trainings = trainingRepository.findTrainingsByTrainerCriteria(username, fromDate, toDate, traineeUsername);
                return trainings.stream().map(t -> mapper.toDto(t)).toList();
    }
}
