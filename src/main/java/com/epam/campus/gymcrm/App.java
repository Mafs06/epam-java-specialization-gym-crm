package com.epam.campus.gymcrm;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.campus.gymcrm.config.Config;
import com.epam.campus.gymcrm.models.Trainee;
import com.epam.campus.gymcrm.models.Trainer;
import com.epam.campus.gymcrm.models.Training;
import com.epam.campus.gymcrm.models.TrainingType;
import com.epam.campus.gymcrm.servises.TraineeService;
import com.epam.campus.gymcrm.servises.TrainerService;
import com.epam.campus.gymcrm.servises.TrainingService;
import com.epam.campus.gymcrm.storage.Storage;

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to our Gym CRM!");

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Storage storage = context.getBean(Storage.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        Map<String, List<Object>> dataMap = storage.getStorage();

        System.out.println("\nData loaded from file:");
        dataMap.forEach((entity, records) -> {
            System.out.println("Entity: " + entity);
            records.forEach(System.out::println);
        });
        System.out.println();

        Trainee newTrainee = new Trainee(100, "Carlos", "Lopez", "carlos.lopez", "securePass123", true, LocalDate.of(1995, 5, 20), "123 Main Street");
        traineeService.createTrainee(newTrainee);
        Trainer newTrainer = new Trainer(110, "Jane", "Doe", "janedoe", "password123", true, "Yoga");
        trainerService.createTrainer(newTrainer);
        Training newTraining = new Training(120, 100, 110, "Strength Training", new TrainingType("Strength Training"), LocalDate.of(2025, 3, 15), 60);
        trainingService.createTraining(newTraining);

        
        

        ((AnnotationConfigApplicationContext) context).close();
    }
}
