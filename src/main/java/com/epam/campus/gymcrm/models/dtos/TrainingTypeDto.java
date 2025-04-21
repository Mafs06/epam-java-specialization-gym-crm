package com.epam.campus.gymcrm.models.dtos;

public class TrainingTypeDto {

    private String name;

    public TrainingTypeDto() {}

    public TrainingTypeDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TrainingTypeDto [name=" + name + "]";
    }

}
