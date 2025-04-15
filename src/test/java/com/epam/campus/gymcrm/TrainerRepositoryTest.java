package com.epam.campus.gymcrm;

import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.repositories.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TrainerRepositoryTest {

    @InjectMocks
    private TrainerRepository trainerRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private TypedQuery<String> trainerStringQuery;

    @Mock
    private TypedQuery<String> traineeStringQuery;

    @Mock
    private TypedQuery<Boolean> booleanQuery;

    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsernames_Success() {
        when(entityManager.createQuery(contains("FROM Trainer"), eq(String.class)))
                .thenReturn(trainerStringQuery);
        when(entityManager.createQuery(contains("FROM Trainee"), eq(String.class)))
                .thenReturn(traineeStringQuery);

        when(trainerStringQuery.getResultList()).thenReturn(List.of("trainer1"));
        when(traineeStringQuery.getResultList()).thenReturn(List.of("trainee1"));

        List<String> usernames = trainerRepository.getUsernames();

        assertNotNull(usernames);
        assertTrue(usernames.contains("trainer1"));
        assertTrue(usernames.contains("trainee1"));
    }

    @Test
    void getByUsername_NotFound() {
        when(entityManager.createQuery(anyString(), eq(Object.class)))
                .thenThrow(new NoResultException());

        Optional<Object> result = trainerRepository.getByUsername("nonexistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void updatePassword_Success() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> trainerRepository.updatePassword("username", "newPassword"));
    }

    @Test
    void updatePassword_NotFound() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        assertThrows(NoSuchElementException.class, () ->
                trainerRepository.updatePassword("missingUser", "newPassword")
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

        when(entityManager.createQuery(contains("UPDATE User u"))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        boolean result = trainerRepository.switchActiveStatus("testUser");

        assertFalse(result); // Due to switch of the value
    }

    @Test
    void switchActiveStatus_NotFound() {
        when(entityManager.createQuery(anyString(), eq(Boolean.class)))
                .thenReturn(booleanQuery);
        when(booleanQuery.setParameter(anyString(), any()))
                .thenReturn(booleanQuery);
        when(booleanQuery.getSingleResult()).thenReturn(false);

        when(entityManager.createQuery(contains("UPDATE User u"))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        assertThrows(NoSuchElementException.class, () ->
                trainerRepository.switchActiveStatus("ghostUser")
        );
    }

    @Test
    void updateActiveStatus_Success() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> trainerRepository.updateActiveStatus("activeUser", true));
    }

    @Test
    void updateActiveStatus_NotFound() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(0);

        assertThrows(NoSuchElementException.class, () ->
                trainerRepository.updateActiveStatus("missingUser", true)
        );
    }

}


