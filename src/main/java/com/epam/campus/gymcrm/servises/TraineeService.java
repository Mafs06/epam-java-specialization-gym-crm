package com.epam.campus.gymcrm.servises;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.daos.TraineeDao;
import com.epam.campus.gymcrm.models.Trainee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    
    private TraineeDao traineeDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Optional<Trainee> getTrainee(int id) {
        logger.info("Fetching trainee with id: {}", id);
        return traineeDao.get(id);
    }

    public List<Trainee> getTrainees() {
        logger.info("Fetching all trainees");
        return traineeDao.getAll();
    }

    public void createTrainee(Trainee trainee) {
        traineeDao.save(trainee);
        logger.info("Trainee created: {}", trainee);
    }

    public void updateTrainee(Trainee trainee, String[] params) {
        traineeDao.update(trainee, params);
        logger.info("Trainee updated: {} with params: {}", trainee, params);
    }

    public void deleteTrainee(Trainee trainee) {
        traineeDao.delete(trainee);
        logger.info("Trainee deleted: {}", trainee);
    }
}
