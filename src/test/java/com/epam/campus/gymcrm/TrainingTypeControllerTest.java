package com.epam.campus.gymcrm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.epam.campus.gymcrm.controllers.impl.TrainingTypeController;
import com.epam.campus.gymcrm.models.dtos.TrainingTypeDto;
import com.epam.campus.gymcrm.services.impl.TrainingTypeService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = TrainingTypeController.class)
@ExtendWith(MockitoExtension.class)
public class TrainingTypeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        TrainingTypeDto trainingTypeDto1 = new TrainingTypeDto("Strength Training");
        TrainingTypeDto trainingTypeDto2 = new TrainingTypeDto("Cardio");

        List<TrainingTypeDto> trainingTypeDtos = Arrays.asList(trainingTypeDto1, trainingTypeDto2);

        when(trainingTypeService.getTrainingTypes()).thenReturn(trainingTypeDtos);
    }

    @Test
    void testGetTrainingTypes() {
        webTestClient.get()
            .uri("/gym-crm/training-types")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].name").isEqualTo("Strength Training")
            .jsonPath("$[1].name").isEqualTo("Cardio");
    }
}
