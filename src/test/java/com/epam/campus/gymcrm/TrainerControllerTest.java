package com.epam.campus.gymcrm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.epam.campus.gymcrm.controllers.TrainerController;
import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TraineeFromListDto;
import com.epam.campus.gymcrm.models.dtos.TrainerCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateResponseDto;
import com.epam.campus.gymcrm.services.impl.TrainerService;

@WebFluxTest(controllers = TrainerController.class)
class TrainerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TrainerService trainerService;

    @Test
    void trainerLogin_shouldReturnWelcomeMessage_whenCredentialsAreValid() {
        LoginDto loginDto = new LoginDto("trainer1", "securePass");

        when(trainerService.trainerLogin(any(LoginDto.class))).thenReturn(true);

        webTestClient.method(HttpMethod.GET)
            .uri("/gym-crm/trainers/login")
            .body(BodyInserters.fromValue(loginDto))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("Welcome trainer1");

        verify(trainerService).trainerLogin(any(LoginDto.class));
    }

    @Test
    void createTrainer_shouldReturnLoginDto_whenSuccessful() {
        TrainerCreationDto newTrainer = new TrainerCreationDto("trainer", "1", "strength");
        LoginDto expectedLogin = new LoginDto("trainer1", "securePass");

        when(trainerService.createTrainer(any(TrainerCreationDto.class))).thenReturn(expectedLogin);

        webTestClient.post()
            .uri("/gym-crm/trainers")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newTrainer)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.username").isEqualTo("trainer1")
            .jsonPath("$.password").isEqualTo("securePass");
    }

    @Test
    void getTrainerByUsername_shouldReturnNotFound_whenUserDoesNotExist() {
        when(trainerService.getTrainerByUsername("unknown"))
            .thenThrow(new NoSuchElementException());

        webTestClient.get()
            .uri("/gym-crm/trainers/unknown")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void updateTrainer_shouldReturnUpdatedTrainer_whenSuccessful() {
        TrainerUpdateRequestDto updateRequest = new TrainerUpdateRequestDto("NewName", "LastName", "cardio", false);
        List<TraineeFromListDto> trainees = List.of(
            new TraineeFromListDto("trainee1", "trainee", "1")
        );
        TrainerUpdateResponseDto updateResponse = new TrainerUpdateResponseDto("trainer1", "NewName", "LastName", false, trainees);

        when(trainerService.updateTrainer(eq("trainer1"), any(TrainerUpdateRequestDto.class))).thenReturn(updateResponse);

        webTestClient.put()
            .uri("/gym-crm/trainers/trainer1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateRequest)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.username").isEqualTo("trainer1")
            .jsonPath("$.firstName").isEqualTo("NewName");
    }

    @Test
    void updateTrainerPassword_shouldReturnSuccessMessage_whenSuccessful() {
        LoginChangeDto loginChangeDto = new LoginChangeDto("oldPass", "newPass");

        when(trainerService.updateTrainerPassword(eq("trainer1"), any(LoginChangeDto.class))).thenReturn(true);


        webTestClient.put()
            .uri("/gym-crm/trainers/trainer1/password")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(loginChangeDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("Password changed.");
    }

    @Test
    void switchTrainerActiveStatus_shouldReturnNewStatus_whenSuccessful() {
        when(trainerService.switchTrainerActiveStatus("trainer1")).thenReturn(true);

        webTestClient.patch()
            .uri("/gym-crm/trainers/trainer1/active")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .isEqualTo("Active status changed to true");
    }
}
