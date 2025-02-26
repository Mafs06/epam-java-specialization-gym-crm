package com.epam.campus.gymcrm.servises;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.daos.TraineeDao;
import com.epam.campus.gymcrm.models.Trainee;

@Service
public class TraineeService {

    private final TraineeDao traineeDao;

    @Autowired
    public TraineeService(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Optional<Trainee> getTrainee(int id) {
        return traineeDao.get(id);
    }

    public List<Trainee> getTrainees() {
        return traineeDao.getAll();
    }

    public void createTrainee(Trainee trainee) {
        traineeDao.save(trainee);
        System.out.println("Trainee created!");
    }

    public void updateTrainee(Trainee trainee, String[] params) {
        traineeDao.update(trainee, params);
        System.out.println("Trainee updated!");
    }

    public void deleteTrainee(Trainee trainee) {
        traineeDao.delete(trainee);
        System.out.println("Trainee deleted!");
    }
}
