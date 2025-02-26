package com.epam.campus.gymcrm.models;

import java.time.LocalDate;

public class Training {

    private int trainingID;
    private int traineeID;
    private int trainerID;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private int trainingDuration;
    
    public Training(int trainingID, int traineeID, int trainerID, String trainingName, TrainingType trainingType, LocalDate trainingDate, int trainingDuration) {
        this.trainingID = trainingID;
        this.traineeID = traineeID;
        this.trainerID = trainerID;
        this.trainingName = trainingName;
        this.trainingType = new TrainingType(trainingName);
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public int getTrainingID() {
        return trainingID;
    }

    public void setTrainingID(int trainingID) {
        this.trainingID = trainingID;
    }

    public int getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    @Override
    public String toString() {
        return "Training [trainingID=" + trainingID + ", traineeID=" + traineeID + ", trainerID=" + trainerID
                + ", trainingName=" + trainingName + ", trainingType=" + trainingType + ", trainingDate=" + trainingDate
                + ", trainingDuration=" + trainingDuration + "]";
    }

}
