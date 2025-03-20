package com.epam.campus.gymcrm.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TrainerMapper;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.repositories.TrainingTypeRepository;
import com.epam.campus.gymcrm.services.ITrainerService;
import com.epam.campus.gymcrm.utils.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainerService implements ITrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerRepository trainerDao;
    private TrainingTypeRepository trainingTypeDao;
    private TrainerMapper mapper;

    @Autowired
    public void setTrainingTypeDao(TrainingTypeRepository trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @Autowired
    public void setTraineeeDao(TrainerRepository trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setMappper(TrainerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TrainerDto getTrainer(int id) {
        logger.info("Fetching trainer with ID: {}", id);
        Trainer trainer = trainerDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Trainer with id %s not found".formatted(id)));

        return mapper.toDto(trainer);
    }

    @Override
    public List<TrainerDto> getTrainers() {
        logger.info("Fetching all trainers");
        List<TrainerDto> trainersDtos = new ArrayList<>();
        trainerDao.getAll().forEach(trainer -> trainersDtos.add(mapper.toDto(trainer)));
        return trainersDtos;
    }

    @Override
    public void createTrainer(TrainerDto newTrainerDto) {
        logger.info("Adding new trainer: {}", newTrainerDto.toString());

        try {
            String username = UserUtil.generateUsername(
                newTrainerDto.getFirstName(),
                newTrainerDto.getLastName(),
                trainerDao.getUsernames()
            );
            String password = UserUtil.generatePassword();

            User user = new User();
            user.setFirstName(newTrainerDto.getFirstName());
            user.setLastName(newTrainerDto.getLastName());
            user.setUsername(username);
            user.setPassword(password);
            user.setActive(newTrainerDto.isActive());

            TrainingType trainingType = trainingTypeDao.get(newTrainerDto.getSpecialization())
                .orElseThrow(() -> new IllegalArgumentException("Invalid specialization"));
            
            Trainer trainer = mapper.toTrainer(newTrainerDto, trainingType, username, password);

            trainerDao.save(trainer);
            System.out.println("Trainer created with username: " + trainer.getUser().getUsername());

        } catch (IllegalArgumentException e) {
            logger.error("Validation error when adding trainer: {}", e.getMessage());
            System.err.println("There was an error and trainer could not be added. Verify specialization id data.");
            return;

        } catch (Exception e) {
            logger.error("Unexpected error while adding trainer: {}", e.getMessage(), e);
            System.err.println("There was an error and trainer could not be added.");
            return;
        }
    }

    @Override
    public void updateTrainer(int id, TrainerDto updatedTrainerDto) {
        logger.info("Updating trainer with id {}", id);
        try {
            Trainer existingTrainer = trainerDao.get(id)
                .orElseThrow(() -> new NoSuchElementException("Trainer with id %s not found".formatted(id)));

            TrainingType trainingType = trainingTypeDao.get(updatedTrainerDto.getSpecialization())
                .orElseThrow(() -> new IllegalArgumentException("Invalid specialization"));

            mapper.updateTrainerEntity(updatedTrainerDto, existingTrainer, trainingType);

            trainerDao.update(existingTrainer);
    
            System.out.println("Trainer updated");
            logger.info("Updated trainer: {}", existingTrainer.toString());
    
        } catch (NoSuchElementException e) {
            logger.error("NoSuchElementException: {}", e.getMessage(), e);
            System.err.println("There was an error and trainer could not be updated. " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException: {}", e.getMessage(), e);
            System.err.println("There was an error and trainer could not be updated. " + e.getMessage());
        }
    }
    

    @Override
    public void deleteTrainer(int id) {
        logger.info("Deleting trainer with id {}", id);

        try {
            Trainer trainer = trainerDao.get(id)
                .orElseThrow(() -> new NoSuchElementException("Trainer with id %s not found".formatted(id)));
            trainerDao.delete(trainer);
            System.out.println("Trainer deleted");

        } catch (NoSuchElementException e) {
            logger.error("NoSuchElementException: {}", e.getMessage(), e);
            System.err.println(e.getMessage());
        }
    }

    public boolean trainerLogin(String username, String password) {
        logger.info("Attempting login for username: {}", username);

        Optional<Trainer> optionalTrainer = trainerDao.getByUsername(username).map(obj -> (Trainer) obj);

        if (optionalTrainer.isEmpty()) {
            System.out.println("Login failed: Username {} not found" + username);
            return false;
        }

        Trainer trainer = optionalTrainer.get();
        String storedPassword = trainer.getUser().getPassword();

        if (storedPassword.equals(password)) {
            System.out.println("Login successful for username: " + username);
            return true;
        } else {
            System.out.println("Login failed: Incorrect password for username " + username);
            return false;
        }
    }

}
