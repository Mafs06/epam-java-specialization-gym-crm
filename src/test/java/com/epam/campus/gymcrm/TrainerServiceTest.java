package com.epam.campus.gymcrm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.campus.gymcrm.daos.TrainerDao;
import com.epam.campus.gymcrm.models.Trainer;
import com.epam.campus.gymcrm.servises.TrainerService;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer(1, "John", "Doe", true, "Fitness");
        trainerService.createTrainer(trainer);

        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    public void testGetTrainer() {
        Trainer trainer = new Trainer(1, "John", "Doe", true, "Fitness");
        when(trainerDao.get(1)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.getTrainer(1);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> trainers = List.of(
            new Trainer(1, "John", "Doe", true, "Fitness"),
            new Trainer(2, "Jane", "Doe", true, "Yoga")
        );
        when(trainerDao.getAll()).thenReturn(trainers);

        List<Trainer> result = trainerService.getTrainers();

        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateTrainer() {
        Trainer trainer = new Trainer(1, "John", "Doe", true, "Fitness");
        String[] params = {"1", "Johnny", "Doe", "johnnydoe", "newpass", "true", "Cardio"};

        trainerService.updateTrainer(trainer, params);

        verify(trainerDao, times(1)).update(trainer, params);
    }

    @Test
    public void testDeleteTrainer() {
        Trainer trainer = new Trainer(1, "John", "Doe", true, "Fitness");

        trainerService.deleteTrainer(trainer);

        verify(trainerDao, times(1)).delete(trainer);
    }
}

