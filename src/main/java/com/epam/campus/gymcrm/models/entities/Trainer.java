package com.epam.campus.gymcrm.models.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialization" , nullable = false)
    private TrainingType trainingType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    @ManyToMany(mappedBy = "trainers")
    private List<Trainee> trainees = new ArrayList<>();

    public Trainer() {}

    public Trainer(TrainerBuilder builder) {
        this.id = builder.id;
        this.trainingType = builder.trainingType;
        this.user = builder.user;
    }

    public int getId() {
        return id;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public User getUser() {
        return user;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public List<Trainee> getTrainees() {
        return trainees;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Trainer [id=" + id + ", user=" + user.toString() + ", trainingType=" + trainingType + "]";
    }

    public static class TrainerBuilder {
        private int id;
        private TrainingType trainingType;
        private User user;

        public TrainerBuilder() {}

        public TrainerBuilder(Trainer existingTrainer) {
            this.id = existingTrainer.getId();
            this.trainingType = existingTrainer.getTrainingType();
            this.user = existingTrainer.getUser();
        }

        public TrainerBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TrainerBuilder trainingType(TrainingType trainingType) {
            this.trainingType = trainingType;
            return this;
        }

        public TrainerBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Trainer build() {
            return new Trainer(this);
        }
    }
    
}
