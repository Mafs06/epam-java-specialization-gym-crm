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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TraineeService implements ITraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private TraineeRepository traineeDao;
    private TraineeMapper mapper;


    @Autowired
    public void setTraineeDao(TraineeRepository traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setMappper(TraineeMapper mapper) {
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
    public void updateTrainee(int id, TraineeDto updatedTraineeDto) {
        if (id != updatedTraineeDto.getId()) {
            throw new IllegalArgumentException("Id to update and id passed do not match");
        }

        Trainee existingTrainee = traineeDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Trainee with id %s not found".formatted(id)));

        Trainee updatedTrainee = mapper.toTrainee(updatedTraineeDto, existingTrainee);

        traineeDao.update(updatedTrainee);
        logger.info("Trainee updated");
    }

    @Override
    public void deleteTrainee(int id) {
        Trainee trainee = traineeDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Trainee with id %s not found".formatted(id)));
        traineeDao.delete(trainee);
        logger.info("Trainee deleted");
    }

}
