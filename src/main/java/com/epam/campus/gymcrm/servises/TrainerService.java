package com.epam.campus.gymcrm.servises;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.daos.TrainerDao;
import com.epam.campus.gymcrm.models.Trainer;

@Service
public class TrainerService {

    private final TrainerDao trainerDao;

    @Autowired
    public TrainerService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Optional<Trainer> getTrainer(int id) {
        return trainerDao.get(id);
    }

    public List<Trainer> getTrainers() {
        return trainerDao.getAll();
    }

    public void createTrainer(Trainer trainer) {
        trainerDao.save(trainer);
        System.out.println("Trainer created!");
    }

    public void updateTrainer(Trainer trainer, String[] params) {
        trainerDao.update(trainer, params);
        System.out.println("Trainer updated!");
    }

    public void deleteTrainer(Trainer trainer) {
        trainerDao.delete(trainer);
        System.out.println("Trainer deleted!");
    }
}
