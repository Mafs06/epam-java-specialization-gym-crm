package com.epam.campus.gymcrm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.epam.campus.gymcrm.mappers.TrainingMapper;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.dtos.TrainingFromUserDto;
import com.epam.campus.gymcrm.models.entities.*;
import com.epam.campus.gymcrm.repositories.*;
import com.epam.campus.gymcrm.services.impl.TrainingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingMapper trainingMapper;

    private final String traineeUsername = "trainee1";
    private final String trainerUsername = "trainer1";
    private final String trainingTypeName = "Yoga";

    @Test
    void createTraining_ShouldSucceed() {
        TrainingDto dto = new TrainingDto();
        dto.setTraineeUsername(traineeUsername);
        dto.setTrainerUsername(trainerUsername);
        dto.setTrainingTypeName(trainingTypeName);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        Training training = new Training();

        when(traineeRepository.getByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerRepository.getByUsername(trainerUsername)).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.getByName(trainingTypeName)).thenReturn(Optional.of(trainingType));
        when(trainingMapper.toTraining(dto, trainee, trainer, trainingType)).thenReturn(training);

        assertDoesNotThrow(() -> trainingService.createTraining(dto));
        verify(trainingRepository).save(training);
    }

    @Test
    void createTraining_InvalidTrainee_ShouldThrow() {
        TrainingDto dto = new TrainingDto();
        dto.setTraineeUsername("invalidTrainee");

        when(traineeRepository.getByUsername("invalidTrainee")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
            trainingService.createTraining(dto)
        );
    }

    @Test
    void getTrainingsByTraineeAndCriteria_ShouldReturnDtos() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        Training training = new Training();
        TrainingFromUserDto dto = new TrainingFromUserDto();

        when(trainingRepository.findTrainingsByTraineeAndCriteria(
                traineeUsername, from, to, trainerUsername, trainingTypeName
        )).thenReturn(List.of(training));

        when(trainingMapper.toTrainingFromTraineeDto(training)).thenReturn(dto);

        List<TrainingFromUserDto> result = trainingService.getTrainingsByTraineeAndCriteria(
            traineeUsername, from, to, trainerUsername, trainingTypeName
        );

        assertEquals(1, result.size());
    }

    @Test
    void getTrainingsByTrainerAndCriteria_ShouldReturnDtos() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        Training training = new Training();
        TrainingFromUserDto dto = new TrainingFromUserDto();

        when(trainingRepository.findTrainingsByTrainerAndCriteria(
                trainerUsername, from, to, traineeUsername
        )).thenReturn(List.of(training));

        when(trainingMapper.toTrainingFromTraineeDto(training)).thenReturn(dto);

        List<TrainingFromUserDto> result = trainingService.getTrainingsByTrainerAndCriteria(
            trainerUsername, from, to, traineeUsername
        );

        assertEquals(1, result.size());
    }
}
