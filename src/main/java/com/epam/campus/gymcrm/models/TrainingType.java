package com.epam.campus.gymcrm.models;

public class TrainingType {

    private String name;

    

    public TrainingType() {}

    public TrainingType(String name) {
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
        return "TrainingType [name=" + name + "]";
    }

}
