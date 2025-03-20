package com.epam.campus.gymcrm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.epam.campus.gymcrm.facade.GymFacade;
import com.epam.campus.gymcrm.services.impl.TraineeService;
import com.epam.campus.gymcrm.services.impl.TrainerService;
import com.epam.campus.gymcrm.services.impl.TrainingService;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;

@Configuration
@ComponentScan(basePackages = "com.epam.campus.gymcrm")
@PropertySource("classpath:application.properties")
public class Config {

    //Java-based configuration for GymFacade
    @Bean
    public GymFacade gymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService, TrainingTypeService trainingTypeService) {
        return new GymFacade(trainerService, traineeService, trainingService, trainingTypeService);
    }

}
