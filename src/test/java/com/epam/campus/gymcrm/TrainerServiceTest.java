package com.epam.campus.gymcrm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.epam.campus.gymcrm.models.dtos.*;
import com.epam.campus.gymcrm.models.entities.*;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.repositories.TrainingTypeRepository;
import com.epam.campus.gymcrm.services.impl.TrainerService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    private final String username = "trainer1";
    private final String password = "securePass";

    @Test
    void createTrainer_ShouldReturnLoginDto() {
        TrainerCreationDto dto = new TrainerCreationDto();
        dto.setFirstName("Ana");
        dto.setLastName("Gomez");
        dto.setSpecialization("Yoga");

        when(trainerRepository.getUsernames()).thenReturn(List.of());

        TrainingType trainingType = new TrainingType();
        trainingType.setName("Yoga");
        when(trainingTypeRepository.getByName("Yoga")).thenReturn(Optional.of(trainingType));

        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            assertNotNull(trainer.getUser());
            return null;
        }).when(trainerRepository).save(any());

        LoginDto result = trainerService.createTrainer(dto);

        assertNotNull(result);
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
    }

    @Test
    void trainerLogin_Successful() {
        Trainer trainer = mock(Trainer.class);
        User user = new User();
        user.setPassword(password);
        when(trainer.getUser()).thenReturn(user);

        when(trainerRepository.getByUsername(username)).thenReturn(Optional.of(trainer));

        boolean result = trainerService.trainerLogin(new LoginDto(username, password));

        assertTrue(result);
    }

    @Test
    void trainerLogin_FailedDueToWrongPassword() {
        Trainer trainer = mock(Trainer.class);
        User user = new User();
        user.setPassword("wrongPass");
        when(trainer.getUser()).thenReturn(user);

        when(trainerRepository.getByUsername(username)).thenReturn(Optional.of(trainer));

        boolean result = trainerService.trainerLogin(new LoginDto(username, password));

        assertFalse(result);
    }

    @Test
    void updateTrainerPassword_Success() {
        Trainer trainer = mock(Trainer.class);
        User user = new User();
        user.setPassword(password);
        when(trainer.getUser()).thenReturn(user);
        when(trainerRepository.getByUsername(username)).thenReturn(Optional.of(trainer));

        LoginChangeDto dto = new LoginChangeDto(password, "newPass");

        boolean result = trainerService.updateTrainerPassword(username, dto);

        assertTrue(result);
        verify(trainerRepository).updatePassword(username, "newPass");
    }

    @Test
    void getTrainerByUsername_NotFound_ShouldThrow() {
        when(trainerRepository.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            trainerService.getTrainerByUsername(username)
        );
    }

    @Test
    void switchTrainerActiveStatus_Success() {
        when(trainerRepository.getByUsername(username)).thenReturn(Optional.of(mock(Trainer.class)));
        when(trainerRepository.switchActiveStatus(username)).thenReturn(false);

        boolean result = trainerService.switchTrainerActiveStatus(username);

        assertFalse(result);
        verify(trainerRepository).switchActiveStatus(username);
    }

    @Test
    void switchTrainerActiveStatus_NotFound_ShouldThrow() {
        when(trainerRepository.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            trainerService.switchTrainerActiveStatus(username)
        );
    }

    @Test
    void updateActiveStatus_Success() {
        when(trainerRepository.getByUsername(username)).thenReturn(Optional.of(mock(Trainer.class)));

        assertDoesNotThrow(() -> trainerService.updateActiveStatus(username, true));
        verify(trainerRepository).updateActiveStatus(username, true);
    }

    @Test
    void updateActiveStatus_NotFound_ShouldThrow() {
        when(trainerRepository.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            trainerService.updateActiveStatus(username, true)
        );
    }
}
