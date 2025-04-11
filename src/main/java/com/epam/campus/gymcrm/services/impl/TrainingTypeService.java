package com.epam.campus.gymcrm.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.campus.gymcrm.mappers.TrainingTypeMapper;
import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.repositories.TrainingTypeRepository;
import com.epam.campus.gymcrm.services.ITrainingTypeService;

@Service
public class TrainingTypeService implements ITrainingTypeService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeService.class);

    private TrainingTypeRepository trainingTypeRepository;
    private TrainingTypeMapper mapper;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper mapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public void createTrainingType(TrainingTypeDto newTrainingTypeDto) {
        logger.info("Adding new training type with data: {}", newTrainingTypeDto);

        TrainingType trainingType = mapper.toTrainingType(newTrainingTypeDto);

        trainingTypeRepository.save(trainingType);
        logger.info("Training type created: {}", trainingType);
        System.out.println("Training type created");
    }

    @Override
    public TrainingTypeDto getTrainingTypeByName(String name) {
        logger.info("Fetching training type with name: {}", name);

        TrainingType trainingType = trainingTypeRepository.getByName(name)
            .orElseThrow(() -> new NoSuchElementException("Training type with name %s not found".formatted(name)));

        return mapper.toDto(trainingType);
    }

    @Override
    public List<TrainingTypeDto> getTrainingTypes() {
        logger.info("Fetching all training types");

        List<TrainingType> trainingTypes = trainingTypeRepository.getAll();

        return trainingTypes.stream().map(mapper::toDto).collect(Collectors.toList());
    }

}
