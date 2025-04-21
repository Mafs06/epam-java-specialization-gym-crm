package com.epam.campus.gymcrm.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TraineeCreationDto;
import com.epam.campus.gymcrm.models.dtos.TraineeFromListDto;
import com.epam.campus.gymcrm.models.dtos.TraineeResponseDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;
import com.epam.campus.gymcrm.models.entities.Trainee;
import com.epam.campus.gymcrm.models.entities.User;

@Component
public abstract class TraineeMapper {

    public static Trainee toTrainee(TraineeCreationDto newTraineeDto, String username, String password, Boolean active) {
        User user = new User(
            newTraineeDto.getFirstName(),
            newTraineeDto.getLastName(),
            username,
            password,
            active);

        return new Trainee.TraineeBuilder()
            .dateOfBirth(newTraineeDto.getDateOfBirth())
            .address(newTraineeDto.getAddress())
            .user(user)
            .build();
    }

    public static void updateTrainee(TraineeUpdateRequestDto traineeUpdateDto, Trainee existingTrainee) {
        User updatedUser = existingTrainee.getUser();
        updatedUser.setFirstName(traineeUpdateDto.getFirstName());
        updatedUser.setLastName(traineeUpdateDto.getLastName());
        updatedUser.setActive(traineeUpdateDto.getActive());
        
        if (traineeUpdateDto.getDateOfBirth() != null){
            existingTrainee.setDateOfBirth(traineeUpdateDto.getDateOfBirth());
        }
        
        if (traineeUpdateDto.getAddress() != null) {
            existingTrainee.setAddress(traineeUpdateDto.getAddress());
        }
    }

    public static TraineeFromListDto toListDto (Trainee trainee) {
        return new TraineeFromListDto(
            trainee.getUser().getUsername(), 
            trainee.getUser().getFirstName(), 
            trainee.getUser().getLastName());
    }

    public static TraineeResponseDto toResponseDto(Trainee trainee) {
        List<TrainerFromListDto> trainerDtos = trainee.getTrainers()
            .stream()
            .map(TrainerMapper::toListDto)
            .collect(Collectors.toList());
        
        return new TraineeResponseDto(
            trainee.getUser().getFirstName(),
            trainee.getUser().getLastName(),
            trainee.getDateOfBirth(),
            trainee.getAddress(),
            trainee.getUser().isActive(),
            trainerDtos);
    }

    public static TraineeUpdateResponseDto toUpdateResponseDto(String username, TraineeResponseDto incompleteUpdateResponse) {
        return new TraineeUpdateResponseDto(
            username,
            incompleteUpdateResponse.getFirstName(),
            incompleteUpdateResponse.getLastName(),
            incompleteUpdateResponse.getDateOfBirth(),
            incompleteUpdateResponse.getAddress(),
            incompleteUpdateResponse.isActive(),
            incompleteUpdateResponse.getTrainers()
        ); 
    }

}
