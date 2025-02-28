package com.epam.campus.gymcrm.servises;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.daos.TrainerDao;
import com.epam.campus.gymcrm.models.Trainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private TrainerDao trainerDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Optional<Trainer> getTrainer(int id) {
        logger.info("Fetching trainer with ID: {}", id);
        return trainerDao.get(id);
    }

    public List<Trainer> getTrainers() {
        logger.info("Fetching all trainers");
        return trainerDao.getAll();
    }

    public void createTrainer(Trainer trainer) {
        trainerDao.save(trainer);
        logger.info("Trainer created: {}", trainer);
    }

    public void updateTrainer(Trainer trainer, String[] params) {
        trainerDao.update(trainer, params);
        logger.info("Trainer updated: {}", trainer);
    }

    public void deleteTrainer(Trainer trainer) {
        trainerDao.delete(trainer);
        logger.info("Trainer deleted: {}", trainer);
    }
}
