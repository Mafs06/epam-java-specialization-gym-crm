package com.epam.campus.gymcrm;

import java.time.LocalDate;

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

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to our Gym CRM!");

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        // Get beans from the Spring context
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        // Test services
        Trainee newTrainee = new Trainee(3, "John", "Doe", "johndoe", "password123", true, LocalDate.of(2000, 5, 15), "123 Main Street");
        traineeService.createTrainee(newTrainee);
        Trainer newTrainer = new Trainer(3, "Bruce", "Wayne", "batman", "darkknight123", true, "Martial Arts");
        trainerService.createTrainer(newTrainer);
        Training newTraining = new Training(3, 3, 3, "Strength Training Advanced", new TrainingType("Strength Training"), LocalDate.of(2025, 3, 1), 60);
        trainingService.createTraining(newTraining);

        System.out.println(traineeService.getTrainee(3).toString());
        System.out.println(trainerService.getTrainer(3).toString());
        System.out.println(trainingService.getTraining(3).toString());

        ((AnnotationConfigApplicationContext) context).close();
    }
}
