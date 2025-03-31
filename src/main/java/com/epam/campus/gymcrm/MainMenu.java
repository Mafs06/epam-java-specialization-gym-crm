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
    
    private String line;
    private int option;
    private String[] values;
    String usernameTyped;
    String passwordTyped;

    private final String ENTER_DATA = "Enter the following data separated by commas and spaces:\n";
    private final String DATA_SEPARATOR = ", ";

    private final String USER_DATA = "first name, last name, true or false values for activation state of the profile";

    private final String TRAINEE_DATA = USER_DATA + ", birth date, address";
    private final String TRAINEE_EXAMPLE = "Carlos, Lopez, true, 1995-05-20, 123 Main Street";

    private final String TRAINER_DATA =  USER_DATA + ", specialization id from the list";
    private final String TRAINER_EXAMPLE = "Jane, Doe, true, 1\n";

    private final String TRAINING_DATA = "id, trainee id, trainer id, training name, trainind date, duration (minutes)";

    private final String ENTER_USER = "Enter the username: ";
    private final String ENTER_PASSWORD = "Enter the password: ";
    private final List<String> TRAINING_TYPES = new ArrayList<String>(List.of("fitness", "yoga", "zumba", "stretching", "resistance"));

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
                System.out.println("5: Get Trainer profile info");
                System.out.println("6: Get Trainee profile info");
                System.out.println("7: Change Trainee password");
                System.out.println("8: Change Trainer password");
                System.out.println("9: Update Trainer profile");
                System.out.println("10: Update Trainee profile");
                System.out.println("12: Change active status on a Trainer");
                System.out.println("13: Delete Trainee profile");
                System.out.println("0: Close Gym CRM\n");

                line = sc.nextLine();
                if (line.matches("[0-9]|10|11|12|13")) {
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
                        System.out.println(ENTER_DATA + TRAINER_DATA + "\nExample: " + TRAINER_EXAMPLE);
                        System.out.println("The available specializations are");
                        // TODO: get TrainingTypes instead of reading hardcoded list
                        for (int i = 0; i < TRAINING_TYPES.size(); i++) {
                            System.out.println((i+1) + ": " + TRAINING_TYPES.get(i));
                        }
                        System.out.println();

                        values = sc.nextLine().split(DATA_SEPARATOR);
            
                        System.out.println();
                        facade.createTrainer(values);
                        break;

                    case 2:
                        System.out.println(ENTER_DATA + TRAINEE_DATA + "\nExample: " + TRAINEE_EXAMPLE + "\n");

                        values = sc.nextLine().split(DATA_SEPARATOR);
            
                        System.out.println();
                        facade.createTrainee(values);
                        break;

                    case 3: case 4:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();
                        System.out.print(ENTER_PASSWORD);
                        passwordTyped = sc.nextLine();
                        
                        if(option==4){
                            facade.trainerLogin(usernameTyped, passwordTyped);
                        } else {
                            facade.traineeLogin(usernameTyped, passwordTyped);
                        }
                        break;

                    case 5:
                    case 6:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        if(option==5){
                            facade.getTrainerByUsername(usernameTyped);
                        } else {
                            facade.getTraineeByUsername(usernameTyped);
                        }
                        break;

                    case 7:
                    case 8:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();
                        System.out.print("Enter the new password: ");
                        passwordTyped = sc.nextLine();

                        if(option==7){
                            facade.updateTraineePassword(usernameTyped, passwordTyped);
                        } else {
                            facade.updateTrainerPassword(usernameTyped, passwordTyped);
                        }
                        break;

                    case 9:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        System.out.println(ENTER_DATA + TRAINER_DATA + "\nExample: " + TRAINER_EXAMPLE);
                        System.out.println("The available specializations are");
                        // TODO: get TrainingTypes instead of reading hardcoded list
                        for (int i = 0; i < TRAINING_TYPES.size(); i++) {
                            System.out.println((i+1) + ": " + TRAINING_TYPES.get(i));
                        }
                        System.out.println();
                        values = sc.nextLine().split(DATA_SEPARATOR);

                        facade.updateTrainer(usernameTyped, values);
                        break;

                    case 10:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        System.out.println(ENTER_DATA + TRAINEE_DATA + "\nExample: " + TRAINEE_EXAMPLE + "\n");
                        
                        values = sc.nextLine().split(DATA_SEPARATOR);

                        facade.updateTrainee(usernameTyped, values);
                        break;

                    case 11:
                    case 12:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        if(option==11){
                            facade.switchTraineeActiveStatus(usernameTyped);
                        } else {
                            facade.switchTrainerActiveStatus(usernameTyped);
                        }
                        break;
                    
                    case 13:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        facade.deleteTrainee(usernameTyped);

                    default:
                        break;
                }
            }
        }
    }
}
