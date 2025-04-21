package com.epam.campus.gymcrm.models.dtos;

import java.util.List;

public class TrainerUpdateResponseDto extends TrainerResponseDto {
    private String username;

    public TrainerUpdateResponseDto(String username, String firstName, String lastName, boolean active,
            List<TraineeFromListDto> trainees) {
        super(firstName, lastName, active, trainees);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
