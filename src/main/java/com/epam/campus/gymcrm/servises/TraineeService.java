package com.epam.campus.gymcrm.servises;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.daos.TraineeDao;
import com.epam.campus.gymcrm.models.Trainee;
import com.epam.campus.gymcrm.models.User;

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
        List<User> existingUsers = traineeDao.getAll().stream()
            .map(t -> (User) t)
            .collect(Collectors.toList());

        trainee.generateUsername(existingUsers);
        trainee.generatePassword();

        traineeDao.save(trainee);
        logger.info("Trainee created: {} with username: {} and password: {}", trainee, trainee.getUsername(), trainee.getPassword());
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

