package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;

import com.epam.campus.gymcrm.models.entities.TrainingType;

// DTO for the Training entity
public class TrainingDto {

    private int id;
    private int traineeID;
    private int trainerID;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private int trainingDuration;
    
    public TrainingDto(int id, int traineeID, int trainerID, String trainingName,
            LocalDate trainingDate, int trainingDuration) {
        this.id = id;
        this.traineeID = traineeID;
        this.trainerID = trainerID;
        this.trainingName = trainingName;
        this.trainingType = new TrainingType(trainingName);
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "[id=" + id + ", traineeID=" + traineeID + ", trainerID=" + trainerID + ", trainingName="
                + trainingName + ", trainingType=" + trainingType + ", trainingDate=" + trainingDate
                + ", trainingDuration=" + trainingDuration + "]";
    }

}
