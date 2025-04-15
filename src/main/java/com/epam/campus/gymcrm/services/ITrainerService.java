package com.epam.campus.gymcrm.services;

import java.util.List;

import com.epam.campus.gymcrm.models.dtos.LoginChangeDto;
import com.epam.campus.gymcrm.models.dtos.LoginDto;
import com.epam.campus.gymcrm.models.dtos.TrainerCreationDto;
import com.epam.campus.gymcrm.models.dtos.TrainerFromListDto;
import com.epam.campus.gymcrm.models.dtos.TrainerResponseDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateRequestDto;
import com.epam.campus.gymcrm.models.dtos.TrainerUpdateResponseDto;

public interface ITrainerService {

    LoginDto createTrainer(TrainerCreationDto trainerDto);

    TrainerUpdateResponseDto updateTrainer(String username, TrainerUpdateRequestDto trainerUpdateDto);

    boolean trainerLogin(LoginDto loginDto);

    TrainerResponseDto getTrainerByUsername(String username);

    boolean updateTrainerPassword(String username, LoginChangeDto loginChangeDto);

    boolean switchTrainerActiveStatus(String username);

    void updateActiveStatus(String username, boolean active);

}
