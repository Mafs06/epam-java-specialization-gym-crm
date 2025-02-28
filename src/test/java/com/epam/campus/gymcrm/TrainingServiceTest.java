package com.epam.campus.gymcrm;

import com.epam.campus.gymcrm.models.Training;
import com.epam.campus.gymcrm.models.TrainingType;
import com.epam.campus.gymcrm.daos.TrainingDao;
import com.epam.campus.gymcrm.servises.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTraining() {
        TrainingType trainingType = new TrainingType("Strength Training Advanced");
        Training training = new Training(1, 100, 110, "Strength Training", trainingType, LocalDate.of(2025, 3, 15), 60);
        when(trainingDao.get(1)).thenReturn(Optional.of(training));

        Optional<Training> result = trainingService.getTraining(1);

        assertTrue(result.isPresent());
        assertEquals("Strength Training", result.get().getTrainingName());
        verify(trainingDao, times(1)).get(1);
    }

    @Test
    public void testGetTrainings() {
        TrainingType trainingType = new TrainingType("Strength Training Advanced");
        Training training1 = new Training(1, 100, 110, "Strength Training", trainingType, LocalDate.of(2025, 3, 15), 60);
        Training training2 = new Training(2, 101, 111, "Strength Training", trainingType, LocalDate.of(2025, 4, 20), 45);
        when(trainingDao.getAll()).thenReturn(Arrays.asList(training1, training2));

        List<Training> result = trainingService.getTrainings();

        assertEquals(2, result.size());
        verify(trainingDao, times(1)).getAll();
    }

    @Test
    public void testCreateTraining() {
        TrainingType trainingType = new TrainingType("Strength Training Advanced");
        Training training = new Training(1, 100, 110, "Strength Training", trainingType, LocalDate.of(2025, 3, 15), 60);

        trainingService.createTraining(training);

        verify(trainingDao, times(1)).save(training);
    }

    @Test
    public void testUpdateTraining() {
        TrainingType trainingType = new TrainingType("Strength Training Advanced");
        Training training = new Training(1, 100, 110, "Strength Training", trainingType, LocalDate.of(2025, 3, 15), 60);
        String[] params = {"1", "101", "111", "Updated Training", "", "2025-03-20", "75"};

        trainingService.updateTraining(training, params);

        verify(trainingDao, times(1)).update(training, params);
    }

    @Test
    public void testDeleteTraining() {
        TrainingType trainingType = new TrainingType("Strength Training Advanced");
        Training training = new Training(1, 100, 110, "Strength Training", trainingType, LocalDate.of(2025, 3, 15), 60);

        trainingService.deleteTraining(training);

        verify(trainingDao, times(1)).delete(training);
    }
}

