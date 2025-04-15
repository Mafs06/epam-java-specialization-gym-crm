package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;
import java.util.List;

public class TraineeResponseDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean active;
    private List<TrainerFromListDto> trainers;
    
    public TraineeResponseDto(String firstName, String lastName, LocalDate dateOfBirth, String address, boolean active,
            List<TrainerFromListDto> trainers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.active = active;
        this.trainers = trainers;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public List<TrainerFromListDto> getTrainers() {
        return trainers;
    }
    public void setTrainers(List<TrainerFromListDto> trainers) {
        this.trainers = trainers;
    }

    @Override
    public String toString() {
        return "TraineeResponseDto [firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth
                + ", address=" + address + ", active=" + active + ", trainers=" + trainers + "]";
    }

}
