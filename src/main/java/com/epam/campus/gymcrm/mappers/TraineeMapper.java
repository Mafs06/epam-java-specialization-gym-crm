package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.User;

@Component
public class TraineeMapper {

    public TraineeDto toDto(Trainee trainee) {
        TraineeDto traineeDto = new TraineeDto(
            trainee.getUser().getFirstName(),
            trainee.getUser().getLastName(),
            trainee.getUser().isActive(),
            trainee.getDateOfBirth(),
            trainee.getAddress());

        return traineeDto;
    }

    public Trainee toTrainee(TraineeDto traineeDto, String username, String password) {
        User user = new User(
            traineeDto.getFirstName(),
            traineeDto.getLastName(),
            username,
            password,
            traineeDto.isActive());

        return new Trainee.TraineeBuilder()
            .dateOfBirth(traineeDto.getDateOfBirth())
            .address(traineeDto.getAddress())
            .user(user)
            .build();
    }

    public void toTrainee(TraineeDto traineeDto, Trainee existingTrainee) {
        User updatedUser = existingTrainee.getUser();
        updatedUser.setFirstName(traineeDto.getFirstName());
        updatedUser.setLastName(traineeDto.getLastName());
        updatedUser.setActive(traineeDto.isActive());
        
        existingTrainee.setDateOfBirth(traineeDto.getDateOfBirth());
        existingTrainee.setAddress(traineeDto.getAddress());
    }

}
