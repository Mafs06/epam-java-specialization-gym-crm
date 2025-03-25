package com.epam.campus.gymcrm.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TrainingMapper;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.repositories.TrainingRepository;
import com.epam.campus.gymcrm.services.ITrainingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainingService implements ITrainingService{

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private TrainingRepository trainingDao;
    private TrainingMapper mapper;

    @Autowired
    public void setTrainingDao(TrainingRepository trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Autowired
    public void setMappper(TrainingMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TrainingDto getTraining(int id) {
        logger.info("Fetching training with ID: {}", id);
        Training training = trainingDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Training with id %s not found".formatted(id)));

        return mapper.toDto(training);
    }

    @Override
    public List<TrainingDto> getTrainings() {
        logger.info("Fetching all trainings");
        List<TrainingDto> trainingDtos = new ArrayList<>();
        trainingDao.getAll().forEach(training -> trainingDtos.add(mapper.toDto(training)));
        return trainingDtos;
    }

    @Override
    public void createTraining(TrainingDto trainingDto) {
        trainingDao.save(mapper.toTraining(trainingDto));
        logger.info("Training created");
    }

    @Override
    public void updateTraining(int id, TrainingDto updatedTrainingDto) {
        Training existingTraining = trainingDao.get(id).orElseThrow(() -> new NoSuchElementException("Training with id %s not found".formatted(id)));

        Training updatedTraining = mapper.toTraining(updatedTrainingDto, existingTraining);

        trainingDao.update(updatedTraining);
        logger.info("Training updated");
    }

    @Override
    public void deleteTraining(int id) {
        Training training = trainingDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Training with id %s not found".formatted(id)));
        trainingDao.delete(training);
        logger.info("Training deleted");
    }
}
