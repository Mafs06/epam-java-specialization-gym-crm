package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

// DTO for the Trainee entity, does not contain ids nor username/password
public class TraineeCreationDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;

    public TraineeCreationDto() {
    }

    public TraineeCreationDto(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
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

    @Override
    public String toString() {
        return "TraineeDto[firstName=" + firstName + ", lastName=" + lastName +
            ", dateOfBirth=" + dateOfBirth + ", address=" + address + "]";
    }

}
