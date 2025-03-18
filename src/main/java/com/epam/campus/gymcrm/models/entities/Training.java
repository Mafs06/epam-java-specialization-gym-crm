package com.epam.campus.gymcrm.models.entities;

import java.time.LocalDate;

public class Training {

    private int id;
    private int traineeID;
    private int trainerID;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private int trainingDuration;
    
    public Training() {}

    public Training(TrainingBuilder builder) {
        this.id = builder.id;
        this.traineeID = builder.traineeID;
        this.trainerID = builder.trainerID;
        this.trainingName = builder.trainingName;
        this.trainingType = new TrainingType(builder.trainingName);
        this.trainingDate = builder.trainingDate;
        this.trainingDuration = builder.trainingDuration;
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

    public String getTrainingName() {
        return trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    @Override
    public String toString() {
        return "Training [id=" + id + ", traineeID=" + traineeID + ", trainerID=" + trainerID
                + ", trainingName=" + trainingName + ", trainingType=" + trainingType + ", trainingDate=" + trainingDate
                + ", trainingDuration=" + trainingDuration + "]";
    }

    public static class TrainingBuilder {
        private int id;
        private int traineeID;
        private int trainerID;
        private String trainingName;
        private LocalDate trainingDate;
        private int trainingDuration;

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

        public TrainingBuilder trainingName(String trainingName) {
            this.trainingName = trainingName;
            return this;
        }

        public TrainingBuilder trainingDate(LocalDate trainingDate) {
            this.trainingDate = trainingDate;
            return this;
        }

        public TrainingBuilder trainingDuration(int trainingDuration) {
            this.trainingDuration = trainingDuration;
            return this;
        }

        public Training build() {
            return new Training(this);
        }

    }

}
