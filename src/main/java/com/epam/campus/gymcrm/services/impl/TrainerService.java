package com.epam.campus.gymcrm.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TrainerMapper;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.services.ITrainerService;
import com.epam.campus.gymcrm.utils.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainerService implements ITrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerRepository trainerDao;
    private TrainerMapper mapper;

    @Autowired
    public void setTrainerDao(TrainerRepository trainerDao) {
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
        String username = UserUtil.generateUsername(
            newTrainerDto.getFirstName(),
            newTrainerDto.getLastName(),
            trainerDao.getUsernames()
        );
        String password = UserUtil.generatePassword();

        Trainer trainer = mapper.toTrainer(newTrainerDto);
        trainer.setUsername(username);
        trainer.setPassword(password);

        trainerDao.save(trainer);
        logger.info("Trainer created");
    }

    @Override
    public void updateTrainer(int id, TrainerDto updatedTrainerDto) {
        if (id != updatedTrainerDto.getId()) {
            throw new IllegalArgumentException("Id to update and id passed do not match");
        }

        Trainer existingTrainer = trainerDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Trainer with id %s not found".formatted(id)));

        Trainer updatedTrainer = mapper.toTrainer(updatedTrainerDto, existingTrainer);

        trainerDao.update(updatedTrainer);
        logger.info("Trainer updated");
    }

    @Override
    public void deleteTrainer(int id) {
        Trainer trainer = trainerDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Trainer with id %s not found".formatted(id)));
        trainerDao.delete(trainer);
        logger.info("Trainer deleted");
    }
}
