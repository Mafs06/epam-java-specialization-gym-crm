package com.epam.campus.gymcrm.servises;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.daos.TrainingDao;
import com.epam.campus.gymcrm.models.Training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Optional<Training> getTraining(int id) {
        logger.info("Fetching training with ID: {}", id);
        return trainingDao.get(id);
    }

    public List<Training> getTrainings() {
        logger.info("Fetching all trainings");
        return trainingDao.getAll();
    }

    public void createTraining(Training training) {
        trainingDao.save(training);
        logger.info("Training created: {}", training);
    }

    public void updateTraining(Training training, String[] params) {
        trainingDao.update(training, params);
        logger.info("Training updated: {}", training);
    }

    public void deleteTraining(Training training) {
        trainingDao.delete(training);
        logger.info("Training deleted: {}", training);
    }
}
