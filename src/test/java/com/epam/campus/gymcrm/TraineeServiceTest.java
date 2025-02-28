package com.epam.campus.gymcrm;

import com.epam.campus.gymcrm.models.Trainee;
import com.epam.campus.gymcrm.daos.TraineeDao;
import com.epam.campus.gymcrm.servises.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTrainee() {
        Trainee trainee = new Trainee(1, "John", "Doe", "johndoe", "pass123", true, null, "123 Street");
        when(traineeDao.get(1)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.getTrainee(1);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(traineeDao, times(1)).get(1);
    }

    @Test
    public void testGetTrainees() {
        Trainee trainee1 = new Trainee(1, "John", "Doe", "johndoe", "pass123", true, null, "123 Street");
        Trainee trainee2 = new Trainee(2, "Jane", "Doe", "janedoe", "pass123", true, null, "456 Avenue");
        when(traineeDao.getAll()).thenReturn(Arrays.asList(trainee1, trainee2));

        List<Trainee> result = traineeService.getTrainees();

        assertEquals(2, result.size());
        verify(traineeDao, times(1)).getAll();
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee(1, "John", "Doe", "johndoe", "pass123", true, null, "123 Street");

        traineeService.createTrainee(trainee);

        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    public void testUpdateTrainee() {
        Trainee trainee = new Trainee(1, "John", "Doe", "johndoe", "pass123", true, null, "123 Street");
        String[] params = {"1", "John", "Smith", "johnsmith", "newpass", "true", "789 Road"};

        traineeService.updateTrainee(trainee, params);

        verify(traineeDao, times(1)).update(trainee, params);
    }

    @Test
    public void testDeleteTrainee() {
        Trainee trainee = new Trainee(1, "John", "Doe", "johndoe", "pass123", true, null, "123 Street");

        traineeService.deleteTrainee(trainee);

        verify(traineeDao, times(1)).delete(trainee);
    }
}

