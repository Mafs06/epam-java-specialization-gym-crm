package com.epam.campus.gymcrm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.epam.campus.gymcrm.mappers.TrainerMapper;
import com.epam.campus.gymcrm.models.dtos.TrainerDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import com.epam.campus.gymcrm.services.impl.TrainerService;


class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerDao;

    @Mock
    private TrainerMapper mapper;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private TrainerDto trainerDto;

    /*@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // username and password not set
        trainer = new Trainer.TrainerBuilder()
            .id(5)
            .firstName("John")
            .lastName("Doe")
            .active(true)
            .specialization("Fitness")
            .build();

        trainerDto = new TrainerDto(5, "John", "Doe", true, "Fitness");
    }

    @Test
    void testGetTrainer_Found() {
        when(trainerDao.get(5)).thenReturn(Optional.of(trainer));
        when(mapper.toDto(trainer)).thenReturn(trainerDto);

        TrainerDto result = trainerService.getTrainer(5);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(trainerDao, times(1)).get(5);
    }

    @Test
    void testGetTrainer_NotFound() {
        when(trainerDao.get(5)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainerService.getTrainer(5));

        assertEquals("Trainer with id 5 not found", exception.getMessage());
    }

    @Test
    void testGetAllTrainers() {
        Trainer trainer2 = new Trainer.TrainerBuilder()
            .id(6)
            .firstName("Jane")
            .lastName("Doe")
            .active(true)
            .specialization("Yoga")
            .build();

        TrainerDto trainerDto2 = new TrainerDto(6, "Jane", "Doe", true, "Yoga");

        when(trainerDao.getAll()).thenReturn(Arrays.asList(trainer, trainer2));
        when(mapper.toDto(trainer)).thenReturn(trainerDto);
        when(mapper.toDto(trainer2)).thenReturn(trainerDto2);

        List<TrainerDto> result = trainerService.getTrainers();

        assertEquals(2, result.size());
        verify(trainerDao, times(1)).getAll();
    }

    @Test
    void testCreateTrainer() {
        when(mapper.toTrainer(trainerDto)).thenReturn(trainer);

        trainerService.createTrainer(trainerDto);

        // Uses any in case trainer has changed 
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer_Success() {  
        when(trainerDao.get(5)).thenReturn(Optional.of(trainer));
        when(mapper.toTrainer(trainerDto, trainer)).thenReturn(trainer);

        trainerService.updateTrainer(5, trainerDto);

        // Uses any in case trainer has changed 
        verify(trainerDao, times(1)).update(any(Trainer.class));
    }

    @Test
    void testUpdateTrainer_NotFound() {
        when(trainerDao.get(5)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainerService.updateTrainer(5, trainerDto));

        assertEquals("Trainer with id 5 not found", exception.getMessage());
    }

    @Test
    void testUpdateTrainer_IdMismatch() {
        TrainerDto invalidDto = new TrainerDto(6, "John", "Doe", true,"Jumps");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(5, invalidDto));

        assertEquals("Id to update and id passed do not match", exception.getMessage());
    }

    @Test
    void testDeleteTrainer_Success() {
        when(trainerDao.get(5)).thenReturn(Optional.of(trainer));

        trainerService.deleteTrainer(5);

        verify(trainerDao, times(1)).delete(trainer);
    }

    @Test
    void testDeleteTrainer_NotFound() {
        when(trainerDao.get(5)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainerService.deleteTrainer(5));

        assertEquals("Trainer with id 5 not found", exception.getMessage());
    }*/
}
