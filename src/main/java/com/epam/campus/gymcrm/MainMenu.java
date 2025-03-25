package com.epam.campus.gymcrm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.campus.gymcrm.facade.GymFacade;

public class MainMenu {
    GymFacade facade;
    private static final Logger logger = LoggerFactory.getLogger(MainMenu.class);
    
    String line;
    int option;
    int id;
    String[] values;

    String enterData = "Enter the following data separated by commas and spaces:\n";
    String dataSeparator = ", ";

    String userData = "first name, last name, true or false values for activation state of the profile";

    String traineeData = userData + ", birth date, address";
    String traineeExample = "Jane, Doe, true, 1\n";

    String trainerData =  userData + ", specialization id from the list";
    String trainerExample = "Carlos, Lopez, true, 1995-05-20, 123 Main Street";

    String trainingData = "id, trainee id, trainer id, training name, trainind date, duration (minutes)";
    List<String> trainingTypes = new ArrayList<String>(List.of("fitness", "yoga", "zumba", "stretching", "resistance"));

    public MainMenu(GymFacade facade) {
        this.facade = facade;
    }

    public void run() {
        System.out.println("\nWelcome to our Gym CRM!");

        try (Scanner sc = new Scanner(System.in)) {

            while(true) {
                System.out.println("\nWhat would you like to do? Enter the corresponding option.");
                System.out.println("1: Create Trainer profile");
                System.out.println("2: Create Trainee profile");
                System.out.println("3: Login as Trainee");
                System.out.println("4: Login as Trainer");
                System.out.println("0: Close Gym CRM\n");

                line = sc.nextLine();
                if (line.matches("[0-4]")) {
                    option = Integer.valueOf(line);
                } else {
                    logger.error("User input \"{}\" does not follow the menu option requirements", line);
                    System.err.println("Error. Option not allowed.");
                    continue;
                }

                if (option == 0) {
                    System.out.println("Exiting Gym CRM... Goodbye!");
                    break;
                }

                switch (option) {
                    case 1:
                        System.out.println(enterData + trainerData + "\nExample: " + traineeExample);
                        System.out.println("The available specializations are");
                        // TODO: get TrainingTypes instead of reading hardcoded list
                        for (int i = 0; i < trainingTypes.size(); i++) {
                            System.out.println((i+1) + ": " + trainingTypes.get(i));
                        }
                        System.out.println();

                        values = sc.nextLine().split(dataSeparator);
            
                        System.out.println();
                        //facade.createTrainer(values);
                        break;

                    case 2:
                        System.out.println(enterData + traineeData + "\nExample: " + trainerExample + "\n");

                        values = sc.nextLine().split(dataSeparator);
            
                        System.out.println();
                        //facade.createTrainee(values);
                        break;

                    case 3: case 4:
                        System.out.print("Enter the user: ");
                        String usernameTyped = sc.nextLine();
                        System.out.print("Enter the password: ");
                        String passwordTyped = sc.nextLine();
                        
                        /*if(option==3){
                            facade.traineeLogin(usernameTyped, passwordTyped);
                        } else {
                            facade.trainerLogin(usernameTyped, passwordTyped);
                        }*/

                    default:
                        break;
                }
            }
        }
    }
}
