package com.epam.campus.gymcrm.servises;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.daos.TrainingDao;
import com.epam.campus.gymcrm.models.Training;



@Service
public class TrainingService {

    private final TrainingDao trainingDao;

    @Autowired
    public TrainingService(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Optional<Training> getTraining(int id) {
        return trainingDao.get(id);
    }

    public List<Training> getTrainings() {
        return trainingDao.getAll();
    }

    public void createTraining(Training training) {
        trainingDao.save(training);
        System.out.println("Training created: " + training.toString());
    }

    public void updateTraining(Training training, String[] params) {
        trainingDao.update(training, params);
        System.out.println("Training updated: " + training.toString());
    }

    public void deleteTraining(Training training) {
        trainingDao.delete(training);
        System.out.println("Training deleted: " + training.toString());
    }
}
