package com.epam.campus.gymcrm;

import java.time.LocalDate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.campus.gymcrm.config.Config;
import com.epam.campus.gymcrm.models.Trainee;
import com.epam.campus.gymcrm.models.Trainer;
import com.epam.campus.gymcrm.models.Training;
import com.epam.campus.gymcrm.models.TrainingType;

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to our Gym CRM!");

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);

        Trainee newTrainee = new Trainee(100, "Carlos", "Lopez", true, LocalDate.of(1995, 5, 20), "123 Main Street");
        gymFacade.createTrainee(newTrainee);

        Trainer newTrainer = new Trainer(110, "Jane", "Doe", true, "Yoga");
        gymFacade.createTrainer(newTrainer);

        Training newTraining = new Training(120, 100, 110, "Strength Training", new TrainingType("Strength Training"), LocalDate.of(2025, 3, 15), 60);
        gymFacade.createTraining(newTraining);

        System.out.println(gymFacade.getAllTrainees());
        System.out.println(gymFacade.getAllTrainers());
        System.out.println(gymFacade.getAllTrainings());

        System.out.println(gymFacade.getTrainee(3));
        System.out.println(gymFacade.getTrainee(4));

        System.out.println(gymFacade.getTrainer(3));

        ((AnnotationConfigApplicationContext) context).close();
    }
}
