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

@Component
public class Storage {

    private final String modelsPackagePath = "com.epam.campus.gymcrm.models";
    private final Map<String, List<Object>> storage = new HashMap<>();

    @Value("${storage.filepath}")
    private String filePath;

    @PostConstruct
    public void initializeStorage() {
        System.out.println("Initializing storage from file: " + filePath);
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        File jsonFile = new File(filePath);
        if (!jsonFile.exists()) {
            System.err.println("File not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            JsonNode rootNode = objectMapper.readTree(reader);

            rootNode.fieldNames().forEachRemaining(entityName -> {
                JsonNode entityRecords = rootNode.get(entityName);
                if (entityRecords.isArray()) {
                    List<Object> recordsList = new ArrayList<>();
                    for (JsonNode record : entityRecords) {
                        try {
                            Class c = Class.forName(modelsPackagePath + "." + entityName); //com.example.models.entityName
                            Object o = objectMapper.treeToValue(record, c);
                            recordsList.add(o);
                        } catch (ClassNotFoundException e) {
                            System.err.println("Error. Entity model not found: " + entityName);
                            e.printStackTrace();
                            break;
                        } catch (JsonProcessingException e) {
                            System.err.println("Error processing JSON for entity " + entityName + ": " + e.getMessage() + "\n Verify key-value pairs for each attribute");
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            System.err.println("Error. Invalid entity to map to:" + entityName);
                            e.printStackTrace();
                        }
                    }
                    if (recordsList.size() != 0) {
                        storage.put(entityName, recordsList);
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }

    }

    public Map<String, List<Object>> getStorage() {
        return storage;
    }

    public void addData(String key, Object value) {
        storage.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
