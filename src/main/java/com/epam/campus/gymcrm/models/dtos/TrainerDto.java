package com.epam.campus.gymcrm.models.dtos;

// DTO for the Trainer entity, does not contain ids nor username/password
public class TrainerDto {

    private String firstName;
    private String lastName;
    private boolean active;
    private String specialization;

    public TrainerDto() {}

    public TrainerDto(String firstName, String lastName, boolean active, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
                + ", active=" + active + ", specialization=" + specialization + "]";
    }

}
