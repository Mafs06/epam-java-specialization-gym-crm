package com.epam.campus.gymcrm.models.dtos;

public class TrainerFromListDto extends UserListDto {
    private String specialization;

    public TrainerFromListDto(String username, String firstName, String lastName, String specialization) {
        super(username, firstName, lastName);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}
