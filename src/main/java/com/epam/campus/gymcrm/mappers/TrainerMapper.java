package com.epam.campus.gymcrm.mappers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.epam.campus.gymcrm.models.dtos.TraineeFromListDto;
import com.epam.campus.gymcrm.models.dtos.TrainerCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;
import com.epam.campus.gymcrm.models.dtos.TrainerResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateResponseDto;
import com.epam.campus.gymcrm.models.entities.Trainer;
import com.epam.campus.gymcrm.models.entities.TrainingType;
import com.epam.campus.gymcrm.models.entities.User;

@Component
public abstract class TrainerMapper {

    public static TrainerCreationDto toDto(Trainer trainer) {
        TrainerCreationDto trainerDto = new TrainerCreationDto(
            trainer.getUser().getFirstName(),
            trainer.getUser().getLastName(),
            trainer.getTrainingType().getName());

        return trainerDto;
    }

    public static Trainer toTrainer(TrainerCreationDto trainerDto, TrainingType trainingType, String username, String password, boolean active) {
        User user = new User(
            trainerDto.getFirstName(),
            trainerDto.getLastName(),
            username,
            password,
            active);

        return new Trainer.TrainerBuilder()
            .trainingType(trainingType)
            .user(user)
            .build();
    }

    public static void updateTrainer(TrainerUpdateRequestDto trainerUpdateDto, Trainer existingTrainer, TrainingType trainingType) {
        User updatedUser = existingTrainer.getUser();
        updatedUser.setFirstName(trainerUpdateDto.getFirstName());
        updatedUser.setLastName(trainerUpdateDto.getLastName());
        updatedUser.setActive(trainerUpdateDto.getActive());
    
        existingTrainer.setTrainingType(trainingType);
    }
    
    public static TrainerFromListDto toListDto (Trainer trainer) {
        return new TrainerFromListDto(
            trainer.getUser().getUsername(), 
            trainer.getUser().getFirstName(),
            trainer.getUser().getLastName(),
            trainer.getTrainingType().getName());
    }

    public static TrainerResponseDto toResponseDto(Trainer trainer) {
        List<TraineeFromListDto> traineeDtos = trainer.getTrainees()
            .stream()
            .map(TraineeMapper::toListDto)
            .collect(Collectors.toList());

        return new TrainerResponseDto(
            trainer.getUser().getFirstName(), 
            trainer.getUser().getLastName(), 
            trainer.getUser().isActive(), 
            traineeDtos);
    }

    public static TrainerUpdateResponseDto toUpdateResponseDto(String username, TrainerResponseDto incompleteUpdateResponse) {
        return new TrainerUpdateResponseDto(
            username, 
            incompleteUpdateResponse.getFirstName(), 
            incompleteUpdateResponse.getLastName(),
            incompleteUpdateResponse.isActive(),
            incompleteUpdateResponse.getTrainees());
    }
}
