# Gym CRM Application

## Description
This is a Spring-based Gym CRM system designed to manage trainers, trainees, and their training sessions. The project is built following a structured plan for the Java specialization learning path of EPAM Campus.

The current stage focuses on core Spring concepts and best practices.

## Features
- **Trainee Management:** Create, update, delete, and select trainee profiles.
- **Trainer Management:** Create, update, delete and select trainer profiles.
- **Training Management:** Create, update, delete and select trainings.

### 1. Spring Configuration

The project uses **Spring annotations based configuration** for the application context:

- **`@Configuration`** — Marks the class as a source of bean definitions.
- **`@ComponentScan`** — Scans `com.epam.campus.gymcrm` package for Spring components (like `@Service`, `@Repository`, etc.).
- **`@PropertySource`** — Loads properties from the `application.properties` file to configure dynamic values like the path to the data file.

### 2. DAO Implementation

The application implements DAO objects for each domain model entity, using a shared in-memory storage map. Each entity is stored under a separate namespace:

- **TraineeDao**: Manages CRUD operations for trainees, using the `Trainee` namespace.
- **TrainerDao**: Handles CRUD operations for trainers. Data is stored under the `Trainer` namespace.
- **TrainingDao**: Supports CRUD operations for training sessions, organized in the `Training` namespace.

All DAOs implement a common `Dao<T>` interface that defines basic data operations:

```java
public interface Dao<T> {
    Optional<T> get(int id);
    List<T> getAll();
    void save(T t);
    void update(T t, String[] params);
    void delete(T t);
}
```

### 3. In-Memory Storage

The in-memory storage is implemented as a separate Spring bean on the Storage class, initialized with prepared data from an external JSON file.

The Storage class reads the data from the file specified under the storage.filepath property on the application.properties. This reading happens on application startup thanks to using @PostConstruct annotation.

### 4. Dependency Injections

The application implements a clear separation between DAOs and business logic using service and facade layers.

The service beans are injected into the GymFacade using constructor-based injection:

```java
@Autowired
public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
    this.trainerService = trainerService;
    this.traineeService = traineeService;
    this.trainingService = trainingService;
}
```

The DAOs are auto-wired into their respective service classes using setter-based dependency injection. For example, in `TraineeService`:

```java
@Autowired
public void setTraineeDao(TraineeDao traineeDao) {
    this.traineeDao = traineeDao;
}
```

### 5. Unit tests

The application is covered by unit tests using JUnit 5 and Mockito to ensure the correctness of the business logic. The three services (TraineeService, TrainerService, TrainingService) have test files that cover CRUD operations while mocking the respective Dao and verifying method calls. 

Each service test follows a similar structure. For TraineeService :

```java
@Mock
private TraineeDao traineeDao;

@InjectMocks
private TraineeService traineeService;

@BeforeEach
public void setUp() {
    MockitoAnnotations.openMocks(this);
}

@Test
public void exampleTestForTrainee() {
    // verify operations and method calls
}
```

### 6. Logging

Proper logging has been implemented using SLF4J and Logback. Logs are included at various levels (info, debug, error) to track the flow of execution and potential issues:

Services logs events like entity retrieval, creation, update, and deletion.

Storage logs data loading from the JSON file, entity mapping, and storage operations:

### 7. User Profile Generation

The application uses automatic generation of usernames and passwords for Trainees and Trainers, following these rules:

Username: Calculated by concatenating the first and last names with a dot (e.g., John.Smith). If a user with the same name already exists, a serial number is appended (e.g., John.Smith1).

Password: A random 10-character string composed of uppercase, lowercase letters, and digits.

This behavior is implemented on the User class that both of them extend.

```java
public String generateUsername(List<User> existingUsers) {
    String baseUsername = firstName + "." + lastName;
    List<String> existingUsernames = existingUsers.stream()
        .map(User::getUsername)
        .collect(Collectors.toList());

    String username = baseUsername;
    int counter = 1;
    while (existingUsernames.contains(username)) {
        username = baseUsername + counter;
        counter++;
    }
    this.username = username;
    return username;
}

public String generatePassword() {
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
        password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
    }
    this.password = password.toString();
    return this.password;
}
```

mvn clean package
docker-compose build
docker-compose up --build

docker attach gym_app
docker-compose down -v

http://localhost:5050