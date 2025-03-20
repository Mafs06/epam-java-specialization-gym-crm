package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

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
        this.id = builder.id;
        this.traineeID = builder.traineeID;
        this.trainerID = builder.trainerID;
        this.name = builder.name;
        this.trainingTypeID = builder.trainingTypeID;
        this.date = builder.date;
        this.duration = builder.duration;
    }

    public int getId() {
        return id;
    }

    public int getTraineeID() {
        return traineeID;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public String getName() {
        return name;
    }

    public int gettrainingTypeID() {
        return trainingTypeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Training [id=" + id + ", traineeID=" + traineeID + ", trainerID=" + trainerID
                + ", name=" + name + ", trainingTypeID=" + trainingTypeID + ", date=" + date
                + ", duration=" + duration + "]";
    }

    public static class TrainingBuilder {
        private int id;
        private int traineeID;
        private int trainerID;
        private String name;
        private int trainingTypeID;
        private LocalDate date;
        private int duration;

        public TrainingBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TrainingBuilder traineeID(int traineeID) {
            this.traineeID = traineeID;
            return this;
        }

        public TrainingBuilder trainerID(int trainerID) {
            this.trainerID = trainerID;
            return this;
        }

        public TrainingBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TrainingBuilder trainingTypeID(int trainingTypeID) {
            this.trainingTypeID = trainingTypeID;
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
