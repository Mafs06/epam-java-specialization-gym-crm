package com.epam.campus.gymcrm;

import com.epam.campus.gymcrm.controllers.TraineeController;
import com.epam.campus.gymcrm.models.dtos.*;
import com.epam.campus.gymcrm.services.impl.TraineeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = TraineeController.class)
class TraineeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TraineeService traineeService;

    @Test
    void traineeLogin_shouldReturnWelcomeMessage_whenCredentialsAreValid() {
        LoginDto loginDto = new LoginDto("johnDoe", "securePass");

        when(traineeService.traineeLogin(any(LoginDto.class))).thenReturn(true);

        webTestClient.method(HttpMethod.GET)
            .uri("/gym-crm/trainees/login")
            .body(BodyInserters.fromValue(loginDto))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("Welcome johnDoe");

        Mockito.verify(traineeService).traineeLogin(any(LoginDto.class));
    }

    @Test
    void createTrainee_shouldReturnLoginDto_whenSuccessful() {
        TraineeCreationDto newTrainee = new TraineeCreationDto("john", "Doe", null, null);
        LoginDto expectedLogin = new LoginDto("johnDoe", "hashedPassword");

        when(traineeService.createTrainee(any(TraineeCreationDto.class))).thenReturn(expectedLogin);

        webTestClient.post()
                .uri("/gym-crm/trainees")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newTrainee)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("johnDoe")
                .jsonPath("$.password").isEqualTo("hashedPassword");
    }

    @Test
    void getTraineeByUsername_shouldReturnNotFound_whenUserDoesNotExist() {
        when(traineeService.getTraineeByUsername("unknown"))
                .thenThrow(new NoSuchElementException());

        webTestClient.get()
                .uri("/gym-crm/trainees/unknown")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void switchTraineeActiveStatus_shouldReturnNewStatus_whenSuccessful() {
        when(traineeService.switchTraineeActiveStatus("johnDoe")).thenReturn(true);

        webTestClient.patch()
                .uri("/gym-crm/trainees/johnDoe/active")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Active status changed to true");
    }

    @Test
    void updateTraineeTrainers_shouldReturnUpdatedList_whenSuccessful() {
        List<String> newTrainers = List.of("trainer1", "trainer2");
        List<TrainerFromListDto> trainers = List.of(
                new TrainerFromListDto("trainer1", "trainer", "1", "yoga"),
                new TrainerFromListDto("trainer2", "trainer", "2", "yoga")
        );

        when(traineeService.updateTraineeTrainersList("johnDoe", newTrainers)).thenReturn(trainers);

        webTestClient.put()
                .uri("/gym-crm/trainees/johnDoe/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new java.util.HashMap<>() {{
                    put("newTrainers", newTrainers);
                }})
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$[0].username").isEqualTo("trainer1")
                    .jsonPath("$[0].firstName").isEqualTo("trainer")
                    .jsonPath("$[0].lastName").isEqualTo("1")
                    .jsonPath("$[0].specialization").isEqualTo("yoga")
                    .jsonPath("$[1].username").isEqualTo("trainer2")
                    .jsonPath("$[1].firstName").isEqualTo("trainer")
                    .jsonPath("$[1].lastName").isEqualTo("2")
                    .jsonPath("$[1].specialization").isEqualTo("yoga");
    }
}
