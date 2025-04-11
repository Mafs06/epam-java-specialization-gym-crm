package com.epam.campus.gymcrm;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.campus.gymcrm.facade.GymFacade;
import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;

public class MainMenu {
    GymFacade facade;
    private static final Logger logger = LoggerFactory.getLogger(MainMenu.class);
    private boolean isUserAuthenticated = false;

    private String line;
    private int option;
    private String[] values;
    String usernameTyped;
    String passwordTyped;

    private final String ENTER_DATA = "Enter the following data separated by commas and spaces:\n";
    private final String DATA_SEPARATOR = ", ";
    private final String PARAMS_AMOUNT_ERROR = "The amount of parameters is not the expected";

    private final String USER_DATA = "first name, last name, true or false values for activation state of the profile";

    private final String TRAINEE_DATA = USER_DATA + ", birth date, address";
    private final String TRAINEE_EXAMPLE = "Carlos, Lopez, true, 1995-05-20, 123 Main Street";

    private final String TRAINER_DATA =  USER_DATA + ", specialization from the list";
    private final String TRAINER_EXAMPLE = "Jane, Doe, true, fitness\n";

    private final String TRAINING_DATA = "trainee username, trainer username, training name, training type from the list, training date, duration (minutes)";
    private final String TRAINING_EXAMPLE = "Beatriz.Pinzon, Terry.Crews, Aerobics for starters, fitness, 2025-08-28, 60";

    private final String ENTER_USER = "Enter the username: ";
    private final String ENTER_PASSWORD = "Enter the password: ";

    private final String OPTIONAL_CRITERIA_INFO = "The following data requested will filter the trainings of the trainee that will be returned\n" +
                                                    "Leave empty if not you do not desire to filter by that criteria. ";

    public MainMenu(GymFacade facade) {
        this.facade = facade;
    }

    public void run() {
        final String TRAINING_TYPES = facade.getTrainingTypes();

        System.out.println("\nWelcome to our Gym CRM!");

        try (Scanner sc = new Scanner(System.in)) {

            while(true) {
                System.out.println("\nWhat would you like to do? Enter the corresponding option.");
                System.out.println("1: Create Trainer profile");
                System.out.println("2: Create Trainee profile");
                System.out.println("3: Login as Trainee");
                System.out.println("4: Login as Trainer");

                if (isUserAuthenticated) {
                    System.out.println("5: Get Trainer profile info");
                    System.out.println("6: Get Trainee profile info");
                    System.out.println("7: Change Trainee password");
                    System.out.println("8: Change Trainer password");
                    System.out.println("9: Update Trainer profile");
                    System.out.println("10: Update Trainee profile");
                    System.out.println("11: Change active status on a Trainee");
                    System.out.println("12: Change active status on a Trainer");
                    System.out.println("13: Delete Trainee profile");
                    System.out.println("14: Get trainings from a Trainee");
                    System.out.println("15: Get trainings from a Trainer");
                    System.out.println("16: Add Training");
                    System.out.println("17: Get Trainers not assigned on a Trainee");
                    System.out.println("18: Update Trainers list of a Trainee");
                }
                System.out.println("0: Close Gym CRM\n");

                // Matches input to be an integer
                // If some user is authenticated options available are 0 to 9 OR 10 to 16
                // if that is not the case options available are 0 to 4
                String availableOptions = isUserAuthenticated ? "[0-9]|(1[0-8])" : "[0-4]";

                line = sc.nextLine();
                if (line.matches(availableOptions)) {
                    option = Integer.valueOf(line);
                } else {
                    logger.error("User input \"{}\" does not follow the menu option requirements", line);
                    System.err.println("Error. Option not allowed");
                    continue;
                }

                if (option == 0) {
                    System.out.println("Exiting Gym CRM... Goodbye!");
                    break;
                }

                switch (option) {
                    case 1:
                        System.out.println(ENTER_DATA + TRAINER_DATA + "\nExample: " + TRAINER_EXAMPLE);
                        System.out.println("The available specializations are\n" + TRAINING_TYPES + "\n");

                        values = sc.nextLine().split(DATA_SEPARATOR);

                        if (values.length != 4) {
                            System.err.println(PARAMS_AMOUNT_ERROR);
                            break;
                        }

                        boolean active;
                        if (values[2].equalsIgnoreCase("true") || values[2].equalsIgnoreCase("false")) {
                            active = Boolean.parseBoolean(values[2]);
                        } else {
                            System.err.println("Incorrect active status");
                            break;
                        }

                        TrainerDto trainerDto = new TrainerDto(
                            values[0],                          // firstName
                            values[1],                          // lastName
                            active,                             // active
                            values[3]);                         //trainingTypeName
            
                        System.out.println();
                        facade.createTrainer(trainerDto);
                        break;

                    case 2:
                        System.out.println(ENTER_DATA + TRAINEE_DATA + "\nExample: " + TRAINEE_EXAMPLE + "\n");

                        values = sc.nextLine().split(DATA_SEPARATOR);
            
                        if (values.length != 5) {
                            System.err.println("The amount of parameters is not the expected");
                            break;
                        }
                
                        if (values[2].equalsIgnoreCase("true") || values[2].equalsIgnoreCase("false")) {
                            active = Boolean.parseBoolean(values[2]);
                        } else {
                            System.err.println("Incorrect active option for trainee");
                            break;
                        }

                        try {
                            TraineeDto traineeDto = new TraineeDto(
                                values[0],                          // firstName
                                values[1],                          // lastName
                                active,                             // active
                                LocalDate.parse(values[3]),         // dateOfBirth
                                values[4]);                         // adress

                            System.out.println();
                            facade.createTrainee(traineeDto);
                        } catch (DateTimeParseException e) {
                            System.err.println("Invalid date");
                        }
                        break;

                    case 3: case 4:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();
                        System.out.print(ENTER_PASSWORD);
                        passwordTyped = sc.nextLine();
                        
                        if(option==4){
                            isUserAuthenticated = facade.trainerLogin(usernameTyped, passwordTyped);
                        } else {
                            isUserAuthenticated = facade.traineeLogin(usernameTyped, passwordTyped);
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
                        System.out.println("The available specializations are\n" + TRAINING_TYPES);
                        System.out.println();

                        values = sc.nextLine().split(DATA_SEPARATOR);

                        if (values.length != 4) {
                            System.err.println("The amount of parameters is not the expected");
                            break;
                        }
                
                        if (values[2].equalsIgnoreCase("true") || values[2].equalsIgnoreCase("false")) {
                            active = Boolean.parseBoolean(values[2]);
                        } else {
                            System.err.println("Incorrect active status");
                            break;
                        }
                
                        trainerDto = new TrainerDto(
                            values[0],                          // firstName
                            values[1],                          // lastName
                            active,                             // active
                            values[3]);                         //trainingTypeName

                        facade.updateTrainer(usernameTyped, trainerDto);
                        break;

                    case 10:
                        System.out.print(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        System.out.println(ENTER_DATA + TRAINEE_DATA + "\nExample: " + TRAINEE_EXAMPLE + "\n");
                        
                        values = sc.nextLine().split(DATA_SEPARATOR);

                        if (values.length != 5) {
                            System.err.println("The amount of parameters is not the expected");
                            break;
                        }
                
                        if (values[2].equalsIgnoreCase("true") || values[2].equalsIgnoreCase("false")) {
                            active = Boolean.parseBoolean(values[2]);
                        } else {
                            System.err.println("Incorrect active option for trainee");
                            break;
                        }
                
                        try {
                            TraineeDto traineeDto = new TraineeDto(
                                values[0],                      // firstName
                                values[1],                      // lastName
                                active,                         // active
                                LocalDate.parse(values[3]),     // dateOfBirth
                                values[4]);                     // address

                            facade.updateTrainee(usernameTyped, traineeDto);
                        } catch (DateTimeParseException e) {
                            System.err.println("Invalid date");
                        }
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
                        break;

                    case 14:
                        System.out.println(ENTER_USER);
                        usernameTyped = sc.nextLine();
                        System.out.println(OPTIONAL_CRITERIA_INFO);

                        try {
                            System.out.print("Filter by \'from\' date (YYY-MM-DD): ");
                            String fromDateStr = sc.nextLine();
                            LocalDate fromDate = fromDateStr.isBlank() ? null : LocalDate.parse(fromDateStr);

                            System.out.print("Filter by \'to\' date  (YYYY-MM-DD): ");
                            String toDateStr = sc.nextLine();
                            LocalDate toDate = toDateStr.isBlank() ? null : LocalDate.parse(toDateStr);

                            System.out.print("Filter by trainer, write their username: ");
                            String trainerUsername = sc.nextLine();
                            trainerUsername = trainerUsername.isBlank() ? null : trainerUsername;

                            System.out.print("Filter by training type, the availables are\n" + TRAINING_TYPES + "\n");
                            String trainingTypeName = sc.nextLine();
                            trainingTypeName = trainingTypeName.isBlank() ? null : trainingTypeName;

                            facade.getTrainingsByTraineeCriteria(
                                usernameTyped,
                                fromDate,
                                toDate,
                                trainerUsername,
                                trainingTypeName);

                        } catch (DateTimeParseException e) {
                            System.err.println("Invalid date");
                        }
                        
                        break;
                    
                    case 15:
                        System.out.println(ENTER_USER);
                        usernameTyped = sc.nextLine();
                        System.out.println(OPTIONAL_CRITERIA_INFO);

                        try {
                            System.out.print("Filter by \'from\' date (YYY-MM-DD): ");
                            String fromDateStr = sc.nextLine();
                            LocalDate fromDate = fromDateStr.isBlank() ? null : LocalDate.parse(fromDateStr);

                            System.out.print("Filter by \'to\' date  (YYYY-MM-DD): ");
                            String toDateStr = sc.nextLine();
                            LocalDate toDate = toDateStr.isBlank() ? null : LocalDate.parse(toDateStr);

                            System.out.print("Filter by trainee, write their username: ");
                            String traineeUsername = sc.nextLine();
                            traineeUsername = traineeUsername.isBlank() ? null : traineeUsername;

                            System.out.println();
                            facade.getTrainingsByTrainerCriteria(
                                usernameTyped,
                                fromDate,
                                toDate,
                                traineeUsername);

                        } catch (DateTimeParseException e) {
                            System.err.println("Invalid date");
                        }

                        break;

                    case 16:
                        System.out.println(ENTER_DATA + TRAINING_DATA + "\nExample: " + TRAINING_EXAMPLE);
                        System.out.println("The available training types are\n" + TRAINING_TYPES + "\n");

                        values = sc.nextLine().split(DATA_SEPARATOR);

                        if (values.length != 6) {
                            System.err.println("The amount of data is not the expected");
                            break;
                        }

                        try {
                            TrainingDto trainingDto = new TrainingDto(
                                values[0],
                                values[1],
                                values[2],
                                values[3],
                                LocalDate.parse(values[4]),
                                Integer.parseInt(values[5]));

                            facade.createTraining(trainingDto);
                        } catch (NumberFormatException | DateTimeParseException e) {
                            System.out.println("Wrong format for some data passed");
                        }

                        break;
                    
                    case 17:
                        System.out.println(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        facade.getTrainersNotAssignedToTrainee(usernameTyped);
                        break;

                    case 18:
                        System.out.println(ENTER_USER);
                        usernameTyped = sc.nextLine();

                        System.out.println("Enter the usernames of the new trainers separated by commas and spaces");
                        values = sc.nextLine().split(DATA_SEPARATOR);
                        List<String> newTrainers = List.of(values);

                        facade.updateTraineeTrainers(usernameTyped, newTrainers);
                        break;

                    default:
                        break;
                }
            }
        }
    }
}
