package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TraineeCreationDto;
import com.epam.campus.gymcrm.models.dtos.TraineeResponseDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TraineeUpdateResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;

public interface ITraineeService {

    LoginDto createTrainee(TraineeCreationDto newTraineeDto);

    TraineeUpdateResponseDto updateTrainee(String username, TraineeUpdateRequestDto traineeUpdateDto);

    void deleteTrainee(String username);

    boolean traineeLogin(LoginDto userLoginDto);

    TraineeResponseDto getTraineeByUsername(String username);

    boolean updateTraineePassword(String username, LoginChangeDto loginChangeDto);

    boolean switchTraineeActiveStatus(String username);

    void updateActiveStatus(String username, boolean active);

    List<TrainerFromListDto> updateTraineeTrainersList(String traineeUsername, List<String> trainerUsernames);

    List<TrainerFromListDto> getActiveTrainersNotAssignedToTrainee(String traineeUsername);
}
