package com.epam.campus.gymcrm.models;

public class Trainer extends User {

    private String specialization;
    private int trainerID;

    public Trainer() {
        super();
    }

    public Trainer(int trainerID, String firstName, String lastName, String username, String password, boolean active, String specialization) {
        super(firstName, lastName, username, password, active);
        this.specialization = specialization;
        this.trainerID = trainerID;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

    @Override
    public String toString() {
        return "Trainer [trainerID=" + trainerID + ", user=" + super.toString() + " specialization=" + specialization + "]";
    }
    
}
