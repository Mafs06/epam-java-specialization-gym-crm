package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id" , nullable = false)
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;

    @Column(name = "training_date", nullable = false)
    private LocalDate date;

    @Column(name = "training_duration", nullable = false)
    private int duration;
    
    public Training() {}

    public Training(TrainingBuilder builder) {
        this.trainee = builder.trainee;
        this.trainer = builder.trainer;
        this.name = builder.name;
        this.trainingType = builder.trainingType;
        this.date = builder.date;
        this.duration = builder.duration;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public String getName() {
        return name;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Training [id=" + id + ", trainee=" + trainee.toString() + ", trainer=" + trainer.toString()
                + ", name=" + name + ", trainingType=" + trainingType.toString() + ", date=" + date
                + ", duration=" + duration + "]";
    }

    public static class TrainingBuilder {
        private Trainee trainee;
        private Trainer trainer;
        private String name;
        private TrainingType trainingType;
        private LocalDate date;
        private int duration;

        public TrainingBuilder trainee(Trainee trainee) {
            this.trainee = trainee;
            return this;
        }

        public TrainingBuilder trainer(Trainer trainer) {
            this.trainer = trainer;
            return this;
        }

        public TrainingBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TrainingBuilder trainingType(TrainingType trainingType) {
            this.trainingType = trainingType;
            return this;
        }

        public TrainingBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public TrainingBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Training build() {
            return new Training(this);
        }

    }

}
