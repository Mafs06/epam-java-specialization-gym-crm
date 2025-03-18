package com.epam.campus.gymcrm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.campus.gymcrm.mappers.TrainingMapper;
import com.epam.campus.gymcrm.models.dtos.TrainingDto;
import com.epam.campus.gymcrm.models.entities.Training;
import com.epam.campus.gymcrm.repositories.TrainingRepository;
import com.epam.campus.gymcrm.services.impl.TrainingService;

class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingDao;

    @Mock
    private TrainingMapper mapper;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    private TrainingDto trainingDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        training = new Training.TrainingBuilder()
            .id(333)
            .traineeID(2)
            .trainerID(2)
            .trainingName("Strength Training")
            .trainingDate(LocalDate.of(2025, 3, 15))
            .trainingDuration(60)
            .build();
        
        trainingDto = new TrainingDto(333, 2, 2, "Strength Training", LocalDate.of(2025, 3, 15), 60);
    }

    @Test
    void testGetTraining_Found() {
        when(trainingDao.get(333)).thenReturn(Optional.of(training));
        when(mapper.toDto(training)).thenReturn(trainingDto);

        TrainingDto result = trainingService.getTraining(333);

        assertNotNull(result);
        assertEquals("Strength Training", result.getTrainingName());
        verify(trainingDao, times(1)).get(333);
    }

    @Test
    void testGetTraining_NotFound() {
        when(trainingDao.get(999)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingService.getTraining(999));

        assertEquals("Training with id 999 not found", exception.getMessage());
    }

    @Test
    void testGetTrainings() {
        Training training2 = new Training.TrainingBuilder()
            .id(444)
            .traineeID(3)
            .trainerID(3)
            .trainingName("Strength Training")
            .trainingDate(LocalDate.of(2025, 4, 20))
            .trainingDuration(45)
            .build();

        TrainingDto trainingDto2 = new TrainingDto(444, 3, 3, "Strength Training", LocalDate.of(2025, 4, 20), 45);

        when(trainingDao.getAll()).thenReturn(Arrays.asList(training, training2));
        when(mapper.toDto(training)).thenReturn(trainingDto);
        when(mapper.toDto(training2)).thenReturn(trainingDto2);

        List<TrainingDto> result = trainingService.getTrainings();

        assertEquals(2, result.size());
        verify(trainingDao, times(1)).getAll();
    }

    @Test
    void testCreateTraining() {
        when(mapper.toTraining(trainingDto)).thenReturn(training);

        trainingService.createTraining(trainingDto);
        
        // Uses any in case trainer has changed 
        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    void testUpdateTraining_Success() {
        when(trainingDao.get(333)).thenReturn(Optional.of(training));
        when(mapper.toTraining(trainingDto, training)).thenReturn(training);

        trainingService.updateTraining(333, trainingDto);

        // Uses any in case trainer has changed 
        verify(trainingDao, times(1)).update(any(Training.class));
    }

    @Test
    void testUpdateTraining_NotFound() {
        when(trainingDao.get(333)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingService.updateTraining(333, trainingDto));

        assertEquals("Training with id 333 not found", exception.getMessage());
    }

    @Test
    void testUpdateTraining_IdMismatch() {
        TrainingDto invalidDto = new TrainingDto(444, 2, 2, "Strength Training", LocalDate.of(2025, 3, 15), 60);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> trainingService.updateTraining(333, invalidDto));

        assertEquals("Id to update and id passed do not match", exception.getMessage());
    }

    @Test
    void testDeleteTraining_Success() {
        when(trainingDao.get(333)).thenReturn(Optional.of(training));

        trainingService.deleteTraining(333);

        verify(trainingDao, times(1)).delete(training);
    }

    @Test
    void testDeleteTraining_NotFound() {
        when(trainingDao.get(999)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingService.deleteTraining(999));

        assertEquals("Training with id 999 not found", exception.getMessage());
    }
}
