package com.epam.campus.gymcrm.models.dtos;

import java.time.LocalDate;

// DTO for the Trainee entity, does not contain ids nor username/password
public class TraineeDto {

    private String firstName;
    private String lastName;
    private boolean active;
    private LocalDate dateOfBirth;
    private String address;

    public TraineeDto() {
    }

    public TraineeDto(String firstName, String lastName, boolean active, LocalDate dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        return "[firstName=" + firstName + ", lastName=" + lastName + ", active=" + active
            + ", dateOfBirth=" + dateOfBirth + ", address=" + address + "]";
    }

}
