package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;

public class Training {

    private int id;
    private int traineeID;
    private int trainerID;
    private String name;
    private int trainingTypeID;
    private LocalDate date;
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
