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

import com.epam.campus.gymcrm.mappers.TraineeMapper;
import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import com.epam.campus.gymcrm.services.impl.TraineeService;

class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeDao;

    @Mock TraineeMapper mapper;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private TraineeDto traineeDto;

    /*@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // username and password not set
        trainee = new Trainee.TraineeBuilder()
            .id(5)
            .firstName("Antony")
            .lastName("Brooke")
            .active(true)
            .dateOfBirth(LocalDate.of(1976, 8, 15))
            .address("Octopus street")
            .build();

        traineeDto = new TraineeDto(5, "Antony", "Brooke", true, LocalDate.of(1976, 8, 15), "Octopus street");
    }

    @Test
    void testGetTrainee_Found() {
        when(traineeDao.get(5)).thenReturn(Optional.of(trainee));
        when(mapper.toDto(trainee)).thenReturn(traineeDto);

        TraineeDto result = traineeService.getTrainee(5);

        assertNotNull(result);
        assertEquals("Antony", result.getFirstName());
        verify(traineeDao, times(1)).get(5);
    }

    @Test
    void testGetTrainee_NotFound() {
        when(traineeDao.get(5)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> traineeService.getTrainee(5));

        assertEquals("Trainee with id 5 not found", exception.getMessage());
    }

    @Test
    void testGetTrainees() {
        Trainee trainee2 = new Trainee.TraineeBuilder()
            .id(6)
            .firstName("Michael")
            .lastName("Jackson")
            .active(false)
            .dateOfBirth(LocalDate.of(1958, 8, 29))
            .address("456 Avenue")
            .build();
        
        TraineeDto traineeDto2 = new TraineeDto(6, "Michael", "Jackson", false, LocalDate.of(1958, 8, 29), "456 Avenue");

        when(traineeDao.getAll()).thenReturn(Arrays.asList(trainee, trainee2));
        when(mapper.toDto(trainee)).thenReturn(traineeDto);
        when(mapper.toDto(trainee2)).thenReturn(traineeDto2);

        List<TraineeDto> result = traineeService.getTrainees();

        assertEquals(2, result.size());
        verify(traineeDao, times(1)).getAll();
    }

    @Test
    void testCreateTrainee() {
        when(mapper.toTrainee(traineeDto)).thenReturn(trainee);

        traineeService.createTrainee(traineeDto);

        // Uses any in case trainee has changed 
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void testUpdateTrainee_Success() {
        when(traineeDao.get(5)).thenReturn(Optional.of(trainee));
        when(mapper.toTrainee(traineeDto, trainee)).thenReturn(trainee);

        traineeService.updateTrainee(5, traineeDto);

        // Uses any in case trainee has changed
        verify(traineeDao, times(1)).update(any(Trainee.class));
    }

    @Test
    void testUpdateTrainee_NotFound() {
        when(traineeDao.get(5)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> traineeService.updateTrainee(5, traineeDto));

        assertEquals("Trainee with id 5 not found", exception.getMessage());
    }

    @Test
    void testUpdateTrainee_IdMismatch() {
        TraineeDto invalidDto = new TraineeDto(6, "John", "Doe", true, LocalDate.of(2000, 5, 20), "123 Street");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(5, invalidDto));

        assertEquals("Id to update and id passed do not match", exception.getMessage());
    }

    @Test
    void testDeleteTrainee_Success() {
        when(traineeDao.get(5)).thenReturn(Optional.of(trainee));

        traineeService.deleteTrainee(5);

        verify(traineeDao, times(1)).delete(trainee);
    }

    @Test
    void testDeleteTrainee_NotFound() {
        when(traineeDao.get(5)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> traineeService.deleteTrainee(5));

        assertEquals("Trainee with id 5 not found", exception.getMessage());
    }*/
}

