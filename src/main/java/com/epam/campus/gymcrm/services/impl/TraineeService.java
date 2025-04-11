package com.epam.campus.gymcrm.services.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TraineeMapper;
import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.services.ITraineeService;
import com.epam.campus.gymcrm.utils.JPAUtil;
import com.epam.campus.gymcrm.utils.UserUtil;

import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TraineeService implements ITraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeRepository traineeRepository;
    private TraineeMapper mapper;


    @Autowired
    public TraineeService(TraineeRepository traineeRepository, TraineeMapper mapper) {
        this.traineeRepository = traineeRepository;
        this.mapper = mapper;
    }

    @Override
    public Trainee createTrainee(TraineeDto newTraineeDto) {
        logger.info("Adding new trainee with data: {}", newTraineeDto);

        String username = UserUtil.generateUsername(
            newTraineeDto.getFirstName(),
            newTraineeDto.getLastName(),
            traineeRepository.getUsernames()
        );
        String password = UserUtil.generatePassword();

        User user = new User();
        user.setFirstName(newTraineeDto.getFirstName());
        user.setLastName(newTraineeDto.getLastName());
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(newTraineeDto.isActive());

        Trainee trainee = mapper.toTrainee(newTraineeDto, username, password);

        traineeRepository.save(trainee);
        logger.info("Trainee created: {}", trainee);
        return trainee;
    }

    @Override
    public void updateTrainee(String username, TraineeDto updatedTraineeDto) {
        logger.info("Updating trainee with username {}", username);

        Trainee existingTrainee;

        existingTrainee = traineeRepository.getByUsername(username)
            .map(obj -> (Trainee) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));

        mapper.updateTrainee(updatedTraineeDto, existingTrainee);

        traineeRepository.update(existingTrainee);
        logger.info("Trainee updated: {}", existingTrainee);
    }

    @Override
    public void deleteTrainee(String username) {
        logger.info("Deleting trainee with username {}", username);
        
        Trainee trainee = traineeRepository.getByUsername(username)
            .map(obj -> (Trainee) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));

        traineeRepository.delete(trainee);
        logger.info("Deleted trainee: {}", trainee.toString());
    }

    @Override
    public boolean traineeLogin(String username, String password) {
        logger.info("Attempting login for trainee with username: {}", username);

        Trainee trainee;

        trainee = traineeRepository.getByUsername(username)
            .map(obj -> (Trainee) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));
        

        String storedPassword = trainee.getUser().getPassword();

        if (storedPassword.equals(password)) {
            logger.info("Login successful for username: {}", username);
            return true;
        } else {
            logger.error("Incorrect password for username {}", username);
            return false;
        }
    }

    @Override
    public TraineeDto getTraineeByUsername(String username) {
        logger.info("Fetching trainee with username: {}", username);

        Trainee trainee = (Trainee) traineeRepository.getByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));

        return mapper.toDto(trainee);
    }

    @Override
    public void updateTraineePassword(String username, String newPassword) {
        logger.info("Updating password of trainee with username {}", username);
        traineeRepository.updatePassword(username, newPassword);
        logger.info("Succesfull password change for trainee with username: {}", username);
    }

    @Override
    public void switchTraineeActiveStatus(String username) {
        logger.info("Updating active status on trainee with username {}", username);
        traineeRepository.switchActiveStatus(username);
    }

    public void updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            em.getTransaction().begin();

            // Get trainee by user name
            Trainee trainee = traineeRepository.getByUsername(traineeUsername)
                .map(obj -> (Trainee) obj)
                .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(traineeUsername)));

            // Get trainers by usernames
            List<Trainer> newTrainers = em.createQuery(
                "SELECT t FROM Trainer t JOIN FETCH t.user WHERE t.user.username IN :usernames", Trainer.class
            )
            .setParameter("usernames", trainerUsernames)
            .getResultList();

            if (newTrainers.isEmpty()) {
                throw new NoSuchElementException("Traineers with usernames %s not found".formatted(trainerUsernames));
            }

            // Update the list of trainers
            trainee.setTrainers(newTrainers);

            em.merge(trainee);
            em.getTransaction().commit();

        }
    } 

}
