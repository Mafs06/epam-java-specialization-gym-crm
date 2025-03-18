package com.epam.campus.gymcrm.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Storage {

    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private final String modelsPackagePath = "com.epam.campus.gymcrm.models.entities";
    private final Map<String, List<Object>> storage = new HashMap<>();

    @Value("${storage.filepath}")
    private String filePath;

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
                    List<Object> entriesList = new ArrayList<>();
                    for (JsonNode entry : entityEntries) {
                        try {
                            Class<?> c = Class.forName(modelsPackagePath + "." + entityName);
                            Object o = objectMapper.treeToValue(entry, c);
                            entriesList.add(o);
                        } catch (ClassNotFoundException e) {
                            logger.error("Entity model not found: {}", entityName, e);
                            break;
                        } catch (JsonProcessingException e) {
                            logger.error("Error processing JSON for entity {}: {}", entityName, e.getMessage(), e);
                        } catch (IllegalArgumentException e) {
                            logger.error("Invalid entity to map to: {}", entityName, e);
                        }
                    }
                    if (!entriesList.isEmpty()) {
                        storage.put(entityName, entriesList);
                        logger.info("Loaded {} entries for entity: {}", entriesList.size(), entityName);
                    }
                }
            });
        } catch (IOException e) {
            logger.error("Error loading data: {}", e.getMessage(), e);
        }
    }

    public Map<String, List<Object>> getStorage() {
        logger.debug("Fetching storage data");
        return storage;
    }

    public void addData(String key, Object value) {
        storage.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        logger.info("Added data to storage: key={}", key);
    }
}
