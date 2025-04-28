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

import com.epam.campus.gymcrm.controllers.impl.TrainingController;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingFromUserDto;
import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.services.impl.TrainingService;

import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = TrainingController.class)
@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TrainingService trainingService;

    @Test
    void testCreateTraining() {
        TrainingDto trainingDto = new TrainingDto("trainee", "trainer", "Strength Advances", "Strength", LocalDate.now(), 60);

        Training training = new Training(); // Crea un objeto de tipo Training
        when(trainingService.createTraining(trainingDto)).thenReturn(training);

        webTestClient.post()
                .uri("/gym-crm/trainings")
                .bodyValue(trainingDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Training created.");
        }

        @Test
        void testCreateTrainingValidationError() {
            TrainingDto invalidTrainingDto = new TrainingDto("trainee", "trainer", "Strength Advances", "Strength", LocalDate.now(), 60);
        
            when(trainingService.createTraining(invalidTrainingDto)).thenThrow(new NoSuchElementException("Validation failed"));

            webTestClient.post()
                    .uri("/gym-crm/trainings")
                    .bodyValue(invalidTrainingDto)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(String.class)
                    .isEqualTo("There was a validation error and training could not be added.");
        }
        

    @Test
    void testCreateTrainingPersistenceError() {
        TrainingDto trainingDto = new TrainingDto("trainee", "trainer", "Strength Advances", "Strength", LocalDate.now(), 60);

        when(trainingService.createTraining(trainingDto)).thenThrow(new PersistenceException("Database error"));

        webTestClient.post()
                .uri("/gym-crm/trainings")
                .bodyValue(trainingDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class).isEqualTo("There was an error and training could not be added.");
    }

    @Test
    void testGetTrainingsByTrainee() {
        TrainingFromUserDto training1 = new TrainingFromUserDto("training1", LocalDate.now(), "yoga", 60, "trainer1");
        TrainingFromUserDto training2 = new TrainingFromUserDto("training2", LocalDate.now(), "strenght", 120, "trainer2");

        List<TrainingFromUserDto> trainings = Arrays.asList(training1, training2);

        when(trainingService.getTrainingsByTraineeAndCriteria("User123", null, null, null, null)).thenReturn(trainings);

        webTestClient.get()
                .uri("/gym-crm/trainees/User123/trainings")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TrainingFromUserDto.class)
                .hasSize(2)
                .contains(training1, training2);
    }

    @Test
    void testGetTrainingsByTrainer() {
        TrainingFromUserDto training1 = new TrainingFromUserDto("training1", LocalDate.now(), "yoga", 60, "trainee1");
        TrainingFromUserDto training2 = new TrainingFromUserDto("training2", LocalDate.now(), "strenght", 120, "trainee2");

        List<TrainingFromUserDto> trainings = Arrays.asList(training1, training2);

        when(trainingService.getTrainingsByTrainerAndCriteria("Trainer123", null, null, null)).thenReturn(trainings);
        webTestClient.get()
                .uri("/gym-crm/trainers/Trainer123/trainings")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TrainingFromUserDto.class)
                .hasSize(2)
                .contains(training1, training2);
    }
}
