package com.epam.campus.gymcrm.services.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TrainerMapper;
import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TrainerCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateResponseDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.repositories.TrainingTypeRepository;
import com.epam.campus.gymcrm.services.ITrainerService;
import com.epam.campus.gymcrm.session.SessionManager;
import com.epam.campus.gymcrm.utils.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainerService implements ITrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, TraineeRepository traineeRepository) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public LoginDto createTrainer(TrainerCreationDto newTrainerDto) {
        logger.info("Adding new trainer with data: {}", newTrainerDto);

        String username = UserUtil.generateUsername(
            newTrainerDto.getFirstName(),
            newTrainerDto.getLastName(),
            trainerRepository.getUsernames()
        );
        String password = UserUtil.generatePassword();

        TrainingType trainingType = trainingTypeRepository.getByName(newTrainerDto.getSpecialization())
            .orElseThrow(() -> new NoSuchElementException("Specialization with name %s not found".formatted(newTrainerDto.getSpecialization())));
        
        Trainer trainer = TrainerMapper.toTrainer(newTrainerDto, trainingType, username, password, true);
        trainer.getUser().setTrainer(trainer);

        trainerRepository.save(trainer);
        logger.info("Trainer created: {}", trainer);

        return new LoginDto(username, password);
    }

    @Override
    public TrainerUpdateResponseDto updateTrainer(String username, TrainerUpdateRequestDto trainerUpdateDto) {
        logger.info("Updating trainer with username {}", username);

        Trainer trainer;

        trainer = trainerRepository.getByUsername(username)
            .map(obj -> (Trainer) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(username)));

        TrainingType trainingType = trainingTypeRepository.getByName(trainerUpdateDto.getSpecialization())
            .orElseThrow(() -> new NoSuchElementException("Specialization with name %s not found".formatted(trainerUpdateDto.getSpecialization())));

        // Update reference of Trainer already obtained
        TrainerMapper.updateTrainer(trainerUpdateDto, trainer, trainingType);

        trainerRepository.update(trainer);
        logger.info("Trainer updated: {}", trainer);

        // Get response DTO for trainer but username property is missing
        TrainerResponseDto incompleteResponseDto = TrainerMapper.toResponseDto(trainer);
        // Add username property to response DTO
        return TrainerMapper.toUpdateResponseDto(username, incompleteResponseDto);
    }

    @Override
    public boolean trainerLogin(LoginDto loginDto) {
        String username = loginDto.getUsername();
        logger.info("Attempting login for trainer with username: {}", username);

        Trainer trainer;
        
        trainer = trainerRepository.getByUsername(username)
            .map(obj -> (Trainer) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(username)));

        String storedPassword = trainer.getUser().getPassword();

        if (storedPassword.equals(loginDto.getPassword())) {
            logger.info("Login successful for username: {}", username);
             // Set authenticated to true
            trainer.getUser().setAuthenticated(true);
            SessionManager.login(trainer.getUser());

            return true;
        } else {
            logger.error("Incorrect password for username {}", username);
            return false;
        }
    }

    @Override
    public TrainerResponseDto getTrainerByUsername(String username) {
        logger.info("Fetching trainer with username: {}", username);

        Trainer trainer = (Trainer) trainerRepository.getByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(username)));

        return TrainerMapper.toResponseDto(trainer);
    }

    @Override
    public boolean updateTrainerPassword(String username, LoginChangeDto loginChangeDto) {
        logger.info("Updating password of trainer with username {}", username);

        // Check for login match before updating (throws Not found)
        if (trainerLogin(new LoginDto(username, loginChangeDto.getOldPassword()))) {
            trainerRepository.updatePassword(username, loginChangeDto.getNewPassword());
            logger.info("Succesfull password change for trainer with username: {}", username);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean switchTrainerActiveStatus(String username) {
        logger.info("Switching active status on trainer with username {}", username);
        if (trainerRepository.getByUsername(username).isPresent()) {
            boolean setTo = trainerRepository.switchActiveStatus(username);
            logger.info("Active status is now set to " + setTo);
            return setTo;
        } else {
            throw new NoSuchElementException("Trainer with username %s not found".formatted(username));
        }
    }

    @Override
    public void updateActiveStatus(String username, boolean active) {
        logger.info("Updating active status on trainee with username {}", username);
        if (trainerRepository.getByUsername(username).isPresent()) {
            trainerRepository.updateActiveStatus(username, active);
            logger.info("Active status update to: " + active);
        } else {
            throw new NoSuchElementException("Trainer with username %s not found.".formatted(username));
        }
    }

}
