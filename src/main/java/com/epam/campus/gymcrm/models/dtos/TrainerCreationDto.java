package com.epam.campus.gymcrm.models.dtos;

import jakarta.validation.constraints.NotBlank;

// DTO for the Trainer entity, does not contain ids nor username/password
public class TrainerCreationDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String specialization;

    public TrainerCreationDto() {}

    public TrainerCreationDto(String firstName, String lastName, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "TrainerDto[firstName=" + firstName + ", lastName=" + lastName
                + ", specialization=" + specialization + "]";
    }

}
