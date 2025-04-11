package com.epam.campus.gymcrm.services.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TrainerMapper;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.repositories.TrainingTypeRepository;
import com.epam.campus.gymcrm.services.ITrainerService;
import com.epam.campus.gymcrm.utils.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainerService implements ITrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private TraineeRepository traineeRepository;
    private TrainerMapper mapper;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, TraineeRepository traineeRepository, TrainerMapper mapper) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.traineeRepository = traineeRepository;
        this.mapper = mapper;
    }

    @Override
    public Trainer createTrainer(TrainerDto newTrainerDto) {
        logger.info("Adding new trainer with data: {}", newTrainerDto);

        String username = UserUtil.generateUsername(
            newTrainerDto.getFirstName(),
            newTrainerDto.getLastName(),
            trainerRepository.getUsernames()
        );
        String password = UserUtil.generatePassword();

        User user = new User();
        user.setFirstName(newTrainerDto.getFirstName());
        user.setLastName(newTrainerDto.getLastName());
        user.setUsername(username);
        user.setPassword(password);
        user.setActive(newTrainerDto.isActive());

        TrainingType trainingType = trainingTypeRepository.getByName(newTrainerDto.getSpecialization())
            .orElseThrow(() -> new NoSuchElementException("Specialization with name %s not found".formatted(newTrainerDto.getSpecialization())));
        
        Trainer trainer = mapper.toTrainer(newTrainerDto, trainingType, username, password);

        trainerRepository.save(trainer);
        logger.info("Trainer created: {}", trainer);
        return trainer;
    }

    @Override
    public void updateTrainer(String username, TrainerDto updatedTrainerDto) {
        logger.info("Updating trainer with username {}", username);

        Trainer existingTrainer;

        existingTrainer = trainerRepository.getByUsername(username)
            .map(obj -> (Trainer) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(username)));

        TrainingType trainingType = trainingTypeRepository.getByName(updatedTrainerDto.getSpecialization())
            .orElseThrow(() -> new NoSuchElementException("Specialization with name %s not found".formatted(updatedTrainerDto.getSpecialization())));

        mapper.updateTrainer(updatedTrainerDto, existingTrainer, trainingType);

        trainerRepository.update(existingTrainer);
        logger.info("Trainer updated: {}", existingTrainer);
    }

    @Override
    public boolean trainerLogin(String username, String password) {
        logger.info("Attempting login for trainer with username: {}", username);

        Trainer trainer;
        
        trainer = trainerRepository.getByUsername(username)
            .map(obj -> (Trainer) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(username)));

        String storedPassword = trainer.getUser().getPassword();

        if (storedPassword.equals(password)) {
            logger.info("Login successful for username: {}", username);
            return true;
        } else {
            logger.error("Incorrect password for username {}", username);
            return false;
        }
    }

    @Override
    public TrainerDto getTrainerByUsername(String username) {
        logger.info("Fetching trainer with username: {}", username);

        Trainer trainer = (Trainer) trainerRepository.getByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(username)));

        return mapper.toDto(trainer);
    }

    @Override
    public void updateTrainerPassword(String username, String newPassword) {
        logger.info("Updating password of trainer with username {}", username);
        trainerRepository.updatePassword(username, newPassword);
        logger.info("Succesfull password change for trainer with username: {}", username);
    }

    @Override
    public void switchTrainerActiveStatus(String username) {
        logger.info("Updating active status on trainer with username {}", username);
        trainerRepository.switchActiveStatus(username);
    }

    @Override
    public List<TrainerDto> getTrainersNotAssignedToTrainee(String traineeUsername) {
        traineeRepository.getByUsername(traineeUsername)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(traineeUsername)));

        List<Trainer> trainers = trainerRepository.findTrainersNotAssignedToTrainee(traineeUsername);
        return trainers.stream().map(mapper::toDto).toList();
    }

}
