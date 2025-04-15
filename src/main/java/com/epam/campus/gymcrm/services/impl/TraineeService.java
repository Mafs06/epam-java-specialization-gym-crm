package com.epam.campus.gymcrm.services.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TraineeMapper;
import com.epam.campus.gymcrm.mappers.TrainerMapper;
import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TraineeCreationDto;
import com.epam.campus.gymcrm.models.dtos.TraineeResponseDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.services.ITraineeService;
import com.epam.campus.gymcrm.utils.UserUtil;

@Service
public class TraineeService implements ITraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;


    @Autowired
    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public LoginDto createTrainee(TraineeCreationDto newTraineeDto) {
        logger.info("Adding new trainee with data: {}", newTraineeDto);

        String username = UserUtil.generateUsername(
            newTraineeDto.getFirstName(),
            newTraineeDto.getLastName(),
            traineeRepository.getUsernames()
        );
        String password = UserUtil.generatePassword();

        Trainee trainee = TraineeMapper.toTrainee(newTraineeDto, username, password, true);
        trainee.getUser().setTrainee(trainee);

        traineeRepository.save(trainee);
        logger.info("Trainee created: {}", trainee);

        return new LoginDto(username, password);
    }

    @Override
    public TraineeUpdateResponseDto updateTrainee(String username, TraineeUpdateRequestDto traineeUpdateDto) {
        logger.info("Updating trainee with username {}", username);

        Trainee trainee;

        trainee = traineeRepository.getByUsername(username)
            .map(obj -> (Trainee) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));

        // Update reference of Trainee already obtained
        TraineeMapper.updateTrainee(traineeUpdateDto, trainee);

        traineeRepository.update(trainee);
        logger.info("Trainee updated: {}", trainee);

        // Get response DTO for trainee but username property is missing
        TraineeResponseDto incompleteResponseDto = TraineeMapper.toResponseDto(trainee);
        // Add username property to response DTO
        return TraineeMapper.toUpdateResponseDto(username, incompleteResponseDto);
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
    public boolean traineeLogin(LoginDto loginDto) {
        String username = loginDto.getUsername();
        logger.info("Attempting login for trainee with username: {}", username);

        Trainee trainee;

        trainee = traineeRepository.getByUsername(username)
            .map(obj -> (Trainee) obj)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));
        
        String storedPassword = trainee.getUser().getPassword();

        if (storedPassword.equals(loginDto.getPassword())) {
            logger.info("Login successful for username: {}", username);
            return true;
        } else {
            logger.error("Incorrect password for username {}", username);
            return false;
        }
    }

    @Override
    public TraineeResponseDto getTraineeByUsername(String username) {
        logger.info("Fetching trainee with username: {}", username);
        Trainee trainee = (Trainee) traineeRepository.getByUsername(username)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(username)));
        return TraineeMapper.toResponseDto(trainee);
    }

    @Override
    public boolean updateTraineePassword(String username, LoginChangeDto loginChangeDto) {
        logger.info("Updating password of trainee with username {}", username);

        // Check for login match before updating (throws Not found)
        if (traineeLogin(new LoginDto(username, loginChangeDto.getOldPassword()))) {
            traineeRepository.updatePassword(username, loginChangeDto.getNewPassword());
            logger.info("Succesfull password change for trainee with username: {}", username);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean switchTraineeActiveStatus(String username) {
        logger.info("Switching active status on trainee with username {}", username);
        if (traineeRepository.getByUsername(username).isPresent()) {
            boolean setTo = traineeRepository.switchActiveStatus(username);
            logger.info("Active status is now set to " + setTo);
            return setTo;
        } else {
            throw new NoSuchElementException("Trainee with username %s not found".formatted(username));
        }
    }

    @Override
    public void updateActiveStatus(String username, boolean active) {
        logger.info("Updating active status on trainee with username {}", username);
        if (traineeRepository.getByUsername(username).isPresent()) {
            traineeRepository.updateActiveStatus(username, active);
            logger.info("Active status updated to " + active);
        } else {
            throw new NoSuchElementException("Trainee with username %s not found".formatted(username));
        }
    }

    @Override
    public List<TrainerFromListDto> updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames) {
        logger.info("Updating trainers of trainee with username {}", traineeUsername);

        Trainee trainee = (Trainee) traineeRepository.getByUsername(traineeUsername)
            .orElseThrow(() -> new NoSuchElementException("Trainee with username %s not found".formatted(traineeUsername)));

        List<Trainer> trainers = traineeRepository.updateTrainers(trainee, trainerUsernames);
        logger.info("Trainers updated.");

        return trainers.stream().map(TrainerMapper::toListDto).toList();
    } 

    @Override
    public List<TrainerFromListDto> getActiveTrainersNotAssignedToTrainee(String traineeUsername) {
        traineeRepository.getByUsername(traineeUsername)
            .orElseThrow(() -> new NoSuchElementException("Trainer with username %s not found".formatted(traineeUsername)));

        List<Trainer> trainers = trainerRepository.findActiveTrainersNotAssignedToTrainee(traineeUsername);
        return trainers.stream().map(TrainerMapper::toListDto).toList();
    }

}