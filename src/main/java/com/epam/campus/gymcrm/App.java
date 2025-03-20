package com.epam.campus.gymcrm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.postgresql.shaded.com.ongres.saslprep.SASLprep;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.campus.gymcrm.config.Config;
import com.epam.campus.gymcrm.facade.GymFacade;

public class App {
    public static void main(String[] args) {
        int id;
        String[] values;
        String enterData = "Enter the following data separated by commas and spaces: ";
        String dataSeparator = ", ";
        String userData = "first name, last name, true or false values for activation state of the profile";
        String traineeData = userData + ", birth date, address";
        String trainerData =  userData + ", specialization id from the list";
        String trainingData = "id, trainee id, trainer id, training name, trainind date, duration (minutes)";
        List<String> trainingTypes = new ArrayList<String>(List.of("fitness", "yoga", "zumba", "stretching", "resistance"));

        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);
        /*
        //! Move to storage init
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym_persistence_unit");

        EntityManager em = emf.createEntityManager();
        // Data initialization for training types
        TrainingType trainingType;
        for (int i=0; i<trainingTypes.size(); i++) {
            em.getTransaction().begin();
            trainingType = new TrainingType();
            trainingType.setName(trainingTypes.get(i));
            em.persist(trainingType);
            em.getTransaction().commit();
        }
        em.close();
        */

        System.out.println("\nWelcome to our Gym CRM!\n");

        try (Scanner sc = new Scanner(System.in)) {

            while(true) {
                System.out.println("\nWhat would you like to do? Enter the corresponding option.\n");
                System.out.println("1: Create Trainer profile");
                System.out.println("2: Create Trainee profile");
                System.out.println("3: Login as Trainee");
                System.out.println("4: Login as Trainer");
                System.out.println("0: Close Gym CRM\n");

                int option = sc.nextInt();
                sc.nextLine();

                if (option == 0) {
                    System.out.println("Exiting Gym CRM... Goodbye!");
                    break;
                }

                switch (option) {
                    case 1:
                        System.out.println(enterData + trainerData + "\nExample:");
                        System.out.println("Jane, Doe, true, 1");
                        System.out.println("The available specializations are");
                        for (int i = 0; i < trainingTypes.size(); i++) {
                            System.out.println((i+1) + ": " + trainingTypes.get(i));
                        }

                        values = sc.nextLine().split(dataSeparator);
            
                        System.out.println();
                        gymFacade.createTrainer(values);
                        break;

                    case 2:
                        System.out.println(enterData + traineeData + "\nExample:");
                        System.out.println("Carlos, Lopez, true, 1995-05-20, 123 Main Street");

                        values = sc.nextLine().split(dataSeparator);
            
                        System.out.println();
                        gymFacade.createTrainee(values);
                        break;

                    case 3: case 4:
                        System.out.print("Ingrese su usuario: ");
                        String usernameTyped = sc.nextLine();
                        System.out.print("Ingrese su contraseña: ");
                        String passwordTyped = sc.nextLine();
                        
                        if(option==3){
                            gymFacade.traineeLogin(usernameTyped, passwordTyped);
                        } else {
                            gymFacade.trainerLogin(usernameTyped, passwordTyped);
                        }

                    default:
                        break;
                }
            }
        }

        ((AnnotationConfigApplicationContext) context).close();
    }
}