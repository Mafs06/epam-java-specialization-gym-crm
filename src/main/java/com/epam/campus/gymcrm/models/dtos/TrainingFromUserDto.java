package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;
import java.util.Objects;

public class TrainingFromUserDto {
    private String trainingName;
    private LocalDate trainingDate;
    private String trainingTypeName;
    private int trainingDuration;
    private String userUsername;

    public TrainingFromUserDto() { }
    
    public TrainingFromUserDto(String trainingName, LocalDate trainingDate, String trainingTypeName, int trainingDuration, String userUsername) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingTypeName = trainingTypeName;
        this.trainingDuration = trainingDuration;
        this.userUsername = userUsername;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public String getTrainingTypeName() {
        return trainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingFromUserDto that = (TrainingFromUserDto) o;
        return trainingDuration == that.trainingDuration &&
               Objects.equals(trainingName, that.trainingName) &&
               Objects.equals(trainingDate, that.trainingDate) &&
               Objects.equals(trainingTypeName, that.trainingTypeName) &&
               Objects.equals(userUsername, that.userUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingName, trainingDate, trainingTypeName, trainingDuration, userUsername);
    }

}
