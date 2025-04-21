package com.epam.campus.gymcrm;

import com.epam.campus.gymcrm.models.entities.*;
import com.epam.campus.gymcrm.repositories.TrainingRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrainingRepositoryTest {

    @InjectMocks
    private TrainingRepository trainingRepository;

    @Mock
    private EntityManager em;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private CriteriaQuery<Training> cq;

    @Mock
    private Root<Training> trainingRoot;

    @Mock
    private Join<Training, Trainee> traineeJoin;

    @Mock
    private Join<Trainee, User> traineeUserJoin;

    @Mock
    private Join<Training, Trainer> trainerJoin;

    @Mock
    private Join<Trainer, User> trainerUserJoin;

    @Mock
    private Join<Training, TrainingType> trainingTypeJoin;

    @Mock
    private TypedQuery<Training> typedQuery;

    @Mock
    private Path<LocalDate> datePath;

    @Mock
    private Fetch<Training, Trainee> traineeFetch;

    @Mock
    private Fetch<Trainee, User> traineeUserFetch;

    @Mock
    private Fetch<Training, Trainer> trainerFetch;

    @Mock
    private Fetch<Trainer, User> trainerUserFetch;

    @Mock
    private Fetch<Training, TrainingType> trainingTypeFetch;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // For typed joins
        traineeJoin = mock(Join.class); // Trainee join
        traineeUserJoin = mock(Join.class); // User join from Trainee

        trainerJoin = mock(Join.class); // Trainer join
        trainerUserJoin = mock(Join.class); // User join from Trainer

        trainingTypeJoin = mock(Join.class); // TrainingType join
    }

    @Test
    void findTrainingsByTraineeAndCriteria_Success() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
    
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Training.class)).thenReturn(cq);
        when(cq.from(Training.class)).thenReturn(trainingRoot);
    
        // Mocks for fetch
        when(trainingRoot.fetch("trainee", JoinType.LEFT)).thenReturn((Fetch) traineeFetch);
        when(traineeFetch.fetch("user", JoinType.LEFT)).thenReturn((Fetch) traineeUserFetch);
    
        when(trainingRoot.fetch("trainer", JoinType.LEFT)).thenReturn((Fetch) trainerFetch);
        when(trainerFetch.fetch("user", JoinType.LEFT)).thenReturn((Fetch) trainerUserFetch);
    
        when(trainingRoot.fetch("trainingType", JoinType.LEFT)).thenReturn((Fetch) trainingTypeFetch);
    
        // Mocks for joins
        when(trainingRoot.join("trainee")).thenReturn((Join) traineeJoin);
        when(traineeJoin.join("user")).thenReturn((Join) traineeUserJoin);
    
        when(trainingRoot.join("trainer")).thenReturn((Join) trainerJoin);
        when(trainerJoin.join("user")).thenReturn((Join) trainerUserJoin);
    
        when(trainingRoot.join("trainingType")).thenReturn((Join) trainingTypeJoin);
    
        // Mocks for equal and lower
        when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(cb.lower(any())).thenReturn(mock(Expression.class));
    
        // Date Mocks
        when(trainingRoot.get("date")).thenReturn((Path) datePath);
        when(cb.greaterThanOrEqualTo(datePath, fromDate)).thenReturn(mock(Predicate.class));
        when(cb.lessThanOrEqualTo(datePath, toDate)).thenReturn(mock(Predicate.class));
    
        when(cb.and(any(Predicate[].class))).thenReturn(mock(Predicate.class));
    
        when(cq.select(trainingRoot)).thenReturn(cq);
        when(cq.where(any(Predicate.class))).thenReturn(cq);
    
        when(em.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new Training()));
    
        List<Training> result = trainingRepository.findTrainingsByTraineeAndCriteria(
                "trainee1", fromDate, toDate, "trainer1", "yoga"
        );
    
        assertNotNull(result);
        assertEquals(1, result.size());
    }
        

    @Test
    void findTrainingsByTrainerAndCriteria_Success() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 12, 31);
    
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Training.class)).thenReturn(cq);
        when(cq.from(Training.class)).thenReturn(trainingRoot);
    
        // Mocks for fetch
        when(trainingRoot.fetch("trainee", JoinType.LEFT)).thenReturn((Fetch) traineeFetch);
        when(traineeFetch.fetch("user", JoinType.LEFT)).thenReturn((Fetch) traineeUserFetch);
    
        when(trainingRoot.fetch("trainer", JoinType.LEFT)).thenReturn((Fetch) trainerFetch);
        when(trainerFetch.fetch("user", JoinType.LEFT)).thenReturn((Fetch) trainerUserFetch);
    
        when(trainingRoot.fetch("trainingType", JoinType.LEFT)).thenReturn((Fetch) trainingTypeFetch);
    
        // Mocks for joins
        when(trainingRoot.join("trainer")).thenReturn((Join) trainerJoin);
        when(trainerJoin.join("user")).thenReturn((Join) trainerUserJoin);
    
        when(trainingRoot.join("trainee")).thenReturn((Join) traineeJoin);
        when(traineeJoin.join("user")).thenReturn((Join) traineeUserJoin);
    
        when(trainingRoot.join("trainingType")).thenReturn((Join) trainingTypeJoin);
    
        // Date and predicate mocks
        when(trainingRoot.get("date")).thenReturn((Path) datePath);
        when(cb.greaterThanOrEqualTo(datePath, fromDate)).thenReturn(mock(Predicate.class));
        when(cb.lessThanOrEqualTo(datePath, toDate)).thenReturn(mock(Predicate.class));
    
        when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
        when(cb.lower(any())).thenReturn(mock(Expression.class));
        when(cb.and(any(Predicate[].class))).thenReturn(mock(Predicate.class));
    
        when(cq.select(trainingRoot)).thenReturn(cq);
        when(cq.where(any(Predicate.class))).thenReturn(cq);
    
        when(em.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new Training()));
    
        List<Training> result = trainingRepository.findTrainingsByTrainerAndCriteria(
                "trainer1", fromDate, toDate, "trainee1"
        );
    
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void save_ShouldPersistTraining() {
        Training training = new Training();
        assertDoesNotThrow(() -> trainingRepository.save(training));
        verify(em, times(1)).persist(training);
    }

    @Test
    void get_ShouldReturnTraining_WhenExists() {
        Training expected = new Training();
        when(em.find(Training.class, 1)).thenReturn(expected);

        Optional<Training> result = trainingRepository.get(1);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void get_ShouldReturnEmpty_WhenNotFound() {
        when(em.find(Training.class, 1)).thenReturn(null);
        Optional<Training> result = trainingRepository.get(1);
        assertTrue(result.isEmpty());
    }

    @Test
    void update_ShouldMergeTraining() {
        Training training = new Training();
        trainingRepository.update(training);
        verify(em, times(1)).merge(training);
    }

    @Test
    void delete_ShouldRemoveTraining() {
        Training training = new Training();
        when(em.contains(training)).thenReturn(true);
        trainingRepository.delete(training);
        verify(em, times(1)).remove(training);
    }

    @Test
    void delete_ShouldMergeBeforeRemoving_WhenNotManaged() {
        Training training = new Training();
        Training merged = new Training();
        when(em.contains(training)).thenReturn(false);
        when(em.merge(training)).thenReturn(merged);
        trainingRepository.delete(training);
        verify(em, times(1)).remove(merged);
    }

    @Test
    void getAll_ShouldReturnListOfTrainings() {
        TypedQuery<Training> query = mock(TypedQuery.class);
        when(em.createQuery("SELECT t FROM Training t", Training.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Training()));

        List<Training> result = trainingRepository.getAll();
        assertEquals(1, result.size());
    }
}
