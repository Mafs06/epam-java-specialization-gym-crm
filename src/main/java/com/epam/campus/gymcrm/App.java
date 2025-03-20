package com.epam.campus.gymcrm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.campus.gymcrm.config.Config;
import com.epam.campus.gymcrm.facade.GymFacade;
import com.epam.campus.gymcrm.models.entities.TrainingType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    public static void main(String[] args) {
        int id;
        String[] values;
        String enterData = "Enter the following data separated by commas and spaces: ";
        String dataSeparator = ", ";
        String userData = "id, first name, last name, true or false values for activation state of the profile";
        String traineeData = userData + ", birth date, address";
        String trainerData =  userData + ", specialization";
        String trainingData = "id, trainee id, trainer id, training name, trainind date, duration (minutes)";
        List<String> trainingTypes = new ArrayList<String>(List.of("fitness", "yoga", "zumba", "stretching", "resistance"));

        //ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        //GymFacade gymFacade = context.getBean(GymFacade.class);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym_persistence_unit");
        EntityManager em = emf.createEntityManager();

        TrainingType trainingType;
        try {
            for (int i=0; i<trainingTypes.size(); i++) {
                em.getTransaction().begin();
                trainingType = new TrainingType();
                trainingType.setName(trainingTypes.get(i));
                em.persist(trainingType);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }

        System.out.println();
        System.out.println("Welcome to our Gym CRM!");

        /*try (Scanner sc = new Scanner(System.in)) {

            while(true) {
                System.out.println("\nWhat would you like to do? Enter the corresponding option.");
                System.out.println();
                System.out.println("1: See all trainee profiles");
                System.out.println("2: See all trainer profiles");
                System.out.println("3: See all trainings");
                System.out.println();
                System.out.println("4: Search for a trainee profile");
                System.out.println("5: Search for a trainer profile");
                System.out.println("6: Search for a training");
                System.out.println();
                System.out.println("7: Create a trainee profile");
                System.out.println("8: Create a trainer profile");
                System.out.println("9: Create a training");
                System.out.println();
                System.out.println("10: Update an existing trainee profile");
                System.out.println("11: Update an existing trainer profile");
                System.out.println("12: Update an existing training");
                System.out.println();
                System.out.println("13: Delete a trainee profile");
                System.out.println("14: Delete a trainer profile");
                System.out.println("15: Delete a training");
                System.out.println();
                System.out.println("0: Close Gym CRM");
                System.out.println();
            
                int option = sc.nextInt();
                sc.nextLine();

                if (option == 0) {
                    System.out.println("Exiting Gym CRM... Goodbye!");
                    break;
                }

                switch (option) {
                    case 1:
                        System.out.println(gymFacade.getAllTrainees());
                        break;
                    case 2:
                        System.out.println(gymFacade.getAllTrainers());
                        break;
                    case 3:
                        System.out.println(gymFacade.getAllTrainings());
                        break;

                    case 4: 
                        System.out.print("Enter the ID of the trainee to search: ");
                        id = sc.nextInt();
                        System.out.println(gymFacade.getTrainee(id));
                        break;
                    case 5:
                        System.out.print("Enter the ID of the trainer to search: ");
                        id = sc.nextInt();
                        System.out.println(gymFacade.getTrainer(id));
                        break;
                    case 6:
                        System.out.print("Enter the ID of the training to search: ");
                        id = sc.nextInt();
                        System.out.println(gymFacade.getTraining(id));
                        break;

                    case 7:
                        System.out.println(enterData + traineeData + "\nExample:");
                        System.out.println("100, Carlos, Lopez, true, 1995-05-20, 123 Main Street");
                        values = sc.nextLine().split(dataSeparator);

                        gymFacade.createTrainee(values);
                        break;
                    
                    case 8:
                        System.out.println(enterData + trainerData + "\nExample:");
                        System.out.println("110, Jane, Doe, true, Yoga");
                        values = sc.nextLine().split(dataSeparator);

                        gymFacade.createTrainer(values);
                        break;

                    case 9:
                        System.out.println(enterData + trainingData + "\nExample:");
                        System.out.println("120, 1, 3, Strength Training, 2025-03-15, 60");

                        values = sc.nextLine().split(dataSeparator);
                        gymFacade.createTraining(values);
                        break;

                    case 10: 
                        System.out.print("Enter the ID of the trainee to update: ");
                        id = sc.nextInt();
                        sc.nextLine();

                        System.out.println(enterData + traineeData + "\nExample:");
                        System.out.println("1, Tyron, Brown, true, 1990-11-30, Blue Walk Street");
                        values = sc.nextLine().split(dataSeparator);

                        gymFacade.updateTrainee(id, values);
                        break;

                    case 11:
                        System.out.print("Enter the ID of the trainer to update: ");
                        id = sc.nextInt();
                        sc.nextLine();

                        System.out.println(enterData + trainerData + "\nExample:");
                        System.out.println("1, Lily, White, true, Gymnastics");
                        values = sc.nextLine().split(dataSeparator);

                        gymFacade.updateTrainer(id, values);
                        break;

                    case 12:
                        System.out.print("Enter the ID of the training to update: ");
                        id = sc.nextInt();
                        sc.nextLine();

                        System.out.println(enterData + trainingData + "\nExample:");
                        System.out.println("1, 2, 2, Strength Training Advanced, 2025-04-01, 60");
                        values = sc.nextLine().split(dataSeparator);

                        gymFacade.updateTraining(id, values);
                        break;

                    case 13:
                        System.out.print("Enter the ID of the trainee to delete: ");
                        id = sc.nextInt();
                        gymFacade.deleteTrainee(id);
                        break;
                    case 14:
                        System.out.print("Enter the ID of the trainer to delete: ");
                        id = sc.nextInt();
                        gymFacade.deleteTrainer(id);
                        break;
                    case 15:
                        System.out.print("Enter the ID of the training to delete: ");
                        id = sc.nextInt();
                        gymFacade.deleteTraining(id);
                        break;

                    default:
                        break;
                }
            }
        }

        ((AnnotationConfigApplicationContext) context).close(); */
    }
}