package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;
import java.util.List;

public class TraineeUpdateResponseDto extends TraineeResponseDto {
    private String username;

    public TraineeUpdateResponseDto(String username, String firstName, String lastName, LocalDate dateOfBirth, String address,
            boolean active, List<TrainerFromListDto> trainers) {
        super(firstName, lastName, dateOfBirth, address, active, trainers);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
