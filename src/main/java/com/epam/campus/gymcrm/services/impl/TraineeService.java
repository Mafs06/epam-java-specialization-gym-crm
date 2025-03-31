package com.epam.campus.gymcrm.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TraineeMapper;
import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.services.ITraineeService;
import com.epam.campus.gymcrm.utils.UserUtil;

import jakarta.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TraineeService implements ITraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeRepository traineeDao;
    private TraineeMapper mapper;


    @Autowired
    public TraineeService(TraineeRepository traineeDao, TraineeMapper mapper) {
        this.traineeDao = traineeDao;
        this.mapper = mapper;
    }
    
    @Override
    public TraineeDto getTrainee(int id) {
        logger.info("Fetching trainee with id: {}", id);
        Trainee trainee = traineeDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Trainee with id %s not found".formatted(id)));

        return mapper.toDto(trainee);
    }

    @Override
    public List<TraineeDto> getTrainees() {
        logger.info("Fetching all trainees");
        List<TraineeDto> traineesDtos = new ArrayList<>();
        traineeDao.getAll().forEach(trainee -> traineesDtos.add(mapper.toDto(trainee)));

        return traineesDtos;
    }

    @Override
    public void createTrainee(TraineeDto newTraineeDto) {
        try {
            String username = UserUtil.generateUsername(
                newTraineeDto.getFirstName(),
                newTraineeDto.getLastName(),
                traineeDao.getUsernames()
            );
            String password = UserUtil.generatePassword();

            User user = new User();
            user.setFirstName(newTraineeDto.getFirstName());
            user.setLastName(newTraineeDto.getLastName());
            user.setUsername(username);
            user.setPassword(password);
            user.setActive(newTraineeDto.isActive());

            Trainee trainee = mapper.toTrainee(newTraineeDto, username, password);

            traineeDao.save(trainee);
            System.out.println("Trainee created with username: " + trainee.getUser().getUsername());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error when adding trainee: {}", e.getMessage());
            System.err.println("There was an error and trainee could not be added. Verify specialization id data.");
            return;

        } catch (Exception e) {
            logger.error("Unexpected error while adding trainee: {}", e.getMessage(), e);
            System.err.println("There was an error and trainee could not be added.");
            return;
        }
    }

    @Override
    public void updateTrainee(String username, TraineeDto updatedTraineeDto) {
        logger.info("Updating trainee with username {}", username);

        Trainee existingTrainee;

        try {
            existingTrainee = traineeDao.getByUsername(username)
                .map(obj -> (Trainee) obj)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));

            mapper.toTrainee(updatedTraineeDto, existingTrainee);

            traineeDao.update(existingTrainee);
    
            logger.info("Updated trainee: {}", existingTrainee.toString());
            System.out.println("Trainee updated.");
    
        } catch (NoSuchElementException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            System.err.println("There was an error and trainee could not be updated.");
        }
    }

    @Override
    public void deleteTrainee(String username) {
        logger.info("Deleting trainee with username {}", username);

        Trainee trainee;
        
        try {
            trainee = traineeDao.getByUsername(username)
                .map(obj -> (Trainee) obj)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));

            traineeDao.delete(trainee);

            logger.info("Deleted trainee: {}", trainee.toString());
            System.out.println("Trainee deleted.");

        } catch (NoSuchElementException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            System.err.println("There was an error and trainee could not be deleted.");
        }
    }

    @Override
    public boolean traineeLogin(String username, String password) {
        logger.info("Attempting login for trainee with username: {}", username);

        Trainee trainee;
        try {
            trainee = traineeDao.getByUsername(username)
            .map(obj -> (Trainee) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            System.err.println("Username and password do not match. Try again.");
            return false;
        }
        

        String storedPassword = trainee.getUser().getPassword();

        if (storedPassword.equals(password)) {
            logger.info("Login successful for username: {}", username);
            System.out.println("Welcome " + username);
            return true;
        } else {
            logger.error("Incorrect password for username {}", username);
            System.err.println("Username and password do not match. Try again.");
            return false;
        }
    }

    @Override
    public TraineeDto getTraineeByUsername(String username) {
        logger.info("Fetching trainee with username: {}", username);

        Trainee trainee = (Trainee) traineeDao.getByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));

        return mapper.toDto(trainee);
    }

    @Override
    public void updateTraineePassword(String username, String newPassword) {
        logger.info("Updating password of trainee with username {}", username);

        try {
            traineeDao.updatePassword(username, newPassword);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            System.out.println("There was an error and password could not be changed.");
        }

        logger.info("Succesfull password change for trainee with username: {}", username);
        System.out.println("Password changed.");
    }

    @Override
    public void switchTraineeActiveStatus(String username) {
        logger.info("Updating active status on trainer with username {}", username);

        try {
            traineeDao.switchActiveStatus(username);
            System.out.println("Active status changed.");
        } catch (NoSuchElementException | NoResultException e) {
            logger.error(e.getMessage());
            System.err.println("There was an error and active status could not be changed.");
        }
    }



}
