package com.epam.campus.gymcrm;

import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.repositories.TraineeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TraineeRepositoryTest {

    @InjectMocks
    private TraineeRepository traineeRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<String> traineeStringQuery;

    @Mock
    private TypedQuery<String> trainerStringQuery;

    @Mock
    private TypedQuery<Trainee> traineeQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @Mock
    private TypedQuery<Boolean> booleanQuery;

    @Mock
    private Query query;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTrainee() {
        Trainee trainee = new Trainee();
        assertDoesNotThrow(() -> traineeRepository.save(trainee));
        verify(entityManager).persist(trainee);
    }

    @Test
    void getUsernames() {
        when(entityManager.createQuery(contains("FROM Trainee"), eq(String.class)))
                .thenReturn(traineeStringQuery);
        when(entityManager.createQuery(contains("FROM Trainer"), eq(String.class)))
                .thenReturn(trainerStringQuery);

        when(traineeStringQuery.getResultList()).thenReturn(List.of("trainee1"));
        when(trainerStringQuery.getResultList()).thenReturn(List.of("trainer1"));

        List<String> usernames = traineeRepository.getUsernames();

        assertNotNull(usernames);
        assertTrue(usernames.contains("trainee1"));
        assertTrue(usernames.contains("trainer1"));
    }

    @Test
    void getByUsername_NotFound() {
        when(entityManager.createQuery(anyString(), eq(Object.class)))
                .thenThrow(new NoResultException());

        Optional<Object> result = traineeRepository.getByUsername("nonexistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void updatePassword_Success() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> traineeRepository.updatePassword("username", "newPassword"));
    }

    @Test
    void updatePassword_NotFound() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        assertThrows(NoSuchElementException.class, () ->
            traineeRepository.updatePassword("missingUser", "newPassword")
        );
    }

    @Test
    void switchActiveStatus_Success() {
        when(entityManager.createQuery(anyString(), eq(Boolean.class)))
                .thenReturn(booleanQuery);
        when(booleanQuery.setParameter(anyString(), any()))
                .thenReturn(booleanQuery);
        when(booleanQuery.getSingleResult())
                .thenReturn(true);

        when(entityManager.createQuery(contains("UPDATE User u")))
                .thenReturn(query);
        when(query.setParameter(anyString(), any()))
                .thenReturn(query);
        when(query.executeUpdate())
                .thenReturn(1);

        boolean result = traineeRepository.switchActiveStatus("testUser");

        assertFalse(result);
    }

    @Test
    void switchActiveStatus_NotFound() {
        when(entityManager.createQuery(anyString(), eq(Boolean.class)))
                .thenReturn(booleanQuery);
        when(booleanQuery.setParameter(anyString(), any()))
                .thenReturn(booleanQuery);
        when(booleanQuery.getSingleResult())
                .thenReturn(false);

        when(entityManager.createQuery(contains("UPDATE User u")))
                .thenReturn(query);
        when(query.setParameter(anyString(), any()))
                .thenReturn(query);
        when(query.executeUpdate())
                .thenReturn(0);

        assertThrows(NoSuchElementException.class, () ->
                traineeRepository.switchActiveStatus("ghostUser")
        );
    }

    @Test
    void updateActiveStatus_Success() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> traineeRepository.updateActiveStatus("activeUser", true));
    }

    @Test
    void updateActiveStatus_NotFound() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        assertThrows(NoSuchElementException.class, () ->
            traineeRepository.updateActiveStatus("missingUser", true)
        );
    }

    @Test
    void updateTrainers_Success() {
        Trainee trainee = new Trainee();
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");
        List<Trainer> expectedTrainers = Arrays.asList(new Trainer(), new Trainer());

        when(entityManager.createQuery(anyString(), eq(Trainer.class)))
                .thenReturn(trainerQuery);
        when(trainerQuery.setParameter(anyString(), any()))
                .thenReturn(trainerQuery);
        when(trainerQuery.getResultList())
                .thenReturn(expectedTrainers);

        List<Trainer> result = traineeRepository.updateTrainers(trainee, trainerUsernames);

        assertEquals(expectedTrainers.size(), result.size());
    }
}
