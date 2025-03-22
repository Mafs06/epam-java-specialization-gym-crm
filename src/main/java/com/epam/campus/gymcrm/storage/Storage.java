package com.epam.campus.gymcrm.storage;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.services.impl.TraineeService;
import com.epam.campus.gymcrm.services.impl.TrainerService;
import com.epam.campus.gymcrm.services.impl.TrainingService;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Storage {

    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private final String modelsPackagePath = "com.epam.campus.gymcrm.models.dtos";

    @Value("${storage.filepath}")
    private String filePath;

    private final TrainingTypeService trainingTypeService;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    

        @Autowired
    public Storage(TrainingTypeService trainingTypeService, TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainingTypeService = trainingTypeService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    @PostConstruct
    public void initializeStorage() {
        logger.info("Initializing storage from file: {}", filePath);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        File jsonFile = new File(filePath);
        if (!jsonFile.exists()) {
            logger.error("File not found: {}", filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            JsonNode rootNode = objectMapper.readTree(reader);

            rootNode.fieldNames().forEachRemaining(entityName -> {
                JsonNode entityEntries = rootNode.get(entityName);
                if (entityEntries.isArray()) {
                    for (JsonNode entry : entityEntries) {
                        try {
                            Class<?> c = Class.forName(modelsPackagePath + "." + entityName + "Dto");
                            Object o = objectMapper.treeToValue(entry, c);
                            saveEntity(o);
                        } catch (ClassNotFoundException e) {
                            logger.error("Entity model not found: {}", entityName, e);
                            break;
                        } catch (JsonProcessingException e) {
                            logger.error("Error processing JSON for entity {}: {}", entityName, e.getMessage(), e);
                        } catch (IllegalArgumentException e) {
                            logger.error("Invalid entity to map to: {}", entityName, e);
                        }
                    }
                }
            });
        } catch (IOException e) {
            logger.error("Error loading data: {}", e.getMessage(), e);
        }
    }

    private void saveEntity(Object entity) {
        try {
            if (entity instanceof TrainerDto trainerDto) {
                trainerService.createTrainer(trainerDto);
            } else if (entity instanceof TraineeDto traineeDto) {
                //traineeService.createTrainee(traineeDto);
            } else if (entity instanceof TrainingTypeDto trainingTypeDto) {
                trainingTypeService.createTrainingType(trainingTypeDto);
            } else if (entity instanceof TrainingDto trainingDto) {
                //trainingService.createTraining(trainingDto);
            } else {
                System.out.println("Some init data could not be saved");
                logger.warn("Unknown entity type: {}", entity.getClass().getSimpleName());
            }
        } catch (Exception e) {
            logger.error("Error saving entity {}: {}", entity.getClass().getSimpleName(), e.getMessage(), e);
            System.out.println("Error saving init data");
        }
    }

    private void saveTrainingType(JsonNode entry) {
        TrainingTypeDto trainingTypeDto = new TrainingTypeDto(
            entry.get("name").asText()
        );

        trainingTypeService.createTrainingType(trainingTypeDto);
    }

    private void saveTrainee(JsonNode entry) {

    }

    private void saveTrainer(JsonNode entry) {
            String firstName = entry.get("firstName").asText();
            String lastName = entry.get("lastName").asText();
            boolean active = entry.get("active").asBoolean();
            int specializationId = entry.get("specialization").asInt();

            TrainingTypeDto specialization = trainingTypeService.getTrainingType(specializationId);

            TrainerDto trainerdDto = new TrainerDto(firstName, lastName, active, specializationId);

            trainerService.createTrainer(trainerdDto);
            logger.info("Trainer saved: {} {}", firstName, lastName);
    }
}
