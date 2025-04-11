package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;

// DTO for the Training entity
public class TrainingDto {

    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private String trainingTypeName;
    private LocalDate trainingDate;
    private int trainingDuration;
    
    public TrainingDto() {}

    public TrainingDto(String traineeUsername, String trainerUsername, String trainingName, String trainingTypeName, LocalDate trainingDate, int trainingDuration) {
        this.traineeUsername = traineeUsername;
        this.trainerUsername = trainerUsername;
        this.trainingName = trainingName;
        this.trainingTypeName = trainingTypeName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    public String getTraineeUsername() {
        return traineeUsername;
    }

    public void setTraineeUsername(String traineeUsername) {
        this.traineeUsername = traineeUsername;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeId(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
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
        return "TrainingDto[traineeUsername=" + traineeUsername + ", trainerUsername=" + trainerUsername + ", trainingName=" + trainingName
                + ", trainingTypeName=" + trainingTypeName + ", trainingDate=" + trainingDate + ", trainingDuration=" + trainingDuration + "]";
    }

}
