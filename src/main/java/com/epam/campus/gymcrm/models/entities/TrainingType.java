package com.epam.campus.gymcrm.models.entities;

public class TrainingType {

    private int id;
    private String name;

    public TrainingType() {}

    public TrainingType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TrainingType [id=" + id + " name=" + name + "]";
    }

}
