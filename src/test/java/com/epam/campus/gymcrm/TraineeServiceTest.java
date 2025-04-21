package com.epam.campus.gymcrm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.campus.gymcrm.mappers.TraineeMapper;
import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TraineeCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.models.entities.User;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.services.impl.TraineeService;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeMapper traineeMapper;

    private final String username = "johnsmith";
    private final String password = "securePass";

    @Test
    void createTrainee_ShouldReturnLoginDto() {
        TraineeCreationDto creationDto = new TraineeCreationDto();
        creationDto.setFirstName("John");
        creationDto.setLastName("Smith");

        when(traineeRepository.getUsernames()).thenReturn(List.of());
        doAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            assertNotNull(trainee.getUser());
            return null;
        }).when(traineeRepository).save(any());

        LoginDto result = traineeService.createTrainee(creationDto);

        assertNotNull(result);
        assertNotNull(result.getUsername());
        assertNotNull(result.getPassword());
    }

    @Test
    void traineeLogin_Successful() {
        Trainee trainee = mock(Trainee.class);
        User user = new User();
        user.setPassword(password);
        when(trainee.getUser()).thenReturn(user);

        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(trainee));

        boolean result = traineeService.traineeLogin(new LoginDto(username, password));

        assertTrue(result);
    }

    @Test
    void traineeLogin_FailedDueToWrongPassword() {
        Trainee trainee = mock(Trainee.class);
        User user = new User();
        user.setPassword("differentPass");
        when(trainee.getUser()).thenReturn(user);

        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(trainee));

        boolean result = traineeService.traineeLogin(new LoginDto(username, password));

        assertFalse(result);
    }

    @Test
    void updateTraineePassword_Successful() {
        Trainee trainee = mock(Trainee.class);
        User user = new User();
        user.setPassword(password);
        when(trainee.getUser()).thenReturn(user);

        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(trainee));

        LoginChangeDto dto = new LoginChangeDto(password, "newPassword");

        boolean result = traineeService.updateTraineePassword(username, dto);

        assertTrue(result);
        verify(traineeRepository).updatePassword(username, "newPassword");
    }

    @Test
    void deleteTrainee_Success() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername(username);
        trainee.setUser(user);
    
        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(trainee));
    
        assertDoesNotThrow(() -> traineeService.deleteTrainee(username));
        verify(traineeRepository).delete(trainee);
    }
    

    @Test
    void getTraineeByUsername_NotFound_ShouldThrowException() {
        when(traineeRepository.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            traineeService.getTraineeByUsername(username)
        );
    }

    @Test
    void switchTraineeActiveStatus_Success() {
        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(mock(Trainee.class)));
        when(traineeRepository.switchActiveStatus(username)).thenReturn(true);

        boolean result = traineeService.switchTraineeActiveStatus(username);

        assertTrue(result);
        verify(traineeRepository).switchActiveStatus(username);
    }

    @Test
    void switchTraineeActiveStatus_UserNotFound_ShouldThrow() {
        when(traineeRepository.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            traineeService.switchTraineeActiveStatus(username)
        );
    }

    @Test
    void updateActiveStatus_Success() {
        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(mock(Trainee.class)));

        assertDoesNotThrow(() ->
            traineeService.updateActiveStatus(username, true)
        );
        verify(traineeRepository).updateActiveStatus(username, true);
    }

    @Test
    void updateActiveStatus_UserNotFound_ShouldThrow() {
        when(traineeRepository.getByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            traineeService.updateActiveStatus(username, false)
        );
    }
    
    @Test
    void updateTraineeTrainersList_Success() {
        Trainee trainee = new Trainee();

        // Create users
        User user1 = new User();
        user1.setUsername("trainer1");
        User user2 = new User();
        user2.setUsername("trainer2");

        // Create training types
        TrainingType type1 = new TrainingType();
        type1.setName("Yoga");
        TrainingType type2 = new TrainingType();
        type2.setName("Pilates");

        // Create trainers with user and training type
        Trainer trainer1 = new Trainer();
        trainer1.setUser(user1);
        trainer1.setTrainingType(type1);

        Trainer trainer2 = new Trainer();
        trainer2.setUser(user2);
        trainer2.setTrainingType(type2);

        List<Trainer> trainers = List.of(trainer1, trainer2);

        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeRepository.updateTrainers(eq(trainee), anyList())).thenReturn(trainers);

        List<String> trainerUsernames = List.of("trainer1", "trainer2");
        List<TrainerFromListDto> result = traineeService.updateTraineeTrainersList(username, trainerUsernames);

        assertEquals(2, result.size());
        assertEquals("trainer1", result.get(0).getUsername());
        assertEquals("Yoga", result.get(0).getSpecialization());
    }

    

    @Test
    void getActiveTrainersNotAssignedToTrainee_Success() {
        // Prepare existing trainee
        when(traineeRepository.getByUsername(username)).thenReturn(Optional.of(new Trainee()));
    
        // Prepare trainer with user and trainingType
        User user = new User();
        user.setUsername("trainer1");
    
        TrainingType type = new TrainingType();
        type.setName("Yoga");
    
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setTrainingType(type);
    
        List<Trainer> trainers = List.of(trainer);
    
        when(trainerRepository.findActiveTrainersNotAssignedToTrainee(username)).thenReturn(trainers);
    
        // Execute
        List<TrainerFromListDto> result = traineeService.getActiveTrainersNotAssignedToTrainee(username);
    
        // Verify
        assertEquals(1, result.size());
        assertEquals("trainer1", result.get(0).getUsername());
        assertEquals("Yoga", result.get(0).getSpecialization());
    }
    

}

