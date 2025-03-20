package com.epam.campus.gymcrm.mappers;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TraineeDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.User;

@Component
public class TraineeMapper {

    public TraineeDto toDto(Trainee trainee) {
        TraineeDto traineeDto = new TraineeDto(
            trainee.getId(),
            trainee.getFirstName(),
            trainee.getLastName(),
            trainee.isActive(),
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

    public Trainee toTrainee(TraineeDto traineeDto, Trainee existingTrainee) {
        Trainee updatedTrainee = new Trainee.TraineeBuilder()
            .id(existingTrainee.getId()) // Keep id
            .firstName(traineeDto.getFirstName())
            .lastName(traineeDto.getLastName())
            .username(existingTrainee.getUsername()) // Keep username
            .password(existingTrainee.getPassword()) // Keep password
            .active(traineeDto.isActive())
            .dateOfBirth(traineeDto.getDateOfBirth())
            .address(traineeDto.getAddress())
            .build();

        return updatedTrainee;
    }

}
