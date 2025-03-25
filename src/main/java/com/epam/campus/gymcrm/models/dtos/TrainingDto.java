package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;

import com.epam.campus.gymcrm.models.entities.TrainingType;

// DTO for the Training entity
public class TrainingDto {

    private int traineeID;
    private int trainerID;
    private String trainingName;
    private int trainingTypeID;
    private LocalDate trainingDate;
    private int trainingDuration;
    
    public TrainingDto() {}

    public TrainingDto(int traineeID, int trainerID, String trainingName, int trainingTypeID, LocalDate trainingDate, int trainingDuration) {
        this.traineeID = traineeID;
        this.trainerID = trainerID;
        this.trainingName = trainingName;
        this.trainingTypeID = trainingTypeID;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
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

    public int getTrainingTypeID() {
        return trainingTypeID;
    }

    public void setTrainingType(int trainingTypeID) {
        this.trainingTypeID = trainingTypeID;
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
        return "[traineeID=" + traineeID + ", trainerID=" + trainerID + ", trainingName=" + trainingName
                + ", trainingTypeID=" + trainingTypeID + ", trainingDate=" + trainingDate
                + ", trainingDuration=" + trainingDuration + "]";
    }

}
