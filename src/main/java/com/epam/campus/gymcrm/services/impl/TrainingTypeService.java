package com.epam.campus.gymcrm.services.impl;

import java.util.NoSuchElementException;

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

    private TrainingTypeRepository trainingTypeDao;
    private TrainingTypeMapper mapper;

    @Autowired
    public void setTrainingTypeDao(TrainingTypeRepository trainingTypeDao) {
        this.trainingTypeDao = trainingTypeDao;
    }

    @Autowired
    public void setMapper(TrainingTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TrainingTypeDto getTrainingType(int id) {
        logger.info("Fetching training type with ID: {}", id);
        TrainingType trainingType = trainingTypeDao.get(id)
            .orElseThrow(() -> new NoSuchElementException("Training type with id %s not found".formatted(id)));

        return mapper.toDto(trainingType);
    }

}
