package com.epam.campus.gymcrm.models.dtos;

import java.util.List;

public class TrainerResponseDto {
    private String firstName;
    private String lastName;
    private boolean active;
    private List<TraineeFromListDto> trainees;
    
    public TrainerResponseDto(String firstName, String lastName, boolean active, List<TraineeFromListDto> trainees) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
        this.trainees = trainees;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
    }

    public List<TraineeFromListDto> getTrainees() {
        return trainees;
    }

    
}
